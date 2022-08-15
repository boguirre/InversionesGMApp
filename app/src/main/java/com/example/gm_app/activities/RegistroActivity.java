package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.AuthProvider;
import com.example.gm_app.Dao.UsuarioDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistroActivity extends AppCompatActivity {

    CircleImageView crBack;
    TextInputEditText medtNombre;
    TextInputEditText medtApellido;
    TextInputEditText medtTelefono;
    Spinner mspCargo;
    String tipo[]={"DNI","CARNET DE EXTRANJERIA"};
    TextInputEditText medtNumDocumento;
    TextInputEditText medtCorreo;
    TextInputEditText medtPassword;
    Button mbtnRegistrar;
    AuthProvider mAuthDao;
    UsuarioDAO usuarioDAO;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuthDao = new AuthProvider();
        usuarioDAO = new UsuarioDAO();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        crBack = findViewById(R.id.circleBack);
        medtNombre = findViewById(R.id.etdNombre);
        medtApellido = findViewById(R.id.edtApellido);
        medtTelefono = findViewById(R.id.edtTelefono);
        mspCargo = findViewById(R.id.spCargo);
        medtNumDocumento = findViewById(R.id.edtNumDocumento);
        medtCorreo = findViewById(R.id.edtEmail);
        medtPassword = findViewById(R.id.edtContrseña);
        mbtnRegistrar = findViewById(R.id.btnRegistrar);
        ArrayAdapter<String> adapteCr= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipo);
        mspCargo.setAdapter(adapteCr);

        mbtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });

        crBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void registrar(){
        String nombre = medtNombre.getText().toString();
        String apellido = medtApellido.getText().toString();
        String telefono = medtTelefono.getText().toString();
        String tipo = mspCargo.getSelectedItem().toString();
        String numDocumento = medtNumDocumento.getText().toString();
        String correo = medtCorreo.getText().toString();
        String password = medtPassword.getText().toString();

        if (!nombre.isEmpty() && !apellido.isEmpty() && !telefono.isEmpty() && !tipo.isEmpty() && !numDocumento.isEmpty() && !correo.isEmpty() && !password.isEmpty()){
            if (isEmailValid(correo)){
                crearUsuario(nombre,apellido,telefono,tipo,numDocumento,correo,password);

            }
            else {
                Toast.makeText(this, "Has insertado todos los campos  pero email invalido", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Para continuar inserte todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void crearUsuario(final String nombre, final String apellido, final String telefono, final String tipo, final String numDocum, final String email, String password){
      mDialog.show();
        mAuthDao.registrar(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    String id = mAuthDao.getUid();
                    Usuario user = new Usuario();
                    user.setId(id);
                    user.setNombre(nombre);
                    user.setApellido(apellido);
                    user.setTelefono(telefono);
                    user.setCargo(tipo);
                    user.setNumDocumento(numDocum);
                    user.setEmail(email);
                    usuarioDAO.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Intent intent = new Intent(RegistroActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(RegistroActivity.this, "No se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}