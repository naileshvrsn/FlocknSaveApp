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

import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddProductActivity extends AppCompatActivity {


    private TextView name,description,unit,saving;
    private Spinner region,industry,supplier;
    private Button addProduct;


    private ImageView addImage;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference ref;

    private Person pSupplier;

    private String persontype, storageLocation, imageURl;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private SweetAlertDialog pDialog;

    public AddProductActivity() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setup();
    }

    private void setup(){

        mAuth =FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        docRef = db.collection("users").document(mAuth.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        persontype = documentSnapshot.getString("personType");
                        //if the user is admin the display supplier list when addin the product;
                        if(persontype.equals("Admin")){
                            final ArrayList<String> supplierList = new ArrayList<String>();
                            supplierList.add("Select Supplier");

                            db.collection("users")
                                    .whereEqualTo("personType","Supplier")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            pSupplier = document.toObject(Person.class);
                                            supplierList.add(pSupplier.getFirstName());
                                        }

                                        ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                                android.R.layout.simple_spinner_item,supplierList);
                                        supplierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        supplier.setAdapter(supplierAdapter);
                                    }
                                }
                            });
                            supplier.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        name = findViewById(R.id.add_product_name);
        description = findViewById(R.id.add_product_description);
        unit = findViewById(R.id.add_product_unit);
        saving = findViewById(R.id.add_product_saving);

        addImage = findViewById(R.id.upload_product_image);

        region = findViewById(R.id.add_product_region);
        ArrayAdapter<CharSequence> regionadapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.cities_register, android.R.layout.simple_spinner_item);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(regionadapter);

        industry = findViewById(R.id.add_product_industry);
        ArrayAdapter<CharSequence> industryadapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.industry, android.R.layout.simple_spinner_item);
        industryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        industry.setAdapter(industryadapter);

        supplier = findViewById(R.id.add_product_supplier);

        addProduct = findViewById(R.id.add_product_button);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }else {
                    // Product can't be uploaded without image
                    checkImage();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Adding Product");
        pDialog.setCancelable(false);


    };

    private boolean validate(){
        boolean valid = true;

        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pUnit = unit.getText().toString();
        String pSaving = saving.getText().toString();
        String pRegion = region.getSelectedItem().toString();
        String pIndustry = industry.getSelectedItem().toString();


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
            if(p_saving < 0 || p_saving > 100){
                saving.setError("Must be between 0 and 100");
                valid = false;
            }else {
                saving.setError(null);
            }
        }
        if(pRegion.equals("Select Region")){
            Toast.makeText(getApplicationContext(), "Select Region", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if(pIndustry.equals("Select Industry")){
            Toast.makeText(getApplicationContext(), "Select Industry", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if(supplier.getVisibility() == View.VISIBLE){
            String pSupplier = supplier.getSelectedItem().toString();
            if(pSupplier.equals("Select Supplier")){
                    Toast.makeText(getApplicationContext(), "Select Supplier", Toast.LENGTH_LONG).show();
                    valid = false;
            }
        }

        return valid;
    }

    private void checkImage(){
        // Product can't be uploaded without image
        if(filePath != null){
            findsupplierID();
        }else{
            Toast.makeText(AddProductActivity.this, "Select an Image", Toast.LENGTH_LONG).show();
        }
    }

    private void findsupplierID(){
        pDialog.show();

        String pSupplier;

        if(persontype.equals("Admin")){
            //if admin is logged in the get supplier id from database
            getSupplierId(supplier.getSelectedItem().toString());

        }else{
            //the logged in user is supplier
            // upload the product with user id
            pSupplier = mAuth.getUid();
            uploadProduct(pSupplier);
        }

    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                addImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    // get supplier id if the logged in user is an Admin
    private void getSupplierId(String supplierName){
        final Person[] person = new Person[1];

        db.collection("users")
                .whereEqualTo("firstName",supplierName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //start upload once the supplier id if found;
                        uploadProduct(document.getId());
                        }

                }else {
                    Log.d("Error getting documents: ", task.getException().toString());
                }
            }
        });
    }

    private void uploadProduct(String supplierID){
        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pUnit = unit.getText().toString();
        String pSaving = saving.getText().toString();
        String pRegion = region.getSelectedItem().toString();
        String pIndustry = industry.getSelectedItem().toString();

        // Create a new product object
        Product product = new Product(pName,pDescription,pUnit,pSaving,pRegion,pIndustry,supplierID,"");

        //upload the product to database
        db.collection("products")
                .add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            //upload product image on database
                            uploadImage(task.getResult().getId());
                        }else {
                            Toast.makeText(AddProductActivity.this, "Product not uploaded ",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddProductActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadImage(final String productId){
        if(filePath != null){
            ref = storageReference.child("productImages/"+productId );
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
                            Toast.makeText(AddProductActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getImageURl(final String productID){
        //Log.d("URI___________",productID);
        storageLocation = "gs://flocknsave-62b20.appspot.com/productImages/";
        StorageReference storageReference = storage.getReferenceFromUrl(storageLocation+productID);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imageURl = String.valueOf(uri);

                //Log.d("URI___________",imageURl);

                db.collection("products").document(productID).update("imageLocation",imageURl)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pDialog.dismissWithAnimation();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AddProductActivity.this);
                                dlgAlert.setMessage("Product Added Successfully");
                                dlgAlert.setTitle("SUCCESS");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //User Directed to main page once product uploaded successfully
                                        gotoMain();
                                    }
                                });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("URI", "Error");
            }
        });
    }

    private void gotoMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}


