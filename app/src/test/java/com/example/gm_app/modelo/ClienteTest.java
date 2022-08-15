package com.example.gm_app.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClienteTest {

    @Test
    public void getNombre() {
        Cliente c = new Cliente("CLI001", "Bruno", "Aguirre","DNI", "765415", "M", "02012001", "image1");
        assertEquals("Bruno", c.getNombre());
    }

    @Test
    public void setNombre() {
        fail();
    }
}