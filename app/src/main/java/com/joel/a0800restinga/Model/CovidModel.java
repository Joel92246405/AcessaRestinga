package com.joel.a0800restinga.Model;

public class CovidModel {
    private String Titulo, Detalhe, Data;



    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public CovidModel(String titulo, String detalhe, String data) {
        Titulo = titulo;
        Detalhe = detalhe;
        Data = data;
    }

    public CovidModel(String titulo, String detalhe) {
        Titulo = titulo;
        Detalhe = detalhe;
    }

    public CovidModel() {

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
