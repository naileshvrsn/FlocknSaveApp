package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Electricity;
import com.nailesh.flocknsave.model.Location;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.net.maroulis.library.EasySplashScreen;

public class AddLocationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference locationRef;

    private EditText streetAddress, address1, address2, suburb, city, postCode;
    private Spinner region;
    private Button addLocation;
    private String locationID;


    private SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setup();
    }

    private void setup(){

        locationID = getIntent().getStringExtra("locationID");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        locationRef = db.collection("users").document(mAuth.getUid()).collection("locations");


        streetAddress = findViewById(R.id.location_street_number);
        address1 = findViewById(R.id.location_address1);
        address2 = findViewById(R.id.location_address2);
        suburb = findViewById(R.id.location_suburb);
        city = findViewById(R.id.location_city);
        postCode = findViewById(R.id.location_post_code);

        region = findViewById(R.id.location_region_spinner);
        ArrayAdapter<CharSequence> regionadapter = ArrayAdapter.createFromResource(this,
                R.array.cities_register, android.R.layout.simple_spinner_item);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(regionadapter);

        addLocation = findViewById(R.id.location_add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }else{
                    if(!locationID.equals("null")){
                        updateDeliveryLocation();
                    }else{
                        addDeliveryLocation();
                    }
                }
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);

        if(!locationID.equals("null")){
            displayLocation();
            pDialog.setTitle("Updating Location");
        }else{
            pDialog.setTitle("Adding Location");
        }
    }

    private boolean validate(){
        boolean valid = true;

        String streetaddressText = streetAddress.getText().toString();
        String address1Text = address1.getText().toString();
        String suburbText = suburb.getText().toString();
        String cityText = city.getText().toString();
        String postCodeText = postCode.getText().toString();
        String regiontext = region.getSelectedItem().toString();

        if (streetaddressText.isEmpty() || streetaddressText.length() < 2) {
            streetAddress.setError("at least 2 characters");
            valid = false;
        } else {
            streetAddress.setError(null);
        }

        if (address1Text.isEmpty() || address1Text.length() < 5) {
            address1.setError("at least 5 characters");
            valid = false;
        } else {
            address1.setError(null);
        }

        if (suburbText.isEmpty() || suburbText.length() < 2) {
            suburb.setError("at least 2 characters");
            valid = false;
        } else {
            suburb.setError(null);
        }
        if (cityText.isEmpty() || cityText.length() < 2) {
            city.setError("at least 2 characters");
            valid = false;
        } else {
            suburb.setError(null);
        }

        if (postCodeText.length() != 4) {
            postCode.setError("Enter a valid postcode");
            valid = false;
        } else {
            postCode.setError(null);
        }

        if (regiontext.equals("Select Region")) {
            Toast.makeText(AddLocationActivity.this, "Select Region", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    private void addDeliveryLocation()
    {
        pDialog.show();
        String streetaddressText = streetAddress.getText().toString();
        String address1Text = address1.getText().toString();
        String address2Text = address2.getText().toString();
        String suburbText = suburb.getText().toString();
        String cityText = city.getText().toString();
        String postCodeText = postCode.getText().toString();
        String regiontext = region.getSelectedItem().toString();


        Location location = new Location();

        location.setStreetNumber(streetaddressText);
        location.setAddressLine1(address1Text);
        location.setSuburb(suburbText);
        location.setCity(cityText);
        location.setPostCode(postCodeText);
        location.setRegion(regiontext);

        if(!address2Text.isEmpty()){
            location.setAddressLine2(address2Text);
        }


        locationRef.add(location).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(),"Delivery Location Added",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(),"Location could not be added",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void displayLocation(){

        locationRef
                .document(locationID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Location location = task.getResult().toObject(Location.class);
                            if(location != null){

                                streetAddress.setText(location.getStreetNumber());
                                address1.setText(location.getAddressLine1());
                                if(location.getAddressLine2() != null){
                                    address2.setText(location.getAddressLine2());
                                }
                                suburb.setText(location.getSuburb());
                                city.setText(location.getCity());
                                postCode.setText(location.getPostCode());
                                region.setSelection(((ArrayAdapter<String>) region.getAdapter()).getPosition(location.getRegion()));
                            }else {
                                Toast.makeText(getApplicationContext(),"No location found",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void updateDeliveryLocation(){
        pDialog.show();

        String streetaddressText = streetAddress.getText().toString();
        String address1Text = address1.getText().toString();
        String address2Text = address2.getText().toString();
        String suburbText = suburb.getText().toString();
        String cityText = city.getText().toString();
        String postCodeText = postCode.getText().toString();
        String regiontext = region.getSelectedItem().toString();

        HashMap<String,Object> data = new HashMap<>();
        if(!address2Text.isEmpty()){
            data.put("addressLine2",address2Text);
        }else {
            data.put("addressLine2",null);
        }
        data.put("streetNumber",streetaddressText);
        data.put("addressLine1",address1Text);
        data.put("suburb",suburbText);
        data.put("city",cityText);
        data.put("postCode",postCodeText);
        data.put("region",regiontext);



        locationRef
                .document(locationID)
                .update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Location Updated",Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else{
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Location not Update",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
