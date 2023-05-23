package com.ramkatom.myappviabla;

import java.util.Date;

public class Mensaje {
    String usuario;
    String texto;
    Date fecha;

    public Mensaje(String usuario, String texto, Date fecha) {
        this.usuario = usuario;
        this.texto = texto;
        this.fecha = fecha;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTexto() {
        return texto;
    }

    public Date getFecha() {
        return fecha;
    }
}
