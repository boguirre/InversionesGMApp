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
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class IngresoAdapter extends FirestoreRecyclerAdapter<Producto, IngresoAdapter.ViewHolder> {
    Context context;

    public IngresoAdapter(FirestoreRecyclerOptions<Producto> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull IngresoAdapter.ViewHolder holder, int position, @NonNull Producto prod) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();

        holder.textNomprod.setText(prod.getNomprod().toUpperCase());
        holder.textTipoprod.setText("TIPO:  "+prod.getTipoprod());
        holder.textPreciovent.setText("Precio Venta:  "+prod.getPrecioventaprod());
        holder.textPrecioCompr.setText("Precio Compra:  "+ prod.getPreciocompraprod());
        if(prod.getStockprod() > 0){
            holder.textStock.setText("Stock: " + prod.getStockprod() + " Productos");
            holder.textStock.setTextColor(Color.GREEN);
        }
        else {
            holder.textStock.setText("Stock: Producto sin stock!!");
            holder.textStock.setTextColor(Color.RED);
        }
        if (prod.getImageProducto() != null){
            if (!prod.getImageProducto().isEmpty()){
                Picasso.with(context).load(prod.getImageProducto()).into(holder.imageViewProducto);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IngresosStockActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);

            }
        });


    }

    @NonNull
    @Override
    public IngresoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto, parent, false);
        return new IngresoAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textNomprod;
        TextView textPreciovent;
        TextView textPrecioCompr;
        TextView textTipoprod;
        TextView textStock;
        ImageView imageViewProducto;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textNomprod = view.findViewById(R.id.txtnomprod);
            textPrecioCompr = view.findViewById(R.id.txtpreciocomp);
            textPreciovent = view.findViewById(R.id.txtpreciovent);
            textTipoprod = view.findViewById(R.id.txttipoprod);
            textStock = view.findViewById(R.id.txtstock);
            imageViewProducto = view.findViewById(R.id.imageViewProducto);
            viewHolder = view;
        }

    }
}
