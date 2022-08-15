package com.example.gm_app.Dao;

import com.example.gm_app.modelo.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsuarioDAO {

    private CollectionReference mcollection;

    public UsuarioDAO() {
        mcollection = FirebaseFirestore.getInstance().collection("Usuarios");
    }

    public Task<Void> create(Usuario usuario){
        return  mcollection.document(usuario.getId()).set(usuario);
    }

    public Task<Void> update(Usuario usuario){
        Map<String, Object> map = new HashMap<>();
        map.put("telefono", usuario.getTelefono());
        return mcollection.document(usuario.getId()).update(map);
    }
}
