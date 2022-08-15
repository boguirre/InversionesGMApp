package com.example.gm_app.Dao;

import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Proveedor;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class ClienteDAO {

    CollectionReference mcollection;

    public ClienteDAO() {
        mcollection = FirebaseFirestore.getInstance().collection("Cliente");

    }

    public Task<Void> save(Cliente cliente){
        return mcollection.document().set(cliente);
    }

    public Query getAll(){
        return mcollection.orderBy("nombre",Query.Direction.DESCENDING);
    }

    public Query getClienteByName(String name){
        return mcollection.orderBy("nombre").startAt(name).endAt(name+'\uf8ff');
    }

    public Task<DocumentSnapshot> getClienteById(String id){
        return mcollection.document(id).get();
    }

    public Task<Void> updateCliente(Cliente cliente){
        Map<String, Object> map= new HashMap<>();
        map.put("nombre", cliente.getNombre());
        map.put("apellido", cliente.getApellido());
        map.put("fechanacimiento", cliente.getFechanacimiento());
        return mcollection.document(cliente.getId()).update(map);
    }

    public Task<Void> delete(String id){
        return mcollection.document(id).delete();
    }
}
