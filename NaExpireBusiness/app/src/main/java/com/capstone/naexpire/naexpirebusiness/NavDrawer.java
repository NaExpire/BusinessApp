package com.capstone.naexpire.naexpirebusiness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NavDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //test data
    String ownerName = "Seb";
    String restName = "Chicken on a Stick";

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        //Intent intent = getIntent();
        //allInfo = intent.getStringExtra("data");
        //String[] ar = allInfo.split(",");

        //Set the fragment initially
        MenuFragment menuFragment = new MenuFragment();
        FragmentManager manager = getSupportFragmentManager();

        //send string to fragment
        //Bundle bundle = new Bundle();
        //bundle.putString("restrauntData", allInfo);
        //menuFragment.setArguments(bundle);

        manager.beginTransaction().replace(R.id.fragment_container, menuFragment).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView owner = (TextView) header.findViewById(R.id.lblNavOwner);
        TextView rest = (TextView) header.findViewById(R.id.lblNavRest);

        owner.setText(ownerName);
        rest.setText(restName);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_storeMenu) {
            MenuFragment menuFragment = new MenuFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, menuFragment).commit();
        } else if (id == R.id.nav_editMenu) {
            EditMenuFragment editMenuFragment = new EditMenuFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, editMenuFragment).commit();
        } else if (id == R.id.nav_orderInbox) {
            OrderInboxFragment orderInboxFragment = new OrderInboxFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, orderInboxFragment).commit();
        } else if (id == R.id.nav_orderHistory) {
            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, orderHistoryFragment).commit();
        } else if (id == R.id.nav_activeDiscounts) {
            ActiveDiscountsFragment activeDiscountsFragment = new ActiveDiscountsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, activeDiscountsFragment).commit();
        } else if (id == R.id.nav_accountInfo) {
            AccountInfoFragment accountInfoFragment = new AccountInfoFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, accountInfoFragment).commit();
        } else if (id == R.id.nav_logout){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
