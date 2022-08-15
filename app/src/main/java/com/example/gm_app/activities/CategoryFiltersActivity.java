package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ProductoAdapter;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class CategoryFiltersActivity extends AppCompatActivity {
    String mextraCategory;
    RecyclerView mrecyclerview;
    ProductoDAO daoproducto;
    ProductoAdapter productoAdapterr;

    TextView mtextNumberfilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filters);

        daoproducto = new ProductoDAO();

        mrecyclerview = findViewById(R.id.recyclervCategoria);
        mtextNumberfilter = findViewById(R.id.txtNumberFilter);

        mrecyclerview.setLayoutManager(new GridLayoutManager(CategoryFiltersActivity.this, 2));

        mextraCategory = getIntent().getStringExtra("tipo");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = daoproducto.getProductByCategoryAndName(mextraCategory);
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        productoAdapterr = new ProductoAdapter(options, this, mtextNumberfilter);
        mrecyclerview.setAdapter(productoAdapterr);
        productoAdapterr.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productoAdapterr.startListening();
    }
}