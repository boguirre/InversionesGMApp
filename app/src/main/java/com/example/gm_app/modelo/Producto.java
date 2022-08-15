package com.example.gm_app.modelo;

public class Producto {

    private String idprod;
    private String nomprod;
    private String tipoprod;
    private int stockprod;
    private double precioventaprod;
    private double preciocompraprod;
    private String imageProducto;

    public Producto() {
    }

    public Producto(String idprod, String nomprod, String tipoprod, int stockprod, double precioventaprod, double preciocompraprod, String imageProducto) {
        this.idprod = idprod;
        this.nomprod = nomprod;
        this.tipoprod = tipoprod;
        this.stockprod = stockprod;
        this.precioventaprod = precioventaprod;
        this.preciocompraprod = preciocompraprod;
        this.imageProducto = imageProducto;
    }

    public String getImageProducto() {
        return imageProducto;
    }

    public void setImageProducto(String imageProducto) {
        this.imageProducto = imageProducto;
    }

    public String getIdprod() {
        return idprod;
    }

    public void setIdprod(String idprod) {
        this.idprod = idprod;
    }

    public String getNomprod() {
        return nomprod;
    }

    public void setNomprod(String nomprod) {
        this.nomprod = nomprod;
    }

    public String getTipoprod() {
        return tipoprod;
    }

    public void setTipoprod(String tipoprod) {
        this.tipoprod = tipoprod;
    }

    public int getStockprod() {
        return stockprod;
    }

    public void setStockprod(int stockprod) {
        this.stockprod = stockprod;
    }

    public double getPrecioventaprod() {
        return precioventaprod;
    }

    public void setPrecioventaprod(double precioventaprod) {
        this.precioventaprod = precioventaprod;
    }

    public double getPreciocompraprod() {
        return preciocompraprod;
    }

    public void setPreciocompraprod(double preciocompraprod) {
        this.preciocompraprod = preciocompraprod;
    }
}
