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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gm_app.Dao.ImageDAO;
import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class ActualizarImageProductActivity extends AppCompatActivity {

    ImageView mimageProducto;
    TextView mtxtnombre;
    File imageFile;
    private  final int GALERY_REQUEST_CODE = 1;
    private  final int PHOTO_REQUEST_CODE = 2;
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
        setContentView(R.layout.activity_actualizar_image_product);

        mimageProducto = findViewById(R.id.imageProductoImage);
        mtxtnombre = findViewById(R.id.txtNombre);

        btnactualizarProd = findViewById(R.id.btnActualizarProduct);
        mExtraProductId = getIntent().getStringExtra("id");

        daoproducto = new ProductoDAO();
        imageDAO = new ImageDAO();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mbuilderSelector = new AlertDialog.Builder(this);
        mbuilderSelector.setTitle("Selecciona una opcion");
        options = new  CharSequence[] {"Imagen de Galeria", "Tomar Foto"};

        mimageProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });

        btnactualizarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActuaizarImagen();
            }
        });
        getProducto();
    }

    private void ActuaizarImagen() {
        if (imageFile !=null){
            GuardarImagen(imageFile);
        }
        else  if(mPhotoFile !=null){
            GuardarImagen(mPhotoFile);
        }
        else {
            Toast.makeText(this, "Debe subir una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateInfo(Producto prod){
        mDialog.show();
        daoproducto.updateProductImage(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(ActualizarImageProductActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(ActualizarImageProductActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProducto(){
        daoproducto.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imageProducto")){
                        String imageProd = documentSnapshot.getString("imageProducto");
                        Picasso.with(ActualizarImageProductActivity.this).load(imageProd).into(mimageProducto);
                    }
                    if (documentSnapshot.contains("nomprod")){
                        String nomproduct = documentSnapshot.getString("nomprod");
                        mtxtnombre.setText(nomproduct.toUpperCase());
                    }
                }
            }
        });
    }

    private void GuardarImagen(File image){
        mDialog.show();
        imageDAO.save(ActualizarImageProductActivity.this, image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    imageDAO.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            Producto prod = new Producto();
                            prod.setNomprod(nomprod);
                            prod.setTipoprod(tipoprod);
                            prod.setPreciocompraprod(preciocompra);
                            prod.setPrecioventaprod(precioventa);
                            prod.setImageProducto(url);
                            prod.setIdprod(mExtraProductId);
                            updateInfo(prod);
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(ActualizarImageProductActivity.this, "La imagen no se pudo almacenar", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    private void selectOptionImage(final int requestCode) {

        mbuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    abrirGaleria(GALERY_REQUEST_CODE);
                }
                else if (i == 1){
                    takePhoto(PHOTO_REQUEST_CODE);
                }
            }
        });

        mbuilderSelector.show();

    }

    private void takePhoto(final int requestCode) {
        Intent takePictureIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            }catch (Exception e){
                Toast.makeText(this, "Hubo un error en tomar la foto " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(ActualizarImageProductActivity.this, "com.example.gm_app", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile() throws IOException {
        File StorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                StorageDir
        );
        mPhotoPath = "file:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private void abrirGaleria(int requestCode) {

        Intent galeryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                imageFile = FileUtil.from(this, data.getData());
                mimageProducto.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));

            }catch (Exception e){
                Log.d("Error", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode ==PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(ActualizarImageProductActivity.this).load(mPhotoPath).into(mimageProducto);
        }
    }
}