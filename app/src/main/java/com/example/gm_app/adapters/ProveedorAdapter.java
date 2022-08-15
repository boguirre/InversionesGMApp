package com.example.gm_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.activities.ActualizarImageProductActivity;
import com.example.gm_app.activities.ActualizarProductoActivity;
import com.example.gm_app.activities.ActualizarProveedorActivity;
import com.example.gm_app.modelo.Proveedor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProveedorAdapter extends FirestoreRecyclerAdapter<Proveedor, ProveedorAdapter.ViewHolder> {

    Context context;
    ProveedorDAO proveedorDAO;

    public ProveedorAdapter(FirestoreRecyclerOptions<Proveedor> options, Context context){
        super(options);
        this.context = context;
        proveedorDAO = new ProveedorDAO();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Proveedor pro) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String provId = document.getId();

        holder.textRazonsocial.setText(pro.getRazonsocial().toUpperCase());
        holder.textTelefono.setText("Telefono: "+pro.getTelefono());
        holder.textDireccion.setText("Direccion: "+pro.getDireccion());
        holder.textTipo.setText("Tipo: "+ pro.getTipo());
        holder.textRuc.setText("RUC: " + pro.getRuc());
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mbuilderSelector.setItems(holder.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            Intent intent = new Intent(context, ActualizarProveedorActivity.class);
                            intent.putExtra("id", provId);
                            context.startActivity(intent);
                        }
                        else if (i == 1){
                            MensajeEliminar(provId);
                        }
                    }
                });
                holder.mbuilderSelector.show();
            }
        });

    }

    private void MensajeEliminar(String provId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar Proveedor")
                .setMessage("¿Estas Seguro de eliminar este proveedor?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarProveedor(provId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminarProveedor(String provId) {
        proveedorDAO.delete(provId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "El proveedor se elimino correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "El proveedor no se elimino", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_proveedor, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textRazonsocial;
        TextView textRuc;
        TextView textTelefono;
        TextView textTipo;
        TextView textDireccion;
        View viewHolder;
        AlertDialog.Builder mbuilderSelector;
        CharSequence options[];

        public ViewHolder(View view){
            super(view);
            textRazonsocial = view.findViewById(R.id.txtrazonsocial);
            textRuc = view.findViewById(R.id.txtruc);
            textTelefono = view.findViewById(R.id.txttelefono);
            textTipo = view.findViewById(R.id.txttipo);
            textDireccion = view.findViewById(R.id.txtDireccion);
            viewHolder = view;
            mbuilderSelector = new AlertDialog.Builder(context);
            mbuilderSelector.setTitle("Selecciona una opcion");
            options = new  CharSequence[] {"Actualizar Proveedor", "Eliminar"};
        }

    }

}
