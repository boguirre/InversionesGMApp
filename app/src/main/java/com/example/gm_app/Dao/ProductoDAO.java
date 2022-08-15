package com.example.gm_app.Dao;

import com.example.gm_app.modelo.Producto;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductoDAO {

    CollectionReference mcollection;

    public ProductoDAO() {
        mcollection = FirebaseFirestore.getInstance().collection("Producto");
    }

    public Task<Void> save(Producto producto){
        return mcollection.document().set(producto);
    }

    public Query getAll(){
        return mcollection.orderBy("nomprod",Query.Direction.DESCENDING);
    }

    public Query getProductByCategoryAndName(String category){
        return mcollection.whereEqualTo("tipoprod", category).orderBy("nomprod",Query.Direction.DESCENDING);
    }

    public Query getProductByName(String name){
        return mcollection.orderBy("nomprod").startAt(name).endAt(name+'\uf8ff');
    }

    public Task<DocumentSnapshot> getProductById(String id){
        return mcollection.document(id).get();
    }

    public Task<Void> updateStock(Producto prod){
        Map<String, Object> map= new HashMap<>();
        map.put("stockprod", prod.getStockprod());
        return mcollection.document(prod.getIdprod()).update(map);
    }

    public Task<Void> updateProduct(Producto prod){
        Map<String, Object> map= new HashMap<>();
        map.put("nomprod", prod.getNomprod());
        map.put("preciocompraprod", prod.getPreciocompraprod());
        map.put("precioventaprod", prod.getPrecioventaprod());
        map.put("tipoprod", prod.getTipoprod());
        return mcollection.document(prod.getIdprod()).update(map);
    }

    public Task<Void> updateProductImage(Producto prod){
        Map<String, Object> map= new HashMap<>();
        map.put("imageProducto", prod.getImageProducto());
        return mcollection.document(prod.getIdprod()).update(map);
    }
    public Task<Void> delete(String id){
        return mcollection.document(id).delete();
    }
}
