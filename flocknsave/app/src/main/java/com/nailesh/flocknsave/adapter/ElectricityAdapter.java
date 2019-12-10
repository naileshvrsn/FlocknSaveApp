package com.nailesh.flocknsave.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Electricity;

public class ElectricityAdapter extends FirestoreRecyclerAdapter<Electricity, ElectricityAdapter.ICPHolder> {


    public ElectricityAdapter(@NonNull FirestoreRecyclerOptions<Electricity> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ICPHolder holder, int position, @NonNull Electricity model) {
        holder.icpNumber.setText(model.getIcp());
    }

    @NonNull
    @Override
    public ICPHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.electricoty_icp_item,parent,false);

        return new ICPHolder(v);
    }

    class ICPHolder extends RecyclerView.ViewHolder{

        TextView icpNumber;


        public ICPHolder(@NonNull View itemView) {
            super(itemView);
            icpNumber = itemView.findViewById(R.id.electricity_icp_number);
        }
    }
}
