package com.example.gm_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gm_app.Dao.ClienteDAO;
import com.example.gm_app.R;
import com.example.gm_app.activities.ActualizarClienteActivity;
import com.example.gm_app.activities.ActualizarProveedorActivity;
import com.example.gm_app.activities.IngresosStockActivity;
import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ClienteAdapter extends FirestoreRecyclerAdapter<Cliente, ClienteAdapter.ViewHolder> {

    Context context;
    ClienteDAO clienteDAO;

    public ClienteAdapter(FirestoreRecyclerOptions<Cliente> options, Context context){
        super(options);
        this.context = context;
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void onBindViewHolder(@NonNull ClienteAdapter.ViewHolder holder, int position, @NonNull Cliente cli) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String clienteId = document.getId();

        holder.textnomcliente.setText(cli.getNombre().toUpperCase() + " " + cli.getApellido().toUpperCase());
        holder.textsexo.setText("SEXO:  "+cli.getSexo());
        holder.texttipodoc.setText(cli.getTipodocumento() + ": " + cli.getNumdocumento());
        holder.textfechanaci.setText("Fecha de Nacimiento:  "+ cli.getFechanacimiento());
        if (cli.getImageCliente() != null){
            if (!cli.getImageCliente().isEmpty()){
                Picasso.with(context).load(cli.getImageCliente()).into(holder.imageViewCliente);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mbuilderSelector.setItems(holder.options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            Intent intent = new Intent(context, ActualizarClienteActivity.class);
                            intent.putExtra("id", clienteId);
                            context.startActivity(intent);
                        }
                        else if (i == 1){
                            MensajeEliminar(clienteId);
                        }
                    }
                });
                holder.mbuilderSelector.show();
            }
        });


    }

    private void MensajeEliminar(String clienteId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Estas Seguro de eliminar este cliente?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminaCliente(clienteId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void eliminaCliente(String clientId) {
        clienteDAO.delete(clientId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "El Cliente se elimino correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "El Cliente no se elimino", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cliente, parent, false);
        return new ClienteAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textnomcliente;
        TextView textsexo;
        TextView texttipodoc;
        TextView textfechanaci;
        ImageView imageViewCliente;
        View viewHolder;
        AlertDialog.Builder mbuilderSelector;
        CharSequence options[];


        public ViewHolder(View view){
            super(view);
            textnomcliente = view.findViewById(R.id.txtnomcliente);
            textsexo = view.findViewById(R.id.txtsexo);
            texttipodoc = view.findViewById(R.id.txttipodoc);
            textfechanaci= view.findViewById(R.id.txtfechanacimiento);
            imageViewCliente = view.findViewById(R.id.imageViewCliente);
            viewHolder = view;
            mbuilderSelector = new AlertDialog.Builder(context);
            mbuilderSelector.setTitle("Selecciona una opcion");
            options = new  CharSequence[] {"Actualizar Cliente", "Eliminar Cliente"};
        }

    }
}
