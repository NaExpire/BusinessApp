package com.capstone.naexpire.naexpirebusiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPref;

    private TextView owner, rest;

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        //Set the fragment initially
        FragmentMenu fragmentMenu = new FragmentMenu();
        FragmentManager manager = getSupportFragmentManager();

        sharedPref = getSharedPreferences("com.capstone.naexpire.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);

        manager.beginTransaction().replace(R.id.fragment_container, fragmentMenu).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        updateDetails();
    }

    public void updateDetails(){
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        owner = (TextView) header.findViewById(R.id.lblNavOwner);
        rest = (TextView) header.findViewById(R.id.lblNavRest);

        String fullName = sharedPref.getString("firstName", "Shmeggle") +
                " "+ sharedPref.getString("lastName", "TeaBoot");
        owner.setText(fullName);
        rest.setText(sharedPref.getString("restaurantName", ""));
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

    //search
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_storeMenu) {
            FragmentMenu fragmentMenu = new FragmentMenu();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentMenu).commit();
        } else if (id == R.id.nav_editMenu) {
            FragmentEditMenu fragmentEditMenu = new FragmentEditMenu();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentEditMenu).commit();
        } else if (id == R.id.nav_orderInbox) {
            FragmentOrderInbox fragmentOrderInbox = new FragmentOrderInbox();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentOrderInbox).commit();
        } else if (id == R.id.nav_orderHistory) {
            FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentOrderHistory).commit();
        } else if (id == R.id.nav_activeDiscounts) {
            FragmentActiveDiscounts fragmentActiveDiscounts = new FragmentActiveDiscounts();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentActiveDiscounts).commit();
        } else if (id == R.id.nav_accountInfo) {
            FragmentAccountInfo fragmentAccountInfo = new FragmentAccountInfo();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragmentAccountInfo).commit();
        } else if (id == R.id.nav_logout){
            Intent intent = new Intent(this, ActivityLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
