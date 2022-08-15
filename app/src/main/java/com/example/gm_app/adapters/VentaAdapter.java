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
import com.example.gm_app.activities.DetalleVentaActivity;
import com.example.gm_app.activities.IngresosStockActivity;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Venta;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class VentaAdapter extends FirestoreRecyclerAdapter<Venta, VentaAdapter.ViewHolder> {
    Context context;

    public VentaAdapter(FirestoreRecyclerOptions<Venta> options, Context context){
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VentaAdapter.ViewHolder holder, int position, @NonNull Venta vent) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String VentaId = document.getId();
        String imageprod = document.getString("idprod");

        holder.textFechaventa.setText("VENTA " + vent.getFecha());
        holder.textNombreProdventa.setText(vent.getNomproduct());
        holder.textCantidadVenta.setText("Cantidad:  "+vent.getCantidad());
        holder.textPrecioTotalventa.setText("Monto Total:  "+ vent.getTotalventa());
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalleVentaActivity.class);
                intent.putExtra("id", VentaId);
                intent.putExtra("idprod", imageprod);
                context.startActivity(intent);

            }
        });


    }

    @NonNull
    @Override
    public VentaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_venta, parent, false);
        return new VentaAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textFechaventa;
        TextView textNombreProdventa;
        TextView textPrecioTotalventa;
        TextView textCantidadVenta;
        View viewHolder;


        public ViewHolder(View view){
            super(view);
            textFechaventa = view.findViewById(R.id.txtfechaventa);
            textNombreProdventa = view.findViewById(R.id.txtnombreproducto);
            textCantidadVenta = view.findViewById(R.id.txtcantidad);
            textPrecioTotalventa = view.findViewById(R.id.txtpreciototalventa);
            viewHolder = view;
        }

    }

}
