package com.joel.a0800restinga.Model;

public class CarrosselOffline {
    private int Url;
    private String Descricao;
    private String Telefone;

    public CarrosselOffline(int url, String descricao, String telefone) {
        Url = url;
        Descricao = descricao;
        Telefone = telefone;
    }

    public CarrosselOffline() {

    }

    public int getUrl() {
        return Url;
    }

    public void setUrl(int url) {
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
