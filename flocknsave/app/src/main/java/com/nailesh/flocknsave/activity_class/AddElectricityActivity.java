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
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Electricity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddElectricityActivity extends AppCompatActivity {

    private EditText icp;
    private Button addICP;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference docRef;
    private SweetAlertDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_electricity);
        setup();
    }

    private void setup(){

        icp = findViewById(R.id.add_electricity_icp);
        addICP = findViewById(R.id.add_icp_electricity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.setTitle("Adding ICP");


        addICP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validICP = icp.getText().toString();

                if(validICP.isEmpty() || validICP.length() < 10){
                    icp.setError("Please enter valid ICP");
                }else{
                    icp.setError(null);
                    addUserICP();
                }
            }
        });
    }

    private void addUserICP(){
        pDialog.show();
        String eICP = icp.getText().toString();

        docRef = db.collection("users").document(mAuth.getUid()).collection("electric_icp");

        Electricity electricityicp = new Electricity();
        electricityicp.setIcp(eICP);

        docRef.add(electricityicp).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
