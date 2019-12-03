package com.nailesh.flocknsave.activity_class;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;




import com.google.android.material.navigation.NavigationView;
import com.nailesh.flocknsave.fragment.HomeFragment;
import com.nailesh.flocknsave.fragment.HowItWorksFragment;
import com.nailesh.flocknsave.fragment.ScoopFragment;
import com.nailesh.flocknsave.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    NavigationView navigationView;

    Menu menu;



     String person ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setTitle("Flock n Save");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =  new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        switch (person){
            case "customer":
                menu.setGroupVisible(R.id.nav_customer,true);
                break;
            case "supplier":
                menu.setGroupVisible(R.id.nav_supplier,true);
                break;
            case "admin":
                menu.setGroupVisible(R.id.nav_admin,true);
                break;
            default:
                menu.setGroupVisible(R.id.nav_customer,false);
                menu.setGroupVisible(R.id.nav_supplier,false);
                menu.setGroupVisible(R.id.nav_admin,false);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_how_it_works:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HowItWorksFragment()).addToBackStack(null).commit();
                navigationView.setCheckedItem(R.id.nav_how_it_works);
                break;

            case R.id.nav_scope:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScoopFragment()).addToBackStack(null).commit();
                navigationView.setCheckedItem(R.id.nav_scope);
                break;

            case R.id.nav_search_products:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else{
            super.onBackPressed();
        }
    }
}
