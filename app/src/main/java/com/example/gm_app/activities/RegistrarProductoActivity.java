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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class RegistrarProductoActivity extends AppCompatActivity {

    TextInputEditText medtnombreprod;
    TextInputEditText medtpreciocompraprod;
    TextInputEditText medtprecioventaprod;
    ImageView mimageProducto;
    File imageFile;
    private  final int GALERY_REQUEST_CODE = 1;
    private  final int PHOTO_REQUEST_CODE = 2;
    //private int stockprod = 0;
    Spinner mspProd;
    String tipoprop[]={"CELULARES","COVERS","EQUIPOS DE AUDIO"};
    Button btnregistrarProd;

    ProductoDAO daoproducto;
    ImageDAO imageDAO;

    AlertDialog mDialog;
    AlertDialog.Builder mbuilderSelector;
    CharSequence options[];

    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        medtnombreprod = findViewById(R.id.edtnomprod);
        medtpreciocompraprod = findViewById(R.id.edtpreciocompr);
        medtprecioventaprod = findViewById(R.id.edtpreciovent);
        mimageProducto = findViewById(R.id.imageProducto);
        mspProd = findViewById(R.id.spTipoprod);
        ArrayAdapter<String> adapteTip= new ArrayAdapter(this, android.R.layout.simple_list_item_1,tipoprop);
        mspProd.setAdapter(adapteTip);
        btnregistrarProd = findViewById(R.id.btnRegistrarProduct);
        
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
        
        btnregistrarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProducto();
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
                Uri photoUri = FileProvider.getUriForFile(RegistrarProductoActivity.this, "com.example.gm_app", photoFile);
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
            Picasso.with(RegistrarProductoActivity.this).load(mPhotoPath).into(mimageProducto);
        }
    }

    private void saveProducto() {

        String tipoprod =mspProd.getSelectedItem().toString();
        String nombreprod=medtnombreprod.getText().toString();
        String  preciocompreprod=medtpreciocompraprod.getText().toString();
        String precioventaprod=medtprecioventaprod.getText().toString();

        if (!tipoprod.isEmpty() && !nombreprod.isEmpty() && !preciocompreprod.isEmpty() && !precioventaprod.isEmpty() ){
            if (imageFile != null){
                crearProducto(tipoprod,nombreprod,preciocompreprod,precioventaprod, imageFile);
            }
            else if(mPhotoFile != null){
                crearProducto(tipoprod,nombreprod,preciocompreprod,precioventaprod, mPhotoFile);
            }
            else {
                Toast.makeText(this, "Debe seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }


    public void crearProducto(final String tipoprod,  final String nombreprod,  final String preciocompreprod, final String precioventaprod, File image) {
        mDialog.show();
        imageDAO.save(RegistrarProductoActivity.this, image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    imageDAO.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Producto produc=new Producto();
                            int stockprod=0;
                            produc.setTipoprod(tipoprod);
                            produc.setNomprod(nombreprod);
                            produc.setPreciocompraprod(Double.parseDouble(preciocompreprod));
                            produc.setPrecioventaprod(Double.parseDouble(precioventaprod));
                            produc.setStockprod(stockprod);
                            produc.setImageProducto(url);
                            daoproducto.save(produc).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    mDialog.dismiss();
                                    if (taskSave.isSuccessful()){
                                        limpiar();
                                        Toast.makeText(RegistrarProductoActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(RegistrarProductoActivity.this, "No se pudo almacenar", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistrarProductoActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void limpiar() {
        medtprecioventaprod.setText("");
        medtnombreprod.setText("");
        medtpreciocompraprod.setText("");
        medtnombreprod.requestFocus();
        mimageProducto.setImageResource(R.drawable.image_50px);
        mimageProducto = null;
    }
}