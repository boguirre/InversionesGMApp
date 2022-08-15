package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class IngresosStockActivity extends AppCompatActivity {

    String mExtraProductId;
    ProductoDAO  productoDAO;
    ProveedorDAO proveedorDAO;
    ImageView mimageview;
    TextView nomProduct;
    TextInputEditText medtstock;
    Button btnactulizarstock;
    Spinner mspProveedor;
    AlertDialog mDialog;
    private String stockProd = "";
    private double stockactual = 0;

    FirebaseFirestore ref;
    CollectionReference provRef;
    List<String> proveedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos_stock);
        mimageview = findViewById(R.id.imageProductoIng);
        nomProduct = findViewById(R.id.nomProduct);
        medtstock = findViewById(R.id.edtstocking);
        mspProveedor = findViewById(R.id.spProveedor);
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false)
                .build();

        btnactulizarstock = findViewById(R.id.btnActualizarStock);
        productoDAO = new ProductoDAO();
        proveedorDAO = new ProveedorDAO();

        ref = FirebaseFirestore.getInstance();
        provRef = ref.collection("Proveedores");
        proveedores = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, proveedores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspProveedor.setAdapter(adapter);

        provRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String proveedor = document.getString("razonsocial");
                        proveedores.add(proveedor);
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });


        mExtraProductId = getIntent().getStringExtra("id");
        btnactulizarstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarStock();
            }
        });
        getProducto();
    }



    private void limpiar() {
        medtstock.setText("");
    }

    private void updateInfo(Producto prod){
        mDialog.show();
        productoDAO.updateStock(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(IngresosStockActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(IngresosStockActivity.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProducto(){
        productoDAO.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imageProducto")){
                        String imageProd = documentSnapshot.getString("imageProducto");
                        Picasso.with(IngresosStockActivity.this).load(imageProd).into(mimageview);
                    }
                    if (documentSnapshot.contains("nomprod")){
                        String nomproduct = documentSnapshot.getString("nomprod");
                       nomProduct.setText(nomproduct.toUpperCase());
                    }

                }
            }
        });
    }

    private void ActualizarStock() {
        productoDAO.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("stockprod")){
                        stockactual = documentSnapshot.getDouble("stockprod");
                        int stock = Integer.parseInt(medtstock.getText().toString());
                        if (stock > 0 ){
                            Producto prod = new Producto();
                            prod.setStockprod((int) (stock + stockactual));
                            prod.setIdprod(mExtraProductId);
                            updateInfo(prod);
                            limpiar();
                        }
                        else{
                            Producto prod = new Producto();
                            prod.setStockprod((int) stockactual);
                            prod.setIdprod(mExtraProductId);
                            updateInfo(prod);
                            limpiar();
                        }
                    }


                }
            }
        });



    }



}