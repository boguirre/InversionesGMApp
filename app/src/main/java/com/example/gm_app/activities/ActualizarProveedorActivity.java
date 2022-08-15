package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.AuthProvider;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class ActualizarProveedorActivity extends AppCompatActivity {

    TextInputEditText medtRazonSocial;
    TextInputEditText medtRuc;
    TextInputEditText medtTelefono;
    Spinner mspTio;
    String tipo[]={"SERVICIO","VENTAS"};
    TextInputEditText medtDireccion;
    Button mbtnactualizar;
    AlertDialog mDialog;
    String mExtraProvId;

    ProveedorDAO daoproveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_proveedor);
        medtRazonSocial = findViewById(R.id.etdRazonSocial);
        medtRuc = findViewById(R.id.edtruc);
        medtTelefono = findViewById(R.id.edtTelefonoProv);
        medtDireccion = findViewById(R.id.etdDireccion);
        mspTio = findViewById(R.id.spTipo);
        ArrayAdapter<String> adapteTip= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipo);
        mspTio.setAdapter(adapteTip);
        mbtnactualizar = findViewById(R.id.btnActualizarProv);
        mExtraProvId = getIntent().getStringExtra("id");

        daoproveedor = new ProveedorDAO();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();



        mbtnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarProveedor();
            }
        });

        getProveedor();
    }

    private void updateInfo(Proveedor prov){
        mDialog.show();
        daoproveedor.updateProveedor(prov).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(ActualizarProveedorActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ActualizarProveedorActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ActualizarProveedor() {
        String razonsocial = medtRazonSocial.getText().toString();
        String ruc = medtRuc.getText().toString();
        String direccion = medtDireccion.getText().toString();
        String telefono = medtTelefono.getText().toString();
        String tipo = mspTio.getSelectedItem().toString();
        if (!razonsocial.isEmpty() && !ruc.isEmpty() && !direccion.isEmpty() && !telefono.isEmpty()) {
            Proveedor prov = new Proveedor();
            prov.setRazonsocial(razonsocial);
            prov.setRuc(ruc);
            prov.setDireccion(direccion);
            prov.setTelefono(telefono);
            prov.setTipo(tipo);
            prov.setId(mExtraProvId);
            updateInfo(prov);
        }
        else {
            Toast.makeText(this, "Completar los datos por favor", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProveedor(){
        daoproveedor.getProveedorById(mExtraProvId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("razonsocial")){
                        String razonsocial = documentSnapshot.getString("razonsocial");
                        medtRazonSocial.setText(razonsocial);
                    }

                    if (documentSnapshot.contains("ruc")){
                        String ruc = documentSnapshot.getString("ruc");
                        medtRuc.setText(ruc);
                    }

                    if (documentSnapshot.contains("telefono")){
                        String telefono = documentSnapshot.getString("telefono");
                        medtTelefono.setText(telefono);
                    }
                    if (documentSnapshot.contains("direccion")){
                        String direccion = documentSnapshot.getString("direccion");
                        medtDireccion.setText(direccion);
                    }

                }
            }
        });
    }
}