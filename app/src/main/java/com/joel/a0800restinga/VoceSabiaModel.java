package com.joel.a0800restinga;

public class VoceSabiaModel {
    private String Titulo;
    private String Detalhe;

    public VoceSabiaModel(String titulo, String detalhe) {
        Titulo = titulo;
        Detalhe = detalhe;
    }

    public VoceSabiaModel() {

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
