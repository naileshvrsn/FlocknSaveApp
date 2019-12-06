package com.nailesh.flocknsave.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.model.Product;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product model) {
        holder.product_name.setText(model.getName());
        holder.product_description.setText(model.getDescription());
        holder.product_region.setText(model.getRegion());
        holder.product_saving.setText("Saving: "+model.getSavings()+"%");

        if(model.getImageLocation().isEmpty()){
            holder.product_image.setImageResource(R.drawable.icon_product);
        }else{
            Picasso.get().load(model.getImageLocation()).into(holder.product_image);
        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_product_item,parent,false);

        return new ProductHolder(view);
    }

    class ProductHolder extends RecyclerView.ViewHolder{
        TextView product_name;
        TextView product_description;
        TextView product_region;
        TextView product_saving;

        ImageView product_image;

        public ProductHolder(@NonNull View itemView) {

            super(itemView);

            product_name = itemView.findViewById(R.id.product_name);
            product_description = itemView.findViewById(R.id.product_description);
            product_region = itemView.findViewById(R.id.product_region);
            product_saving = itemView.findViewById(R.id.product_saving);

            product_image = itemView.findViewById(R.id.product_image_view);
        }

    }


}
