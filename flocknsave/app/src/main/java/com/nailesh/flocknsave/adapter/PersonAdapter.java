package com.nailesh.flocknsave.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Person;

public class PersonAdapter extends FirestoreRecyclerAdapter<Person, PersonAdapter.PersonHolder> {

    private ProductAdapter.OnItemClickListener listener;

    public PersonAdapter(@NonNull FirestoreRecyclerOptions<Person> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PersonHolder holder, int position, @NonNull Person model) {
        holder.supplier_name.setText(model.getFirstName() +" "+model.getLastName());
        holder.supplier_business.setText(model.getBusinessName());
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_list_item,parent,false);

        return new PersonHolder(view);
    }

    class PersonHolder extends RecyclerView.ViewHolder {

        TextView supplier_name;
        TextView supplier_business;

        public PersonHolder(@NonNull View itemView) {
            super(itemView);

            supplier_name = itemView.findViewById(R.id.supplier_name);
            supplier_business = itemView.findViewById(R.id.supplier_business_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot);
    }

    public void setOnItemClickListener(ProductAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}
