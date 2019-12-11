package com.nailesh.flocknsave.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.activity_class.AddElectricityActivity;
import com.nailesh.flocknsave.activity_class.AddLocationActivity;
import com.nailesh.flocknsave.adapter.DeliveryLocationAdapter;
import com.nailesh.flocknsave.adapter.ElectricityAdapter;
import com.nailesh.flocknsave.model.Electricity;
import com.nailesh.flocknsave.model.Location;

public class AccountLocationFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Query query;

    private Button addLocation;

    private DeliveryLocationAdapter adapter;





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
                Intent intent = new Intent(getContext(), AddLocationActivity.class)
                        .putExtra("locationID","null");
                startActivity(intent);
            }
        });

        setupRecyclerView(view);

    }

    private void setupRecyclerView(View view){
        query = db.collection("users").document(mAuth.getUid()).collection("locations");

        FirestoreRecyclerOptions<Location> options = new FirestoreRecyclerOptions.Builder<Location>()
                .setQuery(query,Location.class)
                .build();

        adapter = new DeliveryLocationAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.location_Recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DeliveryLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                String locationID = documentSnapshot.getId();

                Intent intent = new Intent(getContext(), AddLocationActivity.class)
                        .putExtra("locationID",locationID);
                startActivity(intent);

            }
        });

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
