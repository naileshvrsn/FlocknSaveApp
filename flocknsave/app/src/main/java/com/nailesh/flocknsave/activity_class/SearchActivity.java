package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.adapter.ProductAdapter;
import com.nailesh.flocknsave.model.Product;

public class SearchActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference productRef;
    private ProductAdapter adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setup();
    }

    private void setup() {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        productRef = db.collection("products");

        category = getIntent().getExtras().getString("category");

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = productRef.whereEqualTo("category", category);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Toast.makeText(getApplicationContext(),"Message0",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    int length = task.getResult().size();
                    if (length == 0) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SearchActivity.this);
                        dlgAlert.setMessage("No product for " + category + " category");
                        dlgAlert.setTitle("Error!");
                        dlgAlert.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        dlgAlert.setCancelable(false);
                        dlgAlert.create().show();
                    }
                }
            }
        });

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.product_Recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectCategoryActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);

        // change toolbar button if user is logged in
        if (mAuth.getCurrentUser() != null) {
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

}
