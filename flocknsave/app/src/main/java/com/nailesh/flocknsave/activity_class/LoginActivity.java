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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nailesh.flocknsave.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email,login_password;
    private Button signin;
    private TextView forgot_password;
    private SweetAlertDialog pDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    private void setup(){
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        signin = findViewById(R.id.login_button);
        forgot_password = findViewById(R.id.login_link_reset_password);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // Logged in user already Exist
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            this.finish();
        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }else{
                    pDialog.show();
                    signinUser();
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Logging In");
        pDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu,menu);

        menu.findItem(R.id.nav_menu_login).setVisible(false);
        menu.findItem(R.id.nav_menu_register).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void resetPassword(){
        Intent resetPassword = new Intent(getApplicationContext(),ForgotPasswordActivity.class).putExtra("email", login_email.getText().toString());
        startActivity(resetPassword);
        this.finish();
    }

    private boolean validate(){
        boolean valid = true;

        String email = login_email.getText().toString();
        String password = login_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email.setError("Enter a valid email address");
            valid = false;
        } else {
            login_email.setError(null);
        }

        if (password.isEmpty()) {
            login_password.setError("Enter password");
            valid = false;
        } else {
            login_password.setError(null);
        }
        return valid;
    }

    private void signinUser(){

        login_password.setEnabled(false);
        String email = login_email.getText().toString();
        final String password = login_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login failed. Have you registered?", Toast.LENGTH_SHORT).show();
                    login_password.setText("");
                    login_password.setEnabled(true);
                    pDialog.dismissWithAnimation();

                }else{
                    Toast.makeText(getApplicationContext(), "Login successfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    pDialog.dismissWithAnimation();
                }

            }
        });
    }
}
