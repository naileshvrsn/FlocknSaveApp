package com.nailesh.flocknsave.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Electricity;
import com.nailesh.flocknsave.model.Location;

public class DeliveryLocationAdapter extends FirestoreRecyclerAdapter<Location, DeliveryLocationAdapter.LocationHolder> {

    private DeliveryLocationAdapter.OnItemClickListener listener;

    public DeliveryLocationAdapter(@NonNull FirestoreRecyclerOptions<Location> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationHolder holder, int position, @NonNull Location model) {
        String add2;

        if(model.getAddressLine2() ==(null)){
            add2 = "";
        }else {
            add2 = model.getAddressLine2() +"\n";
        }


        String address = model.getStreetNumber()+" "+model.getAddressLine1() +"\n"+
                add2+
                model.getSuburb()+"\n"+
                model.getCity()+"\n"+
                model.getPostCode()+"\n"+
                model.getRegion();
        holder.deliveryLocation.setText(address);
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_location_item,parent,false);

        return new LocationHolder(v);
    }

    class LocationHolder extends RecyclerView.ViewHolder{

        TextView deliveryLocation;
        Button deleteLocation;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            deliveryLocation = itemView.findViewById(R.id.delivery_location);
            deleteLocation =itemView.findViewById(R.id.location_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
            deleteLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    deleteDeliverylocation(position);
                }
            });

        }
    }

    private void deleteDeliverylocation(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(DeliveryLocationAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
