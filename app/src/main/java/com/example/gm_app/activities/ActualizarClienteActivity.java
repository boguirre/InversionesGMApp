package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.ClienteDAO;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class ActualizarClienteActivity extends AppCompatActivity {

    TextInputEditText medtsexo;
    TextInputEditText medtnombrecliente;
    TextInputEditText medtapellidocliente;
    TextInputEditText medtnumdocumento;
    TextInputEditText medtfechanacimiento;
    TextInputEditText medtipodocut;

    AlertDialog mDialog;
    String mExtraClienteId;

    Button mbtnactualizar;

    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_cliente);

        medtnombrecliente = findViewById(R.id.edtnomcliente);
        medtapellidocliente = findViewById(R.id.edtapecliente);
        medtnumdocumento = findViewById(R.id.edtnumdoccliente);
        medtfechanacimiento = findViewById(R.id.edtfechanacimiento);
        medtipodocut = findViewById(R.id.edttipodocu);
        medtsexo = findViewById(R.id.edtsexocliente);

        mExtraClienteId = getIntent().getStringExtra("id");

        clienteDAO = new ClienteDAO();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        mbtnactualizar = findViewById(R.id.btnActualizarCliente);

        inhabilitarCampos();

        mbtnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarCliente();
            }
        });

        getCliente();
    }

    private void updateInfo(Cliente clie){
        mDialog.show();
        clienteDAO.updateCliente(clie).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(ActualizarClienteActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ActualizarClienteActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ActualizarCliente() {
        String nombre = medtnombrecliente.getText().toString();
        String apellido = medtapellidocliente.getText().toString();
        String fechanacim = medtfechanacimiento.getText().toString();
        if (!nombre.isEmpty() && !apellido.isEmpty() && !fechanacim.isEmpty()){
            Cliente cli = new Cliente();
            cli.setNombre(nombre);
            cli.setApellido(apellido);
            cli.setFechanacimiento(fechanacim);
            cli.setId(mExtraClienteId);
            updateInfo(cli);
        }
        else {
            Toast.makeText(this, "Completar los datos por favor", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCliente(){
        clienteDAO.getClienteById(mExtraClienteId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombre")){
                        String nombre = documentSnapshot.getString("nombre");
                        medtnombrecliente.setText(nombre);
                    }

                    if (documentSnapshot.contains("apellido")){
                        String apellido = documentSnapshot.getString("apellido");
                        medtapellidocliente.setText(apellido);
                    }

                    if (documentSnapshot.contains("sexo")){
                        String sexo = documentSnapshot.getString("sexo");
                        medtsexo.setText(sexo);
                    }
                    if (documentSnapshot.contains("tipodocumento")){
                        String tipodocumento = documentSnapshot.getString("tipodocumento");
                        medtipodocut.setText(tipodocumento);
                    }

                    if (documentSnapshot.contains("numdocumento")){
                        String numdocumento = documentSnapshot.getString("numdocumento");
                        medtnumdocumento.setText(numdocumento);
                    }

                    if (documentSnapshot.contains("fechanacimiento")){
                        String fechanacimiento = documentSnapshot.getString("fechanacimiento");
                        medtfechanacimiento.setText(fechanacimiento);
                    }

                }
            }
        });
    }

    private void inhabilitarCampos(){
        medtsexo.setEnabled(false);
        medtipodocut.setEnabled(false);
        medtnumdocumento.setEnabled(false);

    }
}