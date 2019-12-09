package com.nailesh.flocknsave.activity_class;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.adapter.PersonAdapter;
import com.nailesh.flocknsave.adapter.ProductAdapter;
import com.nailesh.flocknsave.model.Person;

public class PersonsListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference personRef;
    private String personType,userid;
    private PersonAdapter adapter;
    private RecyclerView recyclerView;
    private boolean updatePerson;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        setup();
    }

    private void setup(){

        personType = getIntent().getStringExtra("personType");
        updatePerson = getIntent().getBooleanExtra("updatePerson",false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        personRef = db.collection("users");
        recyclerView = findViewById(R.id.supplier_recycler_View);

        setUpRecyclerView();

    }


    private void setUpRecyclerView(){
        if(updatePerson){
             query = personRef;
        }else {
             query = personRef.whereEqualTo("personType","Supplier");
        }


        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                .setQuery(query, Person.class)
                .build();

        adapter = new PersonAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {

                userid =  documentSnapshot.getId();
                if(updatePerson){
                    updateUserDetails(userid);
                }else {
                    updateProduct(userid);
                }
            }
        });
    }

    private void updateProduct(String userid){
        Intent intent = new Intent(this,SearchActivity.class)
                .putExtra("personType",personType)
                .putExtra("supplierId",userid)
                .putExtra("updateProduct",true);
        startActivity(intent);
        this.finish();
    }

    private void updateUserDetails(String userid){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class)
                .putExtra("personType",personType)
                .putExtra("userId",userid)
                .putExtra("updatePerson",updatePerson);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);

        // change toolbar button if user is logged in
        if (personType != null) {
            menu.findItem(R.id.nav_menu_login).setVisible(false);
            menu.findItem(R.id.nav_menu_logout).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.nav_menu_logout:
                mAuth.signOut();
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
