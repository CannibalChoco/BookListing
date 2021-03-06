package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements
        LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;

    // search urls
    private static final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    private String queryUrl;

    private LoaderManager loaderManager;
    private BookAdapter adapter;

    // TextView that is displayed when the list is empty
    private TextView emptyStateTextView;
    ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ListView bookListView = (ListView) findViewById(R.id.list);
        emptyStateTextView = (TextView) findViewById(R.id.empty_state_text_view);
        bookListView.setEmptyView(emptyStateTextView);
        emptyStateTextView.setText(R.string.enter_query);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(GONE);

        adapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(adapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Book currentBook = (Book) adapter.getItem(position);
                openWebPage(currentBook.getUrl());
            }
        });
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // set onQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.onActionViewCollapsed();

                // check for network connection
                if (isConnected()) {
                    loaderManager = getLoaderManager();
                    // create new query url
                    createQueryUrl(query);
                    // restart loader
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    loadingIndicator.setVisibility(View.VISIBLE);
                } else {
                    adapter.clear();
                    emptyStateTextView.setText(R.string.no_internet_connection);
                    loadingIndicator.setVisibility(GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, queryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator.setVisibility(GONE);
        emptyStateTextView.setText(R.string.no_books_found);

        adapter.clear();
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        queryUrl = null;
        adapter.clear();
    }

    /**
     * Concat the user query with the base url
     *
     * @param query users query
     */
    public void createQueryUrl(String query) {
        String urlQuery = query.replaceAll(" ", "+");
        queryUrl = baseUrl + urlQuery;
    }

}
