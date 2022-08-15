package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.IngresoAdapter;
import com.example.gm_app.adapters.ProductoAdapter;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class IngresosActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{

    RecyclerView mrecyclerview;
    MaterialSearchBar msearchabar;
    ProductoDAO daoproducto;
    IngresoAdapter ingresoAdapter;
    IngresoAdapter ingresoAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);
        msearchabar = findViewById(R.id.searchBarIngresos);

        daoproducto = new ProductoDAO();

        msearchabar.setOnSearchActionListener(this);

        mrecyclerview = findViewById(R.id.recyclervProductoIngresos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(linearLayoutManager);


    }

    private void searchByTitle(String name){
        Query query = daoproducto.getProductByName(name);
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        ingresoAdapterSearch = new IngresoAdapter(options, this);
        ingresoAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(ingresoAdapterSearch);
        ingresoAdapterSearch.startListening();
    }

    private void getAllIngresos(){
        Query query = daoproducto.getAll();
        FirestoreRecyclerOptions<Producto> options = new FirestoreRecyclerOptions.Builder<Producto>()
                .setQuery(query, Producto.class)
                .build();
        ingresoAdapter = new IngresoAdapter(options, this);
        ingresoAdapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(ingresoAdapter);
        ingresoAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllIngresos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ingresoAdapter.startListening();
        if (ingresoAdapterSearch != null){
            ingresoAdapterSearch.stopListening();
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllIngresos();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByTitle(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}