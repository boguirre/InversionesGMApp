package com.example.gm_app.Dao;

import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Venta;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VentaDAO {

    CollectionReference mcollection;

    public VentaDAO() {
        mcollection = FirebaseFirestore.getInstance().collection("Ventas");
    }

    public Task<Void> save(Venta venta){
        return mcollection.document().set(venta);
    }

    public Query getAll(){
        return mcollection.orderBy("fecha",Query.Direction.DESCENDING);
    }

    public Query getVentaByFecha(String fecha){
        return mcollection.orderBy("fecha").startAt(fecha).endAt(fecha+'\uf8ff');
    }

    public Task<DocumentSnapshot> getVentaById(String id){
        return mcollection.document(id).get();
    }

}
