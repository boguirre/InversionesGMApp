package com.example.gm_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gm_app.Dao.ClienteDAO;
import com.example.gm_app.Dao.ProductoDAO;
import com.example.gm_app.R;
import com.example.gm_app.adapters.ClienteAdapter;
import com.example.gm_app.adapters.ProductoAdapter;
import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Producto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

public class ListClienteActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    FloatingActionButton mfabregisterclient;
    MaterialSearchBar msearchabar;
    RecyclerView mrecyclerview;
    ClienteDAO clienteDAO;
    ClienteAdapter clienteAdapter;
    ClienteAdapter clienteAdapterSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);

        mfabregisterclient = findViewById(R.id.fabregisterCliente);

        msearchabar = findViewById(R.id.searchBarCliente);
        clienteDAO = new ClienteDAO();

        msearchabar.setOnSearchActionListener(this);


        mrecyclerview = findViewById(R.id.recyclervCliente);

        mrecyclerview.setLayoutManager(new GridLayoutManager(ListClienteActivity.this, 2));

        mfabregisterclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegistrarCliente();
            }
        });
    }

    private void searchByName(String name){
        Query query = clienteDAO.getClienteByName(name);
        FirestoreRecyclerOptions<Cliente> options = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();
        clienteAdapterSearch = new ClienteAdapter(options, this);
        clienteAdapterSearch.notifyDataSetChanged();
        mrecyclerview.setAdapter(clienteAdapterSearch);
        clienteAdapterSearch.startListening();
    }

    private void getAllCliente(){
        Query query = clienteDAO.getAll();
        FirestoreRecyclerOptions<Cliente> options = new FirestoreRecyclerOptions.Builder<Cliente>()
                .setQuery(query, Cliente.class)
                .build();
        clienteAdapter = new ClienteAdapter(options, this);
        clienteAdapter.notifyDataSetChanged();
        mrecyclerview.setAdapter(clienteAdapter);
        clienteAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllCliente();

    }


    @Override
    protected void onStop() {
        super.onStop();
        clienteAdapter.startListening();
        if (clienteAdapterSearch != null){
            clienteAdapterSearch.stopListening();
        }
    }

    private void goRegistrarCliente() {
        Intent intent = new Intent(ListClienteActivity.this, RegistrarClienteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllCliente();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchByName(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}