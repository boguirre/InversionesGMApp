package com.example.gm_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.activities.ActualizarImageProductActivity;
import com.example.gm_app.activities.ActualizarProductoActivity;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ProductoAdapter extends FirestoreRecyclerAdapter<Producto, ProductoAdapter.ViewHolder> {

    Context context;
    ProductoDAO productoDAO;
    TextView mtextNumberfilter;

    public ProductoAdapter(FirestoreRecyclerOptions<Producto> options, Context context){
        super(options);
        this.context = context;
        productoDAO = new ProductoDAO();
    }

    public ProductoAdapter(FirestoreRecyclerOptions<Producto> options, Context context, TextView textView){
        super(options);
        this.context = context;
        productoDAO = new ProductoDAO();
        mtextNumberfilter = textView;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position, @NonNull Producto prod) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String producId = document.getId();

        if (mtextNumberfilter != null){
            int numberfilter = getSnapshots().size();
            mtextNumberfilter.setText(String.valueOf(numberfilter));
        }

        holder.textNomprod.setText(prod.getNomprod().toUpperCase());
        holder.textTipoprod.setText("TIPO:  "+prod.getTipoprod());
        holder.textPreciovent.setText("Precio Venta:  "+prod.getPrecioventaprod());
        holder.textPrecioCompr.setText("Precio Compra:  "+ prod.getPreciocompraprod());
        holder.textStock.setText("Stock: " + prod.getStockprod());
        if (prod.getImageProducto() != null){
            if (!prod.getImageProducto().isEmpty()){
                Picasso.with(context).load(prod.getImageProducto()).into(holder.imageViewProducto);
            }
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mbuilderSelector.setItems(holder.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            Intent intent = new Intent(context, ActualizarProductoActivity.class);
                            intent.putExtra("id", producId);
                            context.startActivity(intent);
                        }
                        else if (i == 1){
                            Intent intent = new Intent(context, ActualizarImageProductActivity.class);
                            intent.putExtra("id", producId);
                            context.startActivity(intent);
                        }
                        else if (i == 2){
                            MensajeEliminar(producId);
                        }
                    }
                });
                holder.mbuilderSelector.show();
            }
        });



    }

    private void MensajeEliminar(String producId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estas Seguro de eliminar este producto?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarProducto(producId);
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void eliminarProducto(String producId) {
        productoDAO.delete(producId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "El producto se elimino correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "El producto no se elimino", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto, parent, false);
        return new ProductoAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textNomprod;
        TextView textPreciovent;
        TextView textPrecioCompr;
        TextView textTipoprod;
        TextView textStock;
        ImageView imageViewProducto;
        View viewHolder;
        AlertDialog.Builder mbuilderSelector;
        CharSequence options[];


        public ViewHolder(View view){
            super(view);
            textNomprod = view.findViewById(R.id.txtnomprod);
            textPrecioCompr = view.findViewById(R.id.txtpreciocomp);
            textPreciovent = view.findViewById(R.id.txtpreciovent);
            textTipoprod = view.findViewById(R.id.txttipoprod);
            textStock = view.findViewById(R.id.txtstock);
            imageViewProducto = view.findViewById(R.id.imageViewProducto);
            viewHolder = view;
            mbuilderSelector = new AlertDialog.Builder(context);
            mbuilderSelector.setTitle("Selecciona una opcion");
            options = new  CharSequence[] {"Actualizar Informacion", "Actualizar Imagen", "Eliminar"};
        }

    }
}
