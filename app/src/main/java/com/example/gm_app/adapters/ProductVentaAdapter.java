package com.example.gm_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gm_app.R;
import com.example.gm_app.activities.IngresosStockActivity;
import com.example.gm_app.activities.RegistrarVentaActivity;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ProductVentaAdapter  extends FirestoreRecyclerAdapter<Producto, ProductVentaAdapter.ViewHolder> {
    Context context;

    public ProductVentaAdapter(FirestoreRecyclerOptions<Producto> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductVentaAdapter.ViewHolder holder, int position, @NonNull Producto prod) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String productId = document.getId();

        holder.textNomprod.setText(prod.getNomprod().toUpperCase());
        holder.textTipoprod.setText("TIPO:  "+prod.getTipoprod());
        holder.textPreciovent.setText("Precio Venta:  "+prod.getPrecioventaprod());
        holder.textStock.setText("Stock: " + prod.getStockprod() + " Productos");
        if (prod.getImageProducto() != null){
            if (!prod.getImageProducto().isEmpty()){
                Picasso.with(context).load(prod.getImageProducto()).into(holder.imageViewProducto);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistrarVentaActivity.class);
                intent.putExtra("id", productId);
                context.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public ProductVentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_venta, parent, false);
        return new ProductVentaAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textNomprod;
        TextView textPreciovent;
        TextView textTipoprod;
        TextView textStock;
        ImageView imageViewProducto;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textNomprod = view.findViewById(R.id.txtnomprod);
            textPreciovent = view.findViewById(R.id.txtpreciovent);
            textTipoprod = view.findViewById(R.id.txttipoprod);
            textStock = view.findViewById(R.id.txtstock);
            imageViewProducto = view.findViewById(R.id.imageViewProducto);
            viewHolder = view;
        }

    }
}
