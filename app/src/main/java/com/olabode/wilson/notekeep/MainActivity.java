package com.olabode.wilson.notekeep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.olabode.wilson.notekeep.viewmodels.NoteViewModel;
import com.olabode.wilson.notekeep.views.FavouritesFragment;
import com.olabode.wilson.notekeep.views.NotesFragment;
import com.olabode.wilson.notekeep.views.SettingsFragment;
import com.olabode.wilson.notekeep.views.TrashFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Handler mHandler;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private FragmentTransaction ft;

    private NoteViewModel noteViewModel;

    private TextView note, favourite, trash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        note = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_notes));
        favourite = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_favourites));
        trash = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_trash));

        initializeCountDrawer();




        if (savedInstanceState == null) {
            displaySelectedScreen(R.id.nav_notes);
            setFirstItemChecked();
        }
//        enableStrictMode();
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
        getMenuInflater().inflate(R.menu.menu_favourite_frag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
     * update the menu_favourite_frag screen as new fragment.
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                fragment = new FavouritesFragment();
                break;

            case R.id.nav_trash:
                fragment = new TrashFragment();
                break;

            case R.id.nav_settings:
                fragment = new SettingsFragment();
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


    public void enableStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build();

            StrictMode.setThreadPolicy(policy);

        }

    }


    private void initializeCountDrawer() {
        noteViewModel.getNoteCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                note.setGravity(Gravity.CENTER_VERTICAL);
                //note.setTypeface(null, Typeface.BOLD);
                note.setTextColor(getResources().getColor(R.color.colorAccent));
                if (integer > 99) {
                    note.setText("99+");
                } else {
                    note.setText(String.valueOf(integer));
                }
            }
        });
        noteViewModel.getFavouriteCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                favourite.setGravity(Gravity.CENTER_VERTICAL);
                //note.setTypeface(null, Typeface.BOLD);
                favourite.setTextColor(getResources().getColor(R.color.colorAccent));
                if (integer > 99) {
                    favourite.setText("99+");
                } else {
                    favourite.setText(String.valueOf(integer));
                }
            }
        });


        noteViewModel.getTrashCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                trash.setGravity(Gravity.CENTER_VERTICAL);
                //note.setTypeface(null, Typeface.BOLD);
                trash.setTextColor(getResources().getColor(R.color.colorAccent));
                if (integer > 99) {
                    trash.setText("99+");
                } else {
                    trash.setText(String.valueOf(integer));
                }
            }
        });

    }

}
