package com.example.gm_app.Dao;

import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ProveedorDAO {

    CollectionReference mcollection;

    public ProveedorDAO() {
        mcollection = FirebaseFirestore.getInstance().collection("Proveedores");

    }

    public Task<Void> save(Proveedor proveedor){
        return mcollection.document().set(proveedor);
    }

    public Query getAll(){
       return mcollection.orderBy("razonsocial",Query.Direction.DESCENDING);
    }

    public Query getProveedorByName(String name){
        return mcollection.orderBy("razonsocial").startAt(name).endAt(name+'\uf8ff');
    }

    public Task<DocumentSnapshot> getProveedorById(String id){
        return mcollection.document(id).get();
    }



    public Task<Void> updateProveedor(Proveedor prov){
        Map<String, Object> map= new HashMap<>();
        map.put("razonsocial", prov.getRazonsocial());
        map.put("ruc", prov.getRuc());
        map.put("telefono", prov.getTelefono());
        map.put("tipo", prov.getTipo());
        map.put("direccion", prov.getDireccion());
        return mcollection.document(prov.getId()).update(map);
    }

    public Task<Void> delete(String id){
        return mcollection.document(id).delete();
    }
}
