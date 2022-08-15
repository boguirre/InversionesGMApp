package com.example.gm_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.Dao.ProveedorDAO;
import com.example.gm_app.Dao.VentaDAO;
import com.example.gm_app.R;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.example.gm_app.modelo.Venta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrarVentaActivity extends AppCompatActivity {

    TextInputEditText medtnombreprodvent;
    TextInputEditText medtprecioventaprod;
    TextInputEditText medtstockprod;
    TextInputEditText medtcantidadventa;
    ImageView DatePicker;
    TextView txtFechaHoy;
    Button btnRegistrarVenta;
    Spinner mspCliente;
    String mExtraProductId;
    ProductoDAO productoDAO;
    VentaDAO ventaDAO;

    FirebaseFirestore ref;
    CollectionReference clienteRef;
    List<String> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        medtnombreprodvent = findViewById(R.id.edtnomproductventa);
        medtprecioventaprod = findViewById(R.id.edtprecioProductventa);
        medtstockprod = findViewById(R.id.edtstockventa);
        medtcantidadventa = findViewById(R.id.edtcantidadventa);
        DatePicker = findViewById(R.id.btncalendar);
        txtFechaHoy = findViewById(R.id.txtfechaHoy);
        btnRegistrarVenta = findViewById(R.id.btnRegistrarVenta);


        mspCliente = findViewById(R.id.spcliente);

        ref = FirebaseFirestore.getInstance();
        clienteRef = ref.collection("Cliente");
        clientes = new ArrayList<>();

        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();

        inhabilitarCampos();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, clientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspCliente.setAdapter(adapter);

        clienteRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String cliente = document.getString("nombre");
                        clientes.add(cliente);
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });

        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaHoy();
            }
        });

        btnRegistrarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVenta();
            }
        });
        mExtraProductId = getIntent().getStringExtra("id");
        getProducto();
    }

    private void getProducto(){
        productoDAO.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nomprod")){
                        String nomprod = documentSnapshot.getString("nomprod");
                        medtnombreprodvent.setText(nomprod);
                    }
                    if (documentSnapshot.contains("precioventaprod")){
                        double precioventa = documentSnapshot.getDouble("precioventaprod");
                        medtprecioventaprod.setText(precioventa+"");
                    }

                    if (documentSnapshot.contains("stockprod")){
                        double stockprod = documentSnapshot.getDouble("stockprod");
                        medtstockprod.setText(stockprod+"");
                    }

                }
            }
        });
    }

    private void fechaHoy(){
        Calendar cal = Calendar.getInstance();
        int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(RegistrarVentaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = year + "/" + month + "/" + dayOfMonth;
                txtFechaHoy.setText(fecha);
            }
        }, año, mes, dia);
        dpd.show();
    }

    private void inhabilitarCampos(){
        medtnombreprodvent.setEnabled(false);
        medtprecioventaprod.setEnabled(false);
        medtstockprod.setEnabled(false);

    }

    private void saveVenta() {
        //String idprod = "";
        //double preciototal = 0;
        String  nomprod = medtnombreprodvent.getText().toString();
        double precioventa = Double.parseDouble(medtprecioventaprod.getText().toString());
        int cantidad = Integer.parseInt(medtcantidadventa.getText().toString());
        String cliente = mspCliente.getSelectedItem().toString();
        String fecha = txtFechaHoy.getText().toString();

        if (cantidad > 0){
            crearVenta(nomprod,precioventa,cantidad,cliente,fecha);
        }
        else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void crearVenta( final String nomprod, final double precioventa, final int cantidad, final String cliente, final String fecha) {
        double totalventa = cantidad * precioventa;
        Venta vent = new Venta();
        vent.setIdprod(mExtraProductId);
        vent.setNomproduct(nomprod);
        vent.setPrecioventa(precioventa);
        vent.setCantidad(cantidad);
        vent.setCliente(cliente);
        vent.setFecha(fecha);
        vent.setTotalventa(totalventa);
        ventaDAO.save(vent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {
                if (task.isSuccessful()){
                    limpiar();
                    Toast.makeText(RegistrarVentaActivity.this, "La Venta se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrarVentaActivity.this, "No se pudo almacenar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        productoDAO.getProductById(mExtraProductId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("stockprod")){
                         double stockactual = documentSnapshot.getDouble("stockprod");
                         double stockrest = stockactual - cantidad;
                        Producto prod = new Producto();
                        prod.setStockprod((int) (stockrest));
                        prod.setIdprod(mExtraProductId);
                        productoDAO.updateStock(prod);

                    }


                }
            }
        });
    }

    private void limpiar() {
        Intent intent = new Intent(this, ListVentaActivity.class);
        startActivity(intent);
    }

}