package assignment.moneytap.com.wikipedia.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.client.QueryManager;
import assignment.moneytap.com.wikipedia.data.Page;
import assignment.moneytap.com.wikipedia.data.Response;
import assignment.moneytap.com.wikipedia.view.SuggestionAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        QueryManager.OnResponseListener, SearchView.OnSuggestionListener {

    private static final String TAG = "MainActivity";

    private SearchView searchView;
    private QueryManager queryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryManager = QueryManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryManager.registerListener(this);
    }

    protected void onStop() {
        super.onStop();
        queryManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Init searchView and set listeners
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_title));
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        WikiLog.e(TAG, newText);
        queryManager.scheduleQuery(newText);
        return false;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor= searchView.getSuggestionsAdapter().getCursor();
        cursor.moveToPosition(position);
        String title = cursor.getString(2);
        String url = cursor.getString(4);
        searchView.setQuery(title,true);
        Intent intent = new Intent(getApplicationContext(), WikiPageActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
        return true;
    }

    @Override
    public void onResponseReceived(int what, Response response) {
        WikiLog.e(TAG, response.toString());
        final Cursor cursor = getCursor(response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SuggestionAdapter suggestionAdapter = new SuggestionAdapter(
                        getApplicationContext(), cursor, false);
                searchView.setSuggestionsAdapter(suggestionAdapter);
            }
        });
    }

    public MatrixCursor getCursor(Response response) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[] {"_id", "icon", "title", "desc", "url"});

        if(!response.isBatchComplete() || response.getQuery() == null)
            return null;

        for(Page page : response.getQuery().getPages()) {

            MatrixCursor.RowBuilder rowBuilder = matrixCursor.newRow();
            rowBuilder.add("_id", page.getPageId())
                    .add("title", page.getTitle());
            if(page.getThumbnail() != null)
                rowBuilder.add("icon", page.getThumbnail().getSource());
            if(page.getTerms() != null)
                rowBuilder.add("desc", page.getTerms().getDescription().get(0));
            if(page.getUrl() != null)
                rowBuilder.add("url", page.getUrl());
        }

        return matrixCursor;
    }
}
