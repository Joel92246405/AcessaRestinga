package com.joel.a0800restinga.Model;

public class EstouDoandoModel {

    private String nome;
    private String telefone;
    private String whatsapp;
    private String email;
    private String conteudo;

    public EstouDoandoModel() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public EstouDoandoModel(String nome, String telefone, String whatsapp, String email, String conteudo) {
        this.nome = nome;
        this.telefone = telefone;
        this.whatsapp = whatsapp;
        this.email = email;
        this.conteudo = conteudo;
    }
}
