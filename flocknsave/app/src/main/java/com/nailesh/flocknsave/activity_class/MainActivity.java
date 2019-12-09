package com.nailesh.flocknsave.activity_class;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.fragment.HomeFragment;
import com.nailesh.flocknsave.fragment.HowItWorksFragment;
import com.nailesh.flocknsave.fragment.AboutUsFragment;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Person;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Person loginPerson;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private String firstName, lastName;
    private TextView user_name;
    private SweetAlertDialog pDialog;
    private Menu menu;
    private String persontype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading");

        pDialog.setCancelable(false);
        pDialog.show();

        setup(savedInstanceState);

        pDialog.dismissWithAnimation();

    }

    private void setup(Bundle savedInstanceState){

        user_name = findViewById(R.id.nav_user_name);
        // get logged in user
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            db = FirebaseFirestore.getInstance();
            docRef = db.collection("users").document(mAuth.getUid());

            // Get user object of logged in user from Firestore.
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.exists()){
                            loginPerson = documentSnapshot.toObject(Person.class);

                            //display username in nav drawer
                            firstName = loginPerson.getFirstName();
                            lastName = loginPerson.getLastName();
                            user_name.setText(firstName+" "+lastName);

                            // update Navigation View according to person type
                            persontype = loginPerson.getPersonType();

                            switch (persontype){
                                case "Buyer":
                                    menu.setGroupVisible(R.id.nav_customer,true);
                                    break;
                                case "Supplier":
                                    menu.setGroupVisible(R.id.nav_supplier,true);
                                    break;
                                case "Admin":
                                    menu.setGroupVisible(R.id.nav_admin,true);
                                    break;
                                default:
                                    menu.setGroupVisible(R.id.nav_customer,false);
                                    menu.setGroupVisible(R.id.nav_supplier,false);
                                    menu.setGroupVisible(R.id.nav_admin,false);
                            }

                        }else{
                            Toast.makeText(MainActivity.this,"Please Login", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {

                persontype = "User";
        }

        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.nav_user_name);


        // Setup Navigation Drawer
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =  new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu,menu);

        // change toolbar button if user is logged in
        if(mAuth.getCurrentUser() != null){
            menu.findItem(R.id.nav_menu_login).setVisible(false);
            menu.findItem(R.id.nav_menu_logout).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_menu_logout:
                mAuth.signOut();
                Intent logout = new Intent(this,LoginActivity.class);
                startActivity(logout);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Show Fragments for Nav drawer when item is clicked
        switch (menuItem.getItemId()){

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_how_it_works:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HowItWorksFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_how_it_works);
                break;

            case R.id.nav_scope:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_scope);
                break;

            case R.id.nav_search_products:
                Intent intent = new Intent(this,SelectCategoryActivity.class).putExtra("personType",persontype);
                startActivity(intent);
                this.finish();
                break;

            case R.id.nav_add_product:
                addProduct();
                break;

            case R.id.nav_admin_add_product:
                addProduct();
                break;

            case R.id.nav_update_product:
                updateProduct();
                break;

            case R.id.nav_admin_update_product:
                selectPerson(false);
                break;

            case R.id.nav_user_list:
                selectPerson(true);
                break;

            case R.id.nav_supplier_profile:
                updatePersonalInfo();
                break;

            case R.id.nav_customer_profile:
                updatePersonalInfo();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        // close drawer
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            // show home fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else{
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    private void addProduct(){
        Intent intent = new Intent(this,AddProductActivity.class)
                .putExtra("personType",persontype)
            .putExtra("updateProduct",false);
        startActivity(intent);
        this.finish();
    }

    private void updateProduct(){
        Intent intent = new Intent(this,SearchActivity.class)
                .putExtra("personType",persontype)
                .putExtra("supplierId",mAuth.getCurrentUser().getUid())
                .putExtra("updateProduct",true);
        startActivity(intent);
        this.finish();
    }

    //Admin has to select user to update user details
    private void selectPerson(boolean updatePerson){
        Intent intent = new Intent(this, PersonsListActivity.class)
                .putExtra("personType",persontype)
                .putExtra("updatePerson",updatePerson);
        startActivity(intent);
        this.finish();
    }

    //Non-Admin user and only update their info
    private void updatePersonalInfo(){
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class)
                .putExtra("personType",persontype)
                .putExtra("userId",mAuth.getUid())
                .putExtra("updatePerson",true);
        startActivity(intent);
    }

}
