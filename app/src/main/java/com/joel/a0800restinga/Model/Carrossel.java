package com.joel.a0800restinga.Model;

public class Carrossel {
    private String Url;
    private String Descricao;
    private String Telefone;

    public Carrossel(String url, String descricao, String telefone) {
        Url = url;
        Descricao = descricao;
        Telefone = telefone;
    }

    public Carrossel() {

    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }
}
