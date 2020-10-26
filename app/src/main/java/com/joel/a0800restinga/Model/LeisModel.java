package com.joel.a0800restinga.Model;

public class LeisModel {
    private String Titulo;
    private String Detalhe;

    public LeisModel(String titulo, String detalhe) {
        Titulo = titulo;
        Detalhe = detalhe;
    }

    public LeisModel() {

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
