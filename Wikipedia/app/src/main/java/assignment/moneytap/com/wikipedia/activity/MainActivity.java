package assignment.moneytap.com.wikipedia.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.PageORM;
import assignment.moneytap.com.wikipedia.Util.WikiConstants.ColumnType;
import assignment.moneytap.com.wikipedia.client.QueryManager;
import assignment.moneytap.com.wikipedia.data.Page;
import assignment.moneytap.com.wikipedia.data.Response;
import assignment.moneytap.com.wikipedia.view.CachedPageAdapter;
import assignment.moneytap.com.wikipedia.view.SuggestionAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        QueryManager.OnResponseListener, SearchView.OnSuggestionListener, View.OnClickListener,
        CachedPageAdapter.OnItemClickListener{

    private static final String TAG = "MainActivity";

    private MenuItem searchItem;
    private SearchView searchView;
    private QueryManager queryManager;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryManager = QueryManager.getInstance();
        recyclerView = findViewById(R.id.cache_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.search);
        fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryManager.registerListener(this);
        initCachedList();
    }

    private void initCachedList() {
        CachedPageAdapter cachedPageAdapter = new CachedPageAdapter(PageORM.getPages(), this);
        recyclerView.setAdapter(cachedPageAdapter);
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
        searchItem = menu.findItem(R.id.search);
        searchItem.setVisible(false);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_title));
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setSuggestionsAdapter(new SuggestionAdapter(
                getApplicationContext(), null, this));

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queryManager.scheduleQuery(QueryManager.SEND_QUERY, query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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
    public void onClick(View view) {
        if(searchItem.isActionViewExpanded())
            searchItem.collapseActionView();
        else searchItem.expandActionView();
    }

    @Override
    public void onResponseReceived(int what, Response response) {
        switch (what) {
            case QueryManager.SEND_QUERY: {
                final Cursor cursor = PageORM.getCursor(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView.getSuggestionsAdapter().swapCursor(cursor);
                    }
                });
            }
            break;
        }
    }

    @Override
    public void onItemClick(int position, Page page) {
        Intent intent = new Intent(getApplicationContext(), WikiPageActivity.class);
        intent.putExtra(ColumnType.URL.toString(), page.getUrl());
        intent.putExtra(ColumnType.TITLE.toString(), page.getTitle());
        intent.putExtra(ColumnType.ICON.toString(), page.getThumbnail().getSource());
        startActivity(intent);
    }
}
