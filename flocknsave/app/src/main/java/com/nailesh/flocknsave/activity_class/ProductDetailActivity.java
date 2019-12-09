package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Product;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private String category,personType,productid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private ImageView productDetailImage;
    private TextView productDetailName, productDetailDescription, productDetailSaving;
    private ElegantNumberButton productCartQuanity;
    private Button addtocart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setup();
    }

    private void setup(){
        category = getIntent().getStringExtra("category");
        personType = getIntent().getStringExtra("personType");
        productid = getIntent().getStringExtra("productId");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("products").document(productid);

        productDetailImage = findViewById(R.id.product_detail_image);
        productDetailName = findViewById(R.id.product_detail_name);
        productDetailDescription = findViewById(R.id.product_detail_description);
        productDetailSaving = findViewById(R.id.product_detail_saving);
        productCartQuanity = findViewById(R.id.product_quantity_button);
        addtocart = findViewById(R.id.addtocart);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Product product = documentSnapshot.toObject(Product.class);

                Picasso.get().load(product.getImageLocation()).into(productDetailImage);
                productDetailName.setText(product.getName());
                productDetailDescription.setText(product.getDescription());
                productDetailSaving.setText("Saving: "+product.getSavings()+"%");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Product Unavailable",Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!personType.equals("Buyer")){
                    Toast.makeText(getApplicationContext(),"Customer account need to shop",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Product Added to cart",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),SearchActivity.class)
                .putExtra("category", category)
                .putExtra("personType",personType);
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
