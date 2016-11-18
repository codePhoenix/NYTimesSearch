package com.hphays.nytimessearch.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
import android.os.Parcel;

import com.hphays.nytimessearch.Article;
import com.hphays.nytimessearch.ArticleArrayAdapter;
import com.hphays.nytimessearch.EndlessScrollListener;
import com.hphays.nytimessearch.R;
import com.hphays.nytimessearch.SearchFilters;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import layout.SearchFiltersDialogFragment;

public class SearchActivity extends AppCompatActivity implements SearchFiltersDialogFragment.EditNameDialogListener {

    EditText etQuery;
    String queryString;
    GridView gvResults;
    Button btnSearch;
    SearchFilters searchFilters = new SearchFilters();
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                Article article = articles.get(position);
                //pass in that article into intent
                i.putExtra("article", article);
                // launch the activity
                startActivity(i);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(page);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                queryString = query;
                adapter.clear();
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

                RequestParams params = new RequestParams();

                params.put("api-key", "f00016086fc44568868f0e0ce191376d");
                params.put("page", 0);
                params.put("q", query);

                if (searchFilters.isArts()) {
                    params.put("fq", "news_desk:(\\\"Arts\\\")");
                }

                if (searchFilters.isFashionAndStyle()) {
                    params.put("fq", "news_desk:(\\\"Fashion & Style\\\")");
                }

                if (searchFilters.isSports()) {
                    params.put("fq", "news_desk:(\\\"Sports\\\")");
                }

                if (searchFilters.isOldest()) {
                    params.put("sort", "oldest");
                }

                if (searchFilters.isNewest()) {
                    params.put("sort", "newest");
                }

                if (searchFilters.getBeginDate() != null) {
                    params.put("begin_date", searchFilters.getBeginDate());
                }

                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());

                        JSONArray articleJsonResults = null;

                        try{
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            adapter.addAll(Article.fromJSONArray(articleJsonResults));
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            Log.d("DEBUG", "Initial query failed!!!!!!!!");
                            e.printStackTrace();
                        }
                    }
                });

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
         // if (id == R.id.action_settings) {
            return true;
        }

        //return super.onOptionsItemSelected(item);

    public void onDetailClick(View view) {
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchFiltersDialogFragment searchFiltersDialogFragment = SearchFiltersDialogFragment.newInstance("Some Title");

        searchFiltersDialogFragment.show(fm, "fragment_search_filters");
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();

        params.put("api-key", "f00016086fc44568868f0e0ce191376d");
        params.put("page", offset);
        params.put("q", queryString);

        if (searchFilters.isArts()) {
            params.put("fq", "news_desk:(\\\"Arts\\\")");
        }

        if (searchFilters.isFashionAndStyle()) {
            params.put("fq", "news_desk:(\\\"Fashion & Style\\\")");
        }

        if (searchFilters.isSports()) {
            params.put("fq", "news_desk:(\\\"Sports\\\")");
        }

        if (searchFilters.isOldest()) {
            params.put("sort", "oldest");
        }

        if (searchFilters.isNewest()) {
            params.put("sort", "newest");
        }

        if (searchFilters.getBeginDate() != null) {
            params.put("begin_date", searchFilters.getBeginDate());
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults = null;

                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
    }

    public void onFinishEditDialog(Parcelable filters) {

        searchFilters =  (SearchFilters) Parcels.unwrap(filters);
    }
}
