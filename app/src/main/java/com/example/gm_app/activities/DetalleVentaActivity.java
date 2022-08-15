package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.Dao.VentaDAO;
import com.example.gm_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class DetalleVentaActivity extends AppCompatActivity {

    String mExtraVentaId;
    String mExtraVentaIdProd;
    TextView nomproduct;
    TextView nomcliente;
    TextView cantidadventa;
    TextView montototal;
    TextView fechaventa;
    ImageView imgProducto;
    Button btnvolver;
    VentaDAO ventaDAO;
    ProductoDAO productoDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);

        nomproduct = findViewById(R.id.nomProductDetalleVenta);
        nomcliente = findViewById(R.id.nomclientedetalle);
        cantidadventa = findViewById(R.id.cantidadDetalleventa);
        montototal = findViewById(R.id.precioTotalVenta);
        fechaventa = findViewById(R.id.fechaventa);
        btnvolver = findViewById(R.id.btnVolver);
        imgProducto = findViewById(R.id. imageProductoVenta);

        mExtraVentaId = getIntent().getStringExtra("id");
        mExtraVentaIdProd = getIntent().getStringExtra("idprod");
        ventaDAO = new VentaDAO();
        productoDAO = new ProductoDAO();

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getVenta();
        getProducto();

    }

    private void getVenta(){
        ventaDAO.getVentaById(mExtraVentaId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nomproduct")){
                        String nomproducto = documentSnapshot.getString("nomproduct");
                        nomproduct.setText("Producto: " + nomproducto);
                    }

                    if (documentSnapshot.contains("cliente")){
                        String cliente = documentSnapshot.getString("cliente");
                        nomcliente.setText("Cliente: " +cliente);
                    }

                    if (documentSnapshot.contains("cantidad")){
                        double cantidad = documentSnapshot.getDouble("cantidad");
                        cantidadventa.setText("Canitdad: " + cantidad+"");
                    }
                    if (documentSnapshot.contains("totalventa")){
                        double totalventa = documentSnapshot.getDouble("totalventa");
                        montototal.setText("Monto total: " +totalventa+"");
                    }
                    if (documentSnapshot.contains("fecha")){
                        String fecha = documentSnapshot.getString("fecha");
                        fechaventa.setText("Fecha de la Venta: "+fecha);
                    }

                }
            }
        });
    }

    private void getProducto(){
        productoDAO.getProductById(mExtraVentaIdProd).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imageProducto")){
                        String imageProd = documentSnapshot.getString("imageProducto");
                        Picasso.with(DetalleVentaActivity.this).load(imageProd).into(imgProducto);
                    }

                }
            }
        });
    }
}