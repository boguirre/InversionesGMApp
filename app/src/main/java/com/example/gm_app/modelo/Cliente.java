package com.example.gm_app.modelo;

public class Cliente {

    private String id;
    private String nombre;
    private String apellido;
    private String tipodocumento;
    private String numdocumento;
    private String sexo;
    private String fechanacimiento;
    private String imageCliente;

    public Cliente() {
    }

    public Cliente(String id, String nombre, String apellido, String tipodocumento, String numdocumento, String sexo, String fechanacimiento, String imageCliente) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipodocumento = tipodocumento;
        this.numdocumento = numdocumento;
        this.sexo = sexo;
        this.fechanacimiento = fechanacimiento;
        this.imageCliente = imageCliente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getNumdocumento() {
        return numdocumento;
    }

    public void setNumdocumento(String numdocumento) {
        this.numdocumento = numdocumento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public String getImageCliente() {
        return imageCliente;
    }

    public void setImageCliente(String imageCliente) {
        this.imageCliente = imageCliente;
    }
}
