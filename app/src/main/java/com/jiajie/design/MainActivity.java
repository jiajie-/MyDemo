package com.jiajie.design;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements CameraFragment.OnFragmentInteractionListener,
        GalleryFragment.OnFragmentInteractionListener {

    private static final String TAG = "CodeLabActivity";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    FloatingActionButton fab;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NavigationView navigationView;

    static final String cameraTag = "CAMERA";
    static final String galleryTag = "GALLERY";

    //fragments
    CameraFragment mCameraFragment;
    GalleryFragment mGalleryFragment;
    FragmentManager mFragmentManager;

    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

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
                Snackbar.make(rootLayout, "Hello.I am Snackbar!", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "onClick: Snackbar");
                            }
                        }).show();
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
        collapsingToolbarLayout.setTitle("Hello World!");

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
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                // 将该 Fragment 添加到“fragment_container”FrameLayout 中
                transaction.add(R.id.fragment_container, mCameraFragment, cameraTag).commit();
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

    /** Swaps fragments in the menu_main content view */
    private void switchContent(int id) {
        Fragment toFragment = null;
        String title = "";
        String tag = "";
        // Highlight the selected item, update the title, and close the drawer
        navigationView.setCheckedItem(id);
        drawerLayout.closeDrawer(navigationView);

        switch (id) {
            case R.id.nav_camera:
                Log.d(TAG, "onNavigationItemSelected: nav_camera");
                title = "Camera";
                if (mCameraFragment == null) {
                    mCameraFragment = CameraFragment.newInstance("1", "2");
                }
                tag = cameraTag;
                toFragment = mCameraFragment;
                break;

            case R.id.nav_gallery:
                Log.d(TAG, "onNavigationItemSelected: nav_gallery");
                title = "Gallery";
                if (mGalleryFragment == null) {
                    mGalleryFragment = GalleryFragment.newInstance("3", "4");
                }
                tag = galleryTag;
                toFragment = mGalleryFragment;
                break;

            case R.id.nav_slideshow:
                Log.d(TAG, "onNavigationItemSelected: nav_slideshow");
                break;

            case R.id.nav_tools:
                Log.d(TAG, "onNavigationItemSelected: nav_tools");
                break;

            case R.id.nav_exit:
                Log.d(TAG, "onNavigationItemSelected: nav_exit");
                //TODO logout account and return login

                break;

            default:
                break;
        }

        if (toFragment == null || mCurrentFragment == toFragment) {
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

        collapsingToolbarLayout.setTitle(title);
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
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}