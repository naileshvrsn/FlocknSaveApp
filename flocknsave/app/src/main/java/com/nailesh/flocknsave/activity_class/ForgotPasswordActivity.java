package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nailesh.flocknsave.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView cancel;
    private EditText email;
    private Button resetPassword;

    private SweetAlertDialog pDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setup();
    }

    private void setup(){

        String loginActivityEmail = getIntent().getExtras().getString("email");

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.forgot_password_email);
        resetPassword = findViewById(R.id.forgot_password_reset_button);
        cancel = findViewById(R.id.forgot_password_cancel);

        if(!loginActivityEmail.isEmpty()){
            email.setText(loginActivityEmail);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                ForgotPasswordActivity.this.finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendemail();
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Sending Email");
        pDialog.setCancelable(false);
    }

    private void sendemail(){
        String rEmail = email.getText().toString().trim();

        if(!TextUtils.isEmpty(rEmail) && android.util.Patterns.EMAIL_ADDRESS.matcher(rEmail).matches()) {
            pDialog.show();

            mAuth.sendPasswordResetEmail(rEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismissWithAnimation();
                    gotoHome();
                }
            });
        }else {
            email.setError("Enter valid email address");
            return;
        }

    }

    private void gotoHome(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        ForgotPasswordActivity.this.finish();
    }


}
