package si.fri.emp.vaje2.projektnaemp;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListOfEventsFragment.OnFragmentInteractionListener, ListOfMyEventsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        ListOfEventsFragment listOfEventsFragment = new ListOfEventsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.FirstFragment, listOfEventsFragment, listOfEventsFragment.getTag()).commit();

        SharedPreferences prfs = getSharedPreferences("ACCOUNT_INFO", Context.MODE_PRIVATE);
        String personID = prfs.getString("Authentication_Id", "");
        String name =  prfs.getString("Authentication_Name", "");

        View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
        ((TextView) nav_header.findViewById(R.id.tvHeaderUsername)).setText("Pozdravljen, " + name);
        navigationView.addHeaderView(nav_header);


       /* Bundle bundle = getIntent().getExtras();
        if(bundle.getString("personID")!= null)
        {
            View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
            ((TextView) nav_header.findViewById(R.id.tvHeaderUsername)).setText("Pozdravljen, " + bundle.getString("name"));
            navigationView.addHeaderView(nav_header);
            personID = bundle.getString("personID");
        }*/


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            // Handle the camera action
            ListOfEventsFragment listOfEventsFragment = new ListOfEventsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FirstFragment, listOfEventsFragment, listOfEventsFragment.getTag()).commit();
            setTitle("Dogodki");

        } else if (id == R.id.nav_my_events) {
            ListOfMyEventsFragment listOfMyEventsFragment = new ListOfMyEventsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FirstFragment, listOfMyEventsFragment, listOfMyEventsFragment.getTag()).commit();
            setTitle("Moji dogodki");

        } else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            navigationView.setCheckedItem(id);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.FirstFragment, settingsFragment, settingsFragment.getTag()).commit();
            setTitle("Nastavitve");

        } else if (id == R.id.nav_logout) {
            String dir = getFilesDir().getAbsolutePath();
            File f0 = new File(dir, "user.txt");
            boolean d0 = f0.delete();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
