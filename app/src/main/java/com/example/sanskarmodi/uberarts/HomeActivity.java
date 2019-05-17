package com.example.sanskarmodi.uberarts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sanskarmodi.uberarts.adapter.DashboardAdapter;
import com.example.sanskarmodi.uberarts.adapter.OrderDetailsAdapter;
import com.example.sanskarmodi.uberarts.fragments.DashboardFragment;
import com.example.sanskarmodi.uberarts.fragments.DashboardItemViewFragment;
import com.example.sanskarmodi.uberarts.fragments.ProfileFragment;
import com.example.sanskarmodi.uberarts.model.DashboardItem;
import com.example.sanskarmodi.uberarts.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;
import io.paperdb.PaperDbException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DashboardFragment.Listener, DashboardAdapter.ItemListener, DashboardItemViewFragment.Bookinglistener, OrderDetailsAdapter.OrderDetailsListener {

    private static final String TAG = "HomeActivity";
    private static final int CHOOSE_IMAGE = 101;
    static String profileImageUrl;
    static Uri uriProfileImage;

    static FirebaseAuth mAuth;
    public static List<DashboardItem> dashboardItemList;

    ActionBar actionBar;
    DrawerLayout drawer;
    ImageButton userImageBtn;
    TextView userNameText, userEmailText;
    ProgressBar progressBar;
    LinearLayout navHeader;

    FragmentManager manager;
    FragmentTransaction transaction;
    Book orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Paper.init(this);
        Paper.book("orders");

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container_fragment,new DashboardFragment()).commit();

        navHeader = (LinearLayout) navigationView.getHeaderView(0);
        Log.i(TAG, "onCreate: " + navHeader);
        progressBar = navHeader.findViewById(R.id.imgProgressBar);
        userImageBtn = navHeader.findViewById(R.id.userImageBtn);
        userNameText = navHeader.findViewById(R.id.usernameTextView);
        userEmailText = navHeader.findViewById(R.id.userEmailTextView);

        loadUserProfile();
        populateDashItemData();
    }

    private void populateDashItemData() {
        dashboardItemList = new ArrayList<>();
        dashboardItemList.add(new DashboardItem("Art Class",getResources().getString(R.string.artclassesdesc),R.drawable.artclass));
        dashboardItemList.add(new DashboardItem("Art Gallery",getResources().getString(R.string.artgallerydesc),R.drawable.artgal));
        dashboardItemList.add(new DashboardItem("Art Workshop",getResources().getString(R.string.artworkshopdesc),R.drawable.workshop));
        dashboardItemList.add(new DashboardItem("Calligraphy Classes",getResources().getString(R.string.calligraphyclassesdesc),R.drawable.calligraphy));
        dashboardItemList.add(new DashboardItem("Custom Art Programmes",getResources().getString(R.string.customartprogramdesc),R.drawable.customart));
        dashboardItemList.add(new DashboardItem("Kids Art Classes",getResources().getString(R.string.kidsartclassdesc),R.drawable.kids));
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
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String currentTag = null;

        transaction = manager.beginTransaction();
        switch (id) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.nav_dashboard:
                currentTag = "Dashboard";
                transaction.replace(R.id.container_fragment, new DashboardFragment(), currentTag);
                break;
            case R.id.nav_profile:
                currentTag = "Profile";
                transaction.replace(R.id.container_fragment, new ProfileFragment(), currentTag);
                break;
            case R.id.order:
                currentTag = "Order";
                transaction.replace(R.id.container_fragment, new OrdersFragment(), currentTag);
                break;
        }
        transaction.addToBackStack("added").commit();
        if(manager.getBackStackEntryCount() > 0)
            actionBar.setTitle(currentTag);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadUserProfile() {
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Log.i(TAG, "loadUserProfile: " + user.getPhotoUrl());
                Glide.with(this).load(user.getPhotoUrl()).into(userImageBtn);
                progressBar.setVisibility(View.GONE);
            }
            if (user.getDisplayName() != null) {
                userNameText.setText(user.getDisplayName());
            }
            if (user.getEmail() != null) {
                userEmailText.setText(user.getEmail());
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDashboardInteraction(View v) {
    }

    @Override
    public void onDashboardItemClick(View view, String title,int index) {
        Log.i(TAG, "onDashboardItemClick: " + title);
        transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("title", dashboardItemList.get(index).getTitle());
        bundle.putString("desc",dashboardItemList.get(index).getDesc());
        DashboardItemViewFragment dashboardItemViewFragment = new DashboardItemViewFragment();
        dashboardItemViewFragment.setArguments(bundle);
        transaction.replace(R.id.container_fragment, dashboardItemViewFragment).commit();
    }

    @Override
    public void onDescItemClick() {

    }

    @Override
    public void onItemChecked(Order order, int index) {
        //Toast.makeText(this,index + "",Toast.LENGTH_LONG).show();

    }
}
