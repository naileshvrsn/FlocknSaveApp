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
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private String category,personType,productId,supplierId;
    private AlertDialog.Builder dlgAlert;
    private boolean updateProduct;


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

        category = getIntent().getStringExtra("category");
        personType = getIntent().getStringExtra("personType");
        Log.d("P____________",personType);

        if(personType.equals("Admin")){
            supplierId = getIntent().getStringExtra("supplierId");
        }else{
            supplierId = mAuth.getUid();
        }


        updateProduct = getIntent().getBooleanExtra("updateProduct", false);

        dlgAlert = new AlertDialog.Builder(SearchActivity.this);


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query;

        if(updateProduct){
            query = productRef.whereEqualTo("supplierId", supplierId);
            dlgAlert.setMessage("No product for this supplier");
        }else {
            query = productRef.whereEqualTo("category", category);
            dlgAlert.setMessage("No product for " + category + " category");
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //Toast.makeText(getApplicationContext(),"Message0",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    int length = task.getResult().size();
                    if (length == 0) {
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

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
              productId  = documentSnapshot.getId();

              if(updateProduct){
                  Intent updateproduct = new Intent(getApplicationContext(),AddProductActivity.class)
                          .putExtra("productId",productId)
                          .putExtra("updateProduct",updateProduct)
                          .putExtra("personType",personType)
                          .putExtra("supplierId",supplierId);
                  startActivity(updateproduct);
                  SearchActivity.this.finish();
              }else{
                  //Toast.makeText(getApplicationContext(),productId.toString(),Toast.LENGTH_SHORT).show();
                  Intent displayproduct = new Intent(getApplicationContext(), ProductDetailActivity.class)
                          .putExtra("productId",productId)
                          .putExtra("personType",personType)
                          .putExtra("category",category);
                  startActivity(displayproduct);
                  SearchActivity.this.finish();
              }
            }
        });
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
        Intent intent;
        if(updateProduct){
            if(personType.equals("Admin")){
                intent = new Intent(this, SupplierListActivity.class)
                        .putExtra("personType",personType);;
            }else{
                intent = new Intent(this, MainActivity.class);
            }

        }else{
            intent = new Intent(this, SelectCategoryActivity.class)
                    .putExtra("personType",personType);
        }

        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);

        // change toolbar button if user is logged in
        if (!personType.equals("User")) {
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
