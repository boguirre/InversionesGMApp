package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.AuthProvider;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrarProveedorActivity extends AppCompatActivity {

    TextInputEditText medtRazonSocial;
    TextInputEditText medtRuc;
    TextInputEditText medtTelefono;
    Spinner mspTio;
    String tipo[]={"SERVICIO","VENTAS"};
    TextInputEditText medtDireccion;
    Button mbtnregistrar;

    ProveedorDAO daoproveedor;
    AuthProvider mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_proveedor);

        medtRazonSocial = findViewById(R.id.etdRazonSocial);
        medtRuc = findViewById(R.id.edtruc);
        medtTelefono = findViewById(R.id.edtTelefonoProv);
        medtDireccion = findViewById(R.id.etdDireccion);
        mspTio = findViewById(R.id.spTipo);
        ArrayAdapter<String> adapteTip= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipo);
        mspTio.setAdapter(adapteTip);
        mbtnregistrar = findViewById(R.id.btnRegistrarProv);

        daoproveedor = new ProveedorDAO();
        mauth = new AuthProvider();

        mbtnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProveedor();
            }
        });

    }

    private void saveProveedor() {

        String razonsocial = medtRazonSocial.getText().toString();
        String ruc = medtRuc.getText().toString();
        String direccion = medtDireccion.getText().toString();
        String telefono = medtTelefono.getText().toString();
        String tipo = mspTio.getSelectedItem().toString();

        if (!razonsocial.isEmpty() && !ruc.isEmpty() && !telefono.isEmpty() && !tipo.isEmpty() && !direccion.isEmpty()){
            crearProveedor(razonsocial,ruc,direccion,telefono,tipo);
        }
        else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    public void crearProveedor(final String razonsocial, final String ruc, final String direccion, final String telefono, final String tippo) {
        Proveedor pro = new Proveedor();
        pro.setRazonsocial(razonsocial);
        pro.setRuc(ruc);
        pro.setTipo(tippo);
        pro.setTelefono(telefono);
        pro.setDireccion(direccion);
        pro.setIdUser(mauth.getUid());
        daoproveedor.save(pro).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    limpiar();
                    Toast.makeText(RegistrarProveedorActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrarProveedorActivity.this, "No se pudo almacenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limpiar(){
        medtRazonSocial.setText("");
        medtRuc.setText("");
        medtTelefono.setText("");
        medtDireccion.setText("");
    }
}