package com.jiajie.design;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jiajie.design.api.GankService;
import com.jiajie.design.api.SearchResponse;
import com.jiajie.design.api.SearchResult;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements
        CameraFragment.CameraInteractionListener,
        GalleryFragment.OnListFragmentInteractionListener,
        SearchFragment.SearchInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton fab;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    //    CollapsingToolbarLayout collapsingToolbarLayout;
    NavigationView navigationView;

    static final String cameraTag = "Camera";
    static final String galleryTag = "Gallery";
    static final String searchTag = "Search";

    //fragments
    FragmentManager mFragmentManager;
    CameraFragment mCameraFragment;
    //    GalleryFragment mGalleryFragment;
    SearchFragment mSearchFragment;
    GalleryFragment mGalleryFragment;

    Fragment mCurrentFragment;
    String mLastFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.navigation);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed() called with: " + "drawerView = [" + drawerView + "]");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpened() called with: " + "drawerView = [" + drawerView + "]");
            }

        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switchContent(id);
                return true;
            }
        });
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // init fragments...
        if (findViewById(R.id.fragment_container) != null) {
            Log.i(TAG, "onCreate: savedInstanceState : " + savedInstanceState);
            mFragmentManager = getSupportFragmentManager();
            // 不过，如果我们要从先前的状态还原，
            // 则无需执行任何操作而应返回
            // 否则就会得到重叠的 Fragment 。
            if (savedInstanceState != null) {
                Log.v(TAG, "onCreate: restored");
            } else {
                //all fragments
                mCameraFragment = CameraFragment.newInstance("1", "2");
                mCurrentFragment = mCameraFragment;
                mLastFragmentTag = mCurrentFragment.getTag();
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                // 将该 Fragment 添加到“fragment_container”FrameLayout 中
                transaction.add(R.id.fragment_container, mCameraFragment, cameraTag).commit();
//                collapsingToolbarLayout.setTitle(mCameraFragment.getTag());
                toolbar.setTitle(mCameraFragment.getTag());
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCurrentFragment != null) {
            outState.putString("tag", mCurrentFragment.getTag());
        }
        Log.e(TAG, "onSaveInstanceState: " + outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(drawerToggle);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        if (drawerLayout != null) {
            drawerLayout.removeDrawerListener(drawerToggle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    private void showSnackBar() {
        Snackbar.make(rootLayout, "Hello.I am Snackbar!", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: Snackbar");
                    }
                }).show();
    }

    private void afterCloseSearch() {
        //close searchFragment and add lastFragment back
        Log.i(TAG, "afterCloseSearch: mCurrentFragment:" + mCurrentFragment.getTag());
        Log.i(TAG, "afterCloseSearch: mLastFragmentTag:" + mLastFragmentTag);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment lastFragment = mFragmentManager.findFragmentByTag(mLastFragmentTag);

        transaction.remove(mCurrentFragment).show(lastFragment).commit();
        mCurrentFragment = lastFragment;
        mLastFragmentTag = mCurrentFragment.getTag();
//        collapsingToolbarLayout.setTitle(mLastFragmentTag);
        toolbar.setTitle(mLastFragmentTag);
    }

    /** Swaps fragments in the menu_main content view */
    private void switchContent(int id) {
        mLastFragmentTag = mCurrentFragment.getTag();

        Fragment toFragment = null;
        String tag = "";
        // Highlight the selected item, update the title, and close the drawer
        navigationView.setCheckedItem(id);
        drawerLayout.closeDrawer(navigationView);

        switch (id) {
            case R.id.nav_camera:
                if (mCameraFragment == null) {
                    mCameraFragment = CameraFragment.newInstance("1", "2");
                }
                tag = cameraTag;
                toFragment = mCameraFragment;
                break;

            case R.id.nav_gallery:
                if (mGalleryFragment == null) {
                    mGalleryFragment = GalleryFragment.newInstance(
                            getResources().getInteger(R.integer.span_count));
                }
                tag = galleryTag;
                toFragment = mGalleryFragment;
                break;

            case R.id.nav_tools:
                break;

            case R.id.nav_exit:
                //TODO logout account and return login
                break;

            case R.id.action_search:
                Log.d(TAG, "switchContent: action_search");
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment.newInstance(1);
                }
                tag = searchTag;
                toFragment = mSearchFragment;
                break;

            default:
                break;
        }

        if (toFragment == null || mCurrentFragment == toFragment) {
            Log.e(TAG, "switchContent: same fragment");
            return;
        }

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (toFragment.isAdded()) {
            //has been added
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.show(toFragment);
        } else {
            //not added
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.fragment_container, toFragment, tag);
        }
        transaction.commit();
        mCurrentFragment = toFragment;

//        collapsingToolbarLayout.setTitle(tag);
        toolbar.setTitle(tag);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setupSearch(menu);
        return true;
    }

    private void setupSearch(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(true);
//        searchView.setIconified(false);
        Log.i(TAG, "SearchView: " + searchView.getQueryHint());
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e(TAG, "onClose: ");
                //close searchFragment and return to lastFragment
                afterCloseSearch();

                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ");
                //switch to search fragment
                switchContent(R.id.action_search);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: query: " + query);
                //if current fragment is not search fragment,switch
                if (mCurrentFragment != null && !(mCurrentFragment instanceof SearchFragment)) {
                    switchContent(R.id.action_search);
                }
                //do search
                doSearch(query, 20, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: newText: " + newText);
                return false;
            }
        });

    }

    private void doSearch(String type, int count, int page) {
        GankService.getGankApi().search(type, count, page)
                .enqueue(new Callback<SearchResponse<SearchResult>>() {
                    @Override
                    public void onResponse(Response<SearchResponse<SearchResult>> response
                            , Retrofit retrofit) {
                        if (response.body() != null) {
                            SearchResponse<SearchResult> gank = response.body();
                            Log.d(TAG, gank.toString());

                            List<SearchResult> results = gank.getResults();
                            if (mSearchFragment != null) {
                                mSearchFragment.setSearchResult(results);
                            }
                            for (SearchResult result : results) {
                                Log.i(TAG, result.toString());
                                //set to recycler view
                            }

                        } else {
                            Log.d(TAG, "onResponse: response.body()==null");
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCameraInteraction(Uri uri) {

    }

//    @Override
//    public void onGalleryInteraction(Uri uri) {
//
//    }

    @Override
    public void onSearchItemClick(SearchResult item) {
        Log.d(TAG, "onSearchItemClick: item:" + item.getWho());

    }

    @Override
    public void onListFragmentInteraction(String item) {
        Log.d(TAG, "onListFragmentInteraction: " + item);

        Intent intent = new Intent(this, FullscreenActivity.class);
        intent.putExtra("url", item);
        startActivity(intent);


    }
}