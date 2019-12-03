package com.nailesh.flocknsave.activity_class;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Person;

public class RegisterActivity extends AppCompatActivity {

    private EditText _firstName, _lastName, _businessName, _phoneNumber, _streetAddress,
            _suburb, _postCode, _email, _password, _confirmPassword;
    private Spinner _region, _industry, _personType;
    private Button _signup;
    private TextView _linkLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LinearLayout _registerLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setup();

        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        _linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });


    }

    private void setup(){

    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
        //Linear Layout
        _registerLinearLayout = findViewById(R.id.register_linear_layout);

        //EditText
        _firstName = findViewById(R.id.register_first_name);
        _lastName = findViewById(R.id.register_last_name);
        _businessName = findViewById(R.id.register_businessname);
        _phoneNumber = findViewById(R.id.register_phone_number);
        _streetAddress = findViewById(R.id.register_street_address);
        _suburb = findViewById(R.id.register_suburb);
        _postCode = findViewById(R.id.register_post_code);
        _email = findViewById(R.id.register_email);
        _password = findViewById(R.id.register_password);
        _confirmPassword = findViewById(R.id.register_confirm_password);

        //Spinner
        _region = findViewById(R.id.register_region_spinner);
        ArrayAdapter<CharSequence> regionadapter = ArrayAdapter.createFromResource(this,
                R.array.cities_register, android.R.layout.simple_spinner_item);
        regionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _region.setAdapter(regionadapter);

        _industry = findViewById(R.id.register_industry_spinner);
        ArrayAdapter<CharSequence> industryadapter = ArrayAdapter.createFromResource(this,
                R.array.industry, android.R.layout.simple_spinner_item);
        industryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _industry.setAdapter(industryadapter);

        _personType = findViewById(R.id.register_person_type_spinner);
        ArrayAdapter<CharSequence> personadapter = ArrayAdapter.createFromResource(this,
                R.array.person_type, android.R.layout.simple_spinner_item);

        personadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _personType.setAdapter(personadapter);


        // button
        _signup = findViewById(R.id.register_signup);

        _linkLogin = findViewById(R.id.register_link_login);

    }

    private void register(){

        if(!validate()){
            return;
        }else{
            addperson();
        }

    }

    private boolean validate(){
        boolean valid = true;
        String firstName = _firstName.getText().toString();
        String lastName = _lastName.getText().toString();
        String businessName = _businessName.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();
        String streetAddress = _streetAddress.getText().toString();
        String suburb = _suburb.getText().toString();
        String postCode = _postCode.getText().toString();
        String region = _region.getSelectedItem().toString();
        String industry = _industry.getSelectedItem().toString();
        String email = _email.getText().toString().trim();
        String password = _password.getText().toString().trim();
        String confirmPassword = _confirmPassword.getText().toString().trim();


        if (firstName.isEmpty() || firstName.length() < 2) {
            _firstName.setError("at least 2 characters");
            valid = false;
        } else {
            _firstName.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 2) {
            _lastName.setError("at least 2 characters");
            valid = false;
        } else {
            _lastName.setError(null);
        }

        if (businessName.isEmpty() || businessName.length() < 2) {
            _businessName.setError("at least 10 characters");
            valid = false;
        } else {
            _businessName.setError(null);
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() < 8 || phoneNumber.length() > 12){
            _phoneNumber.setError("Enter a valid phone number");
            valid = false;
        }
        else {
            _phoneNumber.setError(null);
        }

        if (streetAddress.isEmpty() || streetAddress.length() < 2) {
            _streetAddress.setError("at least 5 characters");
            valid = false;
        } else {
            _streetAddress.setError(null);
        }

        if (suburb.isEmpty() || suburb.length() < 2) {
            _suburb.setError("at least 4 characters");
            valid = false;
        } else {
            _suburb.setError(null);
        }

        if (postCode.isEmpty() || postCode.length() != 4){
            _postCode.setError("Enter a valid postcode");
            valid = false;
        } else {
            _postCode.setError(null);
        }

        if(region.equals("Select Region")){
            Toast.makeText(RegisterActivity.this, "Select Region", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if(industry.equals("Select Industry")){
            Toast.makeText(RegisterActivity.this, "Select Industry", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("Enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        if(confirmPassword.isEmpty() || !password.equals(confirmPassword)){
            _confirmPassword.setError("Passwords do not match");
            valid = false;
        }else {
            _confirmPassword.setError(null);
        }




        return valid;
    }

    private void toLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void addperson(){
        _registerLinearLayout.getFocusedChild().setEnabled(false);
        String firstName = _firstName.getText().toString();
        String lastName = _lastName.getText().toString();
        String businessName = _businessName.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();
        String streetAddress = _streetAddress.getText().toString();
        String suburb = _suburb.getText().toString();
        String postCode = _postCode.getText().toString();
        String region = _region.getSelectedItem().toString();
        String industry = _industry.getSelectedItem().toString();
        final String email = _email.getText().toString().trim();
        final String password = _password.getText().toString().trim();
        String personType = _personType.getSelectedItem().toString();


        final Person person = new Person(firstName,lastName,businessName,phoneNumber,
                streetAddress,suburb,postCode,region,industry,personType);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            db.collection("users").document(mAuth.getUid()).set(person).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Successful ", Toast.LENGTH_LONG).show();

                                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(RegisterActivity.this, "Verification email sent to " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        RegisterActivity.this.finish();
                                    }
                                }
                            });
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        toLogin();
    }
}