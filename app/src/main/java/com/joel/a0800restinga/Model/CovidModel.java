package com.joel.a0800restinga.Model;

public class CovidModel {
    private String Titulo, Detalhe, Data, InfoAdd;

    public String getInfoAdd() {
        return InfoAdd;
    }

    public void setInfoAdd(String infoAd) {
        InfoAdd = infoAd;
    }


    public CovidModel() {

    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
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
