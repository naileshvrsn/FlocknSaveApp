package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddElectricityActivity extends AppCompatActivity {

    private EditText icp;
    private Button addICP;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference icpList;

    private SweetAlertDialog pDialog;
    private String icpID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_electricity);
        setup();
    }

    private void setup(){

        icpID = getIntent().getStringExtra("idpID");

        icp = findViewById(R.id.add_electricity_icp);
        addICP = findViewById(R.id.add_icp_electricity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        icpList = db.collection("users").document(mAuth.getUid()).collection("electric_icp");

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);

        if(!icpID.equals("null")){
            displayICP();
            pDialog.setTitle("Updating ICP");
        }else{
            pDialog.setTitle("Adding ICP");
        }


        addICP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validICP = icp.getText().toString();

                if(validICP.isEmpty() || validICP.length() < 10){
                    icp.setError("Please enter valid ICP");
                }else{
                    icp.setError(null);
                    if(!icpID.equals("null")){
                        updateUserIDP();
                    }else {
                        addUserICP();
                    }
                }
            }
        });

    }

    private void addUserICP(){
        pDialog.show();
        String eICP = icp.getText().toString();

        Electricity electricityicp = new Electricity();
        electricityicp.setIcp(eICP);

        icpList.add(electricityicp).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(),"ICP Added successfully",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(),"ICP Added successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void displayICP(){

        icpList.document(icpID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Electricity electricity = task.getResult().toObject(Electricity.class);
                    icp.setText(electricity.getIcp());
                }
            }
        });
    }

    private void updateUserIDP(){
        pDialog.show();
        String eICP = icp.getText().toString();


        HashMap<String,Object> data = new HashMap<>();
        data.put("icp",eICP);

        icpList.document(icpID).update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"ICP Updated",Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }else{
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"ICP not Update",Toast.LENGTH_SHORT).show();
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
