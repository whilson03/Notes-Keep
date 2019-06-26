package com.olabode.wilson.notekeep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.olabode.wilson.notekeep.views.Favourites;
import com.olabode.wilson.notekeep.views.NotesFragment;
import com.olabode.wilson.notekeep.views.TrashFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            displaySelectedScreen(R.id.nav_notes);
            setFirstItemChecked();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all_notes) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * update the main screen as new fragment.
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        // using handler to make drawer closing transition smoother.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                displaySelectedScreen(id);
            }
        }, 305);


        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * get the current item clicked in the navigation drawer and switch to the
     * corresponding fragment.
     *
     * @param id id for the items in the navigation view.
     */
    public void displaySelectedScreen(int id) {
        Fragment fragment = null;
        switch (id) {

            case R.id.nav_notes:
                fragment = new NotesFragment();

                break;

            case R.id.nav_favourites:
                fragment = new Favourites();
                break;

            case R.id.nav_trash:
                fragment = new TrashFragment();
                break;

            case R.id.nav_settings:

                break;


            case R.id.nav_feedback:
                sendFeedback();
                break;

            case R.id.nav_share:
                break;
        }

        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content_layout, fragment);
            ft.commit();
        }

    }

    /**
     * send feedback email
     */
    public void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "whilson03@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "feedback from NotesKeep app");
        intent.putExtra(Intent.EXTRA_TEXT, "message");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    /**
     * check the first item in navigation drawer.
     */
    public void setFirstItemChecked() {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_notes).setChecked(true);
    }
}
