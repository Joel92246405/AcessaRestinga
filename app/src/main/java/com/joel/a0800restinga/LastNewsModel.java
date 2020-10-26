package com.joel.a0800restinga;

import java.util.ArrayList;
import java.util.List;

public class LastNewsModel {
    private String Titulo, Detalhe, Link;


    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }




    public LastNewsModel(String titulo, String detalhe, String link) {
        Titulo = titulo;
        Detalhe = detalhe;
        Link = link;

    }

    public LastNewsModel(String titulo, String detalhe) {
        Titulo = titulo;
        Detalhe = detalhe;
    }

    public LastNewsModel() {

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
