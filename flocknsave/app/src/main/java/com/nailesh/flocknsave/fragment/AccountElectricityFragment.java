package com.nailesh.flocknsave.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.activity_class.AddElectricityActivity;
import com.nailesh.flocknsave.adapter.ElectricityAdapter;
import com.nailesh.flocknsave.model.Electricity;

import org.w3c.dom.Text;

public class AccountElectricityFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView noicp;

    private Button addICP;

    private Query query;

    private ElectricityAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_electricity,container,false);

        setup(view);

        return view;
    }

    private void setup(View view) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        noicp = view.findViewById(R.id.electricity_no_icp);

        addICP = view.findViewById(R.id.electricity_add_icp);
        addICP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddElectricityActivity.class);
                startActivity(intent);
            }
        });

        setupRecyclerView(view);
    }

    private void setupRecyclerView(View view){
        query = db.collection("users").document(mAuth.getUid()).collection("electric_icp");

        FirestoreRecyclerOptions<Electricity> options = new FirestoreRecyclerOptions.Builder<Electricity>()
                .setQuery(query,Electricity.class)
                .build();

        adapter = new ElectricityAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.electricity_Recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
