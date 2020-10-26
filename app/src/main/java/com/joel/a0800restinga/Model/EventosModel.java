package com.joel.a0800restinga.Model;

public class EventosModel {
    private String Titulo, Detalhe, Link, Data;

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public EventosModel(String titulo, String detalhe, String link, String data) {
        Titulo = titulo;
        Detalhe = detalhe;
        Link = link;
        Data = data;
    }

    public EventosModel(String titulo, String detalhe) {
        Titulo = titulo;
        Detalhe = detalhe;
    }

    public EventosModel() {

    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDetalhe() {
        return Detalhe;
    }

    public void setDetalhe(String detalhe) {
        Detalhe = detalhe;
    }
}
