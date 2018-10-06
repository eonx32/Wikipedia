package assignment.moneytap.com.wikipedia.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.PageORM;
import assignment.moneytap.com.wikipedia.Util.WikiConstants.ColumnType;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.client.QueryManager;
import assignment.moneytap.com.wikipedia.data.Page;
import assignment.moneytap.com.wikipedia.data.Response;
import assignment.moneytap.com.wikipedia.view.CachedPageAdapter;
import assignment.moneytap.com.wikipedia.view.SuggestionAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        QueryManager.OnResponseListener, SearchView.OnSuggestionListener, ListView.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private SearchView searchView;
    private QueryManager queryManager;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryManager = QueryManager.getInstance();
        listView = findViewById(R.id.cache_list);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryManager.registerListener(this);
        initCachedList();
    }

    private void initCachedList() {
        Context context = getApplicationContext();
        CachedPageAdapter cachedPageAdapter = new CachedPageAdapter(
                context, PageORM.getPages());
        listView.setAdapter(cachedPageAdapter);
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
        queryManager.scheduleQuery(QueryManager.SEND_QUERY, newText);
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

        String icon = cursor.getString(ColumnType.ICON.ordinal());
        String title = cursor.getString(ColumnType.TITLE.ordinal());
        String url = cursor.getString(ColumnType.URL.ordinal());
        PageORM.insertPage(cursor);

        searchView.setQuery(title,true);

        Intent intent = new Intent(getApplicationContext(), WikiPageActivity.class);
        intent.putExtra(ColumnType.URL.toString(), url);
        intent.putExtra(ColumnType.TITLE.toString(), title);
        intent.putExtra(ColumnType.ICON.toString(), icon);
        startActivity(intent);

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Page page = (Page)adapterView.getItemAtPosition(i);
        Intent intent = new Intent(getApplicationContext(), WikiPageActivity.class);
        intent.putExtra(ColumnType.URL.toString(), page.getUrl());
        intent.putExtra(ColumnType.TITLE.toString(), page.getTitle());
        intent.putExtra(ColumnType.ICON.toString(), page.getThumbnail().getSource());
        startActivity(intent);
    }

    @Override
    public void onResponseReceived(int what, Response response) {
        WikiLog.e(TAG, response.toString());
        switch (what) {
            case QueryManager.SEND_QUERY: {
                final Cursor cursor = PageORM.getCursor(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(
                                getApplicationContext(), cursor, false);
                        searchView.setSuggestionsAdapter(suggestionAdapter);
                    }
                });
            }
            break;
        }
    }
}
