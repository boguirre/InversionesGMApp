package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gm_app.Dao.ImageDAO;
import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class ActualizarProductoActivity extends AppCompatActivity {

    TextInputEditText medtnombreprod;
    TextInputEditText medtpreciocompraprod;
    TextInputEditText medtprecioventaprod;
    //private int stockprod = 0;
    Spinner mspProd;
    String tipoprop[]={"CELULARES","COVERS","EQUIPOS DE AUDIO"};
    Button btnactualizarProd;

    ProductoDAO daoproducto;
    ImageDAO imageDAO;

    AlertDialog mDialog;
    AlertDialog.Builder mbuilderSelector;
    CharSequence options[];

    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    String mExtraProductId;

    String nomprod = "";
    String tipoprod = "";
    String imageAct = "";
    double preciocompra = 0;
    double precioventa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_producto);

        medtnombreprod = findViewById(R.id.edtnomprod);
        medtpreciocompraprod = findViewById(R.id.edtpreciocompr);
        medtprecioventaprod = findViewById(R.id.edtpreciovent);
        mspProd = findViewById(R.id.spTipoprod);
        ArrayAdapter<String> adapteTip= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipoprop);
        mspProd.setAdapter(adapteTip);

        btnactualizarProd = findViewById(R.id.btnActualizarProduct);
        mExtraProductId = getIntent().getStringExtra("id");

        daoproducto = new ProductoDAO();
        imageDAO = new ImageDAO();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();



        btnactualizarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarProducto();
            }
        });

        getProducto();
    }

    private void updateInfo(Producto prod){
        mDialog.show();
        daoproducto.updateProduct(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(ActualizarProductoActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ActualizarProductoActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProducto(){
        daoproducto.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nomprod")){
                        String nomproduct = documentSnapshot.getString("nomprod");
                        medtnombreprod.setText(nomproduct);
                    }

                    if (documentSnapshot.contains("preciocompraprod")){
                        Double preciocompraprod = documentSnapshot.getDouble("preciocompraprod");
                        medtpreciocompraprod.setText(preciocompraprod+"");
                    }

                    if (documentSnapshot.contains("precioventaprod")){
                        Double precioventaprod = documentSnapshot.getDouble("precioventaprod");
                        medtprecioventaprod.setText(precioventaprod+"");
                    }

                }
            }
        });
    }

    private void ActualizarProducto(){
        nomprod = medtnombreprod.getText().toString();
        tipoprod = mspProd.getSelectedItem().toString();
        preciocompra = Double.parseDouble(medtpreciocompraprod.getText().toString());
        precioventa = Double.parseDouble(medtprecioventaprod.getText().toString());
        if (!nomprod.isEmpty()) {
            Producto prod = new Producto();
            prod.setNomprod(nomprod);
            prod.setTipoprod(tipoprod);
            prod.setPrecioventaprod(precioventa);
            prod.setPreciocompraprod(preciocompra);
            prod.setIdprod(mExtraProductId);
            updateInfo(prod);


        }
        else {
            Toast.makeText(this, "Completar los datos por favor", Toast.LENGTH_SHORT).show();
        }

    }
}