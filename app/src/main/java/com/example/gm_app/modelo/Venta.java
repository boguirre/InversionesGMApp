package com.example.gm_app.modelo;

public class Venta {

    private String id;
    private String idprod;
    private String nomproduct;
    private String cliente;
    private double precioventa ;
    private int cantidad;
    private double totalventa;
    private String fecha;

    public Venta() {
    }

    public Venta(String id, String idprod, String nomproduct, String cliente, double precioventa, int cantidad, double totalventa, String fecha) {
        this.id = id;
        this.idprod = idprod;
        this.nomproduct = nomproduct;
        this.cliente = cliente;
        this.precioventa = precioventa;
        this.cantidad = cantidad;
        this.totalventa = totalventa;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdprod() {
        return idprod;
    }

    public void setIdprod(String idprod) {
        this.idprod = idprod;
    }

    public String getNomproduct() {
        return nomproduct;
    }

    public void setNomproduct(String nomproduct) {
        this.nomproduct = nomproduct;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public double getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(double precioventa) {
        this.precioventa = precioventa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotalventa() {
        return totalventa;
    }

    public void setTotalventa(double totalventa) {
        this.totalventa = totalventa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
