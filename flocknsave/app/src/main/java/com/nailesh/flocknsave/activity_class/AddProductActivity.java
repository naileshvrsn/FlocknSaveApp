package com.nailesh.flocknsave.activity_class;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Person;
import com.nailesh.flocknsave.model.Product;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductActivity extends AppCompatActivity {

    private TextView name, description, unit, saving;
    private Spinner region, supplier, category;
    private Button addProduct;
    private ImageView addImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference ref;
    private Person pSupplier;
    private String persontype,storageLocation,imageURl,productId,supplierId;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private SweetAlertDialog pDialog;
    private boolean updateProduct;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setup();
    }

    private void setup() {

        persontype = getIntent().getStringExtra("personType");
        productId = getIntent().getStringExtra("productId");
        updateProduct = getIntent().getBooleanExtra("updateProduct",false);
        supplierId = getIntent().getStringExtra("supplierId");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageLocation = "gs://flocknsave-62b20.appspot.com/productImages/";

        name = findViewById(R.id.add_product_name);
        description = findViewById(R.id.add_product_description);
        unit = findViewById(R.id.add_product_unit);
        saving = findViewById(R.id.add_product_saving);
        addImage = findViewById(R.id.upload_product_image);
        addProduct = findViewById(R.id.add_product_button);
        supplier = findViewById(R.id.add_product_supplier);

        //Select image from device
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        region = findViewById(R.id.add_product_region);
        ArrayAdapter<CharSequence> regionadapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.cities_register, android.R.layout.simple_spinner_item);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(regionadapter);

        category = findViewById(R.id.add_product_category);
        ArrayAdapter<CharSequence> categoryadapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.category, android.R.layout.simple_spinner_item);
        categoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryadapter);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);

        // display product information if updating product
        if(updateProduct){

            pDialog.setTitle("Retrieving Product");
            pDialog.show();

            addProduct.setText("Update Product");
            db.collection("products")
                    .document(productId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                Product product = task.getResult().toObject(Product.class);
                                displayProduct(product);
                            }
                        }
                    });

        }
        else {
            pDialog.setTitleText("Adding Product");
        }

        // Show supplier list if the logged in user is an Admin
        if (persontype.equals("Admin")) {
            final ArrayList<String> supplierList = new ArrayList<String>();
            supplierList.add("Select Supplier");

            db.collection("users")
                    .whereEqualTo("personType", "Supplier")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            pSupplier = document.toObject(Person.class);
                            supplierList.add(pSupplier.getFirstName());
                        }

                        ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, supplierList);
                        supplierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        supplier.setAdapter(supplierAdapter);
                    }
                }
            });
            supplier.setVisibility(View.VISIBLE);
        }

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                } else {
                    // No fields are empty
                    if(updateProduct){
                        //update existing product

                        startUpdateDBProduct(productId);
                    } else {
                        // new product can't be add without an image;
                        pDialog.setTitle("Adding Product");
                        checkImage();
                    }
                }
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                addImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private boolean validate() {
        boolean valid = true;

        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pUnit = unit.getText().toString();
        String pSaving = saving.getText().toString();
        String pRegion = region.getSelectedItem().toString();
        String pCategory = category.getSelectedItem().toString();

        if (pName.isEmpty() || pName.length() < 2) {
            name.setError("at least 2 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (pDescription.isEmpty() || pDescription.length() < 5) {
            description.setError("at least 5 characters");
            valid = false;
        } else {
            description.setError(null);
        }

        if (pUnit.isEmpty() || pUnit.length() < 2) {
            unit.setError("at least 2 characters");
            valid = false;
        } else {
            unit.setError(null);
        }

        if (pSaving.isEmpty()) {
            saving.setError("Must be between 0 and 100");
            valid = false;
        } else {
            int p_saving = Integer.parseInt(pSaving);
            if (p_saving < 0 || p_saving > 100) {
                saving.setError("Must be between 0 and 100");
                valid = false;
            } else {
                saving.setError(null);
            }
        }
        if (pRegion.equals("Select Region")) {
            Toast.makeText(getApplicationContext(), "Select Region", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (pCategory.equals("Select Category")) {
            Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (supplier.getVisibility() == View.VISIBLE) {
            String pSupplier = supplier.getSelectedItem().toString();
            if (pSupplier.equals("Select Supplier")) {
                Toast.makeText(getApplicationContext(), "Select Supplier", Toast.LENGTH_LONG).show();
                valid = false;
            }
        }

        return valid;
    }

    //Add product Methods
    private void checkImage() {
        // Product can't be uploaded without image
        if (filePath != null) {
            // find supplier id
            findsupplierID();
        } else {
            Toast.makeText(AddProductActivity.this, "Select an Image", Toast.LENGTH_LONG).show();
        }
    }

    private void findsupplierID() {
        pDialog.show();

        String pSupplier;

        if (persontype.equals("Admin")) {
            //if admin is logged in the get supplier id from database
            getSupplierId(supplier.getSelectedItem().toString());

        } else {
            //the logged in user is supplier
            // upload the product with user id
            pSupplier = mAuth.getUid();
            uploadProduct(pSupplier);
        }

    }

    // get supplier id if the logged in user is an Admin
    private void getSupplierId(String supplierName) {

        db.collection("users")
                .whereEqualTo("firstName", supplierName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //start upload once the supplier id if found;
                        uploadProduct(document.getId());
                    }
                } else {
                    Log.d("Error getting documents: ", task.getException().toString());
                }
            }
        });
    }

    private void uploadProduct(String supplierID) {
        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pUnit = unit.getText().toString();
        String pSaving = saving.getText().toString();
        String pRegion = region.getSelectedItem().toString();
        String pCategory = category.getSelectedItem().toString();

        // Create a new product object
        Product product = new Product(pName, pDescription, pUnit, pSaving, pRegion, pCategory, supplierID, "");

        //upload the product to database
        db.collection("products")
                .add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            //upload product image on database
                            uploadImage(task.getResult().getId());
                        } else {
                            Toast.makeText(AddProductActivity.this, "Product not uploaded ", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadImage(final String productId) {
        if (filePath != null) {
            ref = storageReference.child("productImages/" + productId);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //get URL for image to link with product in database
                            getImageURl(productId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProductActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


   //update Product only methods
    private void displayProduct(Product product){


        name.setText(product.getName());
        description.setText(product.getDescription());
        unit.setText(product.getUnit());
        saving.setText(product.getSavings());

        Picasso.get().load(product.getImageLocation()).into(addImage);

        region.setSelection(((ArrayAdapter<String>)region.getAdapter()).getPosition(product.getRegion()));

        category.setSelection(((ArrayAdapter<String>)category.getAdapter()).getPosition(product.getcategory()));

        if(persontype.equals("Admin")){
            db.collection("users")
                    .document(product.getSupplierId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                               Person pSupplier1 = task.getResult().toObject(Person.class);

                               Log.d("Supplier name",pSupplier1.getFirstName());

                                supplier.setSelection(((ArrayAdapter<String>)supplier.getAdapter()).getPosition(pSupplier1.getFirstName()));
                            }
                        }
                    });
        }

        pDialog.dismiss();
    }

    private void startUpdateDBProduct(final String productId){
        pDialog.setTitle("Updating Product");
        pDialog.show();
        if(persontype.equals("Admin")){
            db.collection("users")
                    .whereEqualTo("firstName",supplier.getSelectedItem().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult()){
                                    //update
                                   updateDBProduct(documentSnapshot.getId(),productId);
                                }

                            }
                        }
                    });
        } else {
            //update the product with the current logged in user
            updateDBProduct(mAuth.getUid(),productId);
        }
    }

    private void updateDBProduct(String supplierID, final String productId){

        String pUpdateName = name.getText().toString();
        String pUpdateDescription = description.getText().toString();
        String pUpdateUnit = unit.getText().toString();
        String pUpdateSaving = saving.getText().toString();
        String pUpdateRegion = region.getSelectedItem().toString();
        String pUpdateCategory = category.getSelectedItem().toString();

        // Hashmap for All product details
        HashMap<String,Object> updateData= new HashMap<>();
        updateData.put("name",pUpdateName);
        updateData.put("description",pUpdateDescription);
        updateData.put("unit",pUpdateUnit);
        updateData.put("savings",pUpdateSaving);
        updateData.put("region",pUpdateRegion);
        updateData.put("category",pUpdateCategory);
        updateData.put("supplierId",supplierID);

        //update the product
        db.collection("products")
                .document(productId)
                .update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            updateImage(productId);
                        }
                    }
                });
    }

    private void updateImage(final String productId){
        if(filePath != null){
            // delete previous image
            ref = storageReference.child("productImages/"+productId );

            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getImageURl(productId);
                        }
                    });
                }
            });

        }else {
            pDialog.dismissWithAnimation();
            showMessage();
        }
    }

    // Used for both add and update product
    private void getImageURl(final String productID) {
        //Log.d("URI___________",productID);

        storageReference = storage.getReferenceFromUrl(storageLocation + productID);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imageURl = String.valueOf(uri);

                //Log.d("URI___________",imageURl);

                db.collection("products").document(productID).update("imageLocation", imageURl)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismissWithAnimation();
                                showMessage();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("URI--------------", "Error");
            }
        });
    }

    // Show Alert dialog on complete
    private void showMessage(){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AddProductActivity.this);
        if(updateProduct){
            dlgAlert.setMessage("Product updated successfully");
        }else{
            dlgAlert.setMessage("Product Added successfully");
        }

        dlgAlert.setTitle("SUCCESS");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goBack();
            }
        });
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();
    }

    // Go to previous Activity
    private void goBack(){
        Intent intent;
        if(updateProduct){
            // go to product List
            intent = new Intent(getApplicationContext(), SearchActivity.class)
                    .putExtra("personType",persontype)
                    .putExtra("supplierId",supplierId)
                    .putExtra("updateProduct",true);
        }else {
            // Go to home page
            intent = new Intent(getApplicationContext(), MainActivity.class);
        }

        startActivity(intent);
        this.finish();
    }
}


