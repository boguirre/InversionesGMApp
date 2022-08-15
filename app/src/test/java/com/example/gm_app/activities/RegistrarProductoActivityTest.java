package com.example.gm_app.activities;

import static org.junit.Assert.*;

import com.example.gm_app.Dao.ClienteDAO;
import com.example.gm_app.modelo.Cliente;
import com.example.gm_app.modelo.Producto;
import com.example.gm_app.utils.FileUtil;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class RegistrarProductoActivityTest {

    RegistrarClienteActivity registrarClienteActivity;
    ClienteDAO clienteDAO;
    Cliente cliente;


    @Test
    public void crearClienteTest() {
        registrarClienteActivity = new RegistrarClienteActivity();
        clienteDAO = new ClienteDAO();

        String id = "id";
        String nombre = "Bruno";
        String apellido = "Aguirre";
        String sexo = "M";
        String tipodocu = "DNI";
        String numdocu = "4652125";
        String fechanacim = "02012201";
        String img = "imgae1";

        clienteDAO.save(new Cliente(id,nombre,apellido, sexo, tipodocu, numdocu, fechanacim, img));
        assertEquals("Bruno", cliente.getNombre());
    }
}