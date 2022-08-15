package com.example.gm_app.modelo;

public class Proveedor {

    private String id;
    private String idUser;
    private  String razonsocial;
    private  String ruc;
    private  String telefono;
    private  String direccion;
    private  String tipo;

    public Proveedor() {
    }

    public Proveedor(String id, String idUser, String razonsocial, String ruc, String telefono, String direccion, String tipo) {
        this.id = id;
        this.idUser = idUser;
        this.razonsocial = razonsocial;
        this.ruc = ruc;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
