package com.nailesh.flocknsave.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nailesh.flocknsave.R;

public class AccountLocationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button addLocation;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_location,container,false);
        setup(view);
        return view;
    }

    private void setup(View view){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addLocation = view.findViewById(R.id.location_add);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Add Location",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
