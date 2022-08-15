package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.ClienteDAO;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrarClienteActivity extends AppCompatActivity {

    TextInputEditText medtnombrecliente;
    TextInputEditText medtapellidocliente;
    TextInputEditText medtnumdocumento;
    TextInputEditText medtfechanacimiento;
    Spinner msptipodocumento;
    Spinner mspSexo;
    String tipodoc[]={"DNI","CE"};
    String sexo[]={"MUJER","HOMBRE"};
    Button mbtnregistrar;

    String imgurlHombre = "https://firebasestorage.googleapis.com/v0/b/inversionesgm-da4ba.appspot.com/o/hombre_cliente.png?alt=media&token=769f7a3b-337b-4956-aa47-ce91bb44c6a8";
    String imgurlMujer = "https://firebasestorage.googleapis.com/v0/b/inversionesgm-da4ba.appspot.com/o/mujer_cliente.png?alt=media&token=e5b17c3e-8481-481c-8c34-3f9a27cde743";

    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);
        
        medtnombrecliente = findViewById(R.id.edtnomcliente);
        medtapellidocliente = findViewById(R.id.edtapecliente);
        medtnumdocumento = findViewById(R.id.edtnumdoccliente);
        medtfechanacimiento = findViewById(R.id.edtfechanacimiento);
        msptipodocumento = findViewById(R.id.sptipodocumen);
        ArrayAdapter<String> adapteTipodocu= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipodoc);
        msptipodocumento.setAdapter(adapteTipodocu);
        mspSexo = findViewById(R.id.spsexo);
        ArrayAdapter<String> adapteSexo= new ArrayAdapter(this, android.R.layout.simple_list_item_1,sexo);
        mspSexo.setAdapter(adapteSexo);
        
        mbtnregistrar = findViewById(R.id.btnRegistrarCliente);
        
        clienteDAO = new ClienteDAO();
        
        mbtnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCliente();
            }
        });
    }

    private void saveCliente() {
        String nombre = medtnombrecliente.getText().toString();
        String apellido = medtapellidocliente.getText().toString();
        String sexo = mspSexo.getSelectedItem().toString();
        String tipodocu = msptipodocumento.getSelectedItem().toString();
        String numdocu = medtnumdocumento.getText().toString();
        String fechanacim = medtfechanacimiento.getText().toString();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !sexo.isEmpty() && !tipodocu.isEmpty() && !numdocu.isEmpty() && !fechanacim.isEmpty()){
            if (sexo.equals("HOMBRE")){
                crearCliente(nombre,apellido,sexo,tipodocu,numdocu, fechanacim, imgurlHombre);
            }
            else if (sexo.equals("MUJER")){
                crearCliente(nombre,apellido,sexo,tipodocu,numdocu, fechanacim, imgurlMujer);
            }

        }
        else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void crearCliente(String nombre, String apellido, String sexo, String tipodocu, String numdocu, String fechanacim, String img) {
        Cliente cli = new Cliente();
        cli.setNombre(nombre);
        cli.setApellido(apellido);
        cli.setSexo(sexo);
        cli.setTipodocumento(tipodocu);
        cli.setNumdocumento(numdocu);
        cli.setFechanacimiento(fechanacim);
        cli.setImageCliente(img);
        clienteDAO.save(cli).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    limpiar();
                    Toast.makeText(RegistrarClienteActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrarClienteActivity.this, "No se pudo almacenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void limpiar(){
        medtnombrecliente.setText("");
        medtapellidocliente.setText("");
        medtfechanacimiento.setText("");
        medtnumdocumento.setText("");
        msptipodocumento.setSelection(0);
        mspSexo.setSelection(0);
    }
}