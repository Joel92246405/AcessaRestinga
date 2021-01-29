package com.joel.a0800restinga.Model;

public class EuAlugoModel {
    private String Nome, Telefone, Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria, Email;
    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public EuAlugoModel(String nome, String telefone, String valor, String whatsApp, String titulo, String ocultarValor, String endereco, String categoria, String email) {
        Nome = nome;
        Telefone = telefone;

        Valor = valor;
        WhatsApp = whatsApp;
        Titulo = titulo;
        OcultarValor = ocultarValor;
        Endereco = endereco;
        Categoria = categoria;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public EuAlugoModel() {

    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }



    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getWhatsApp() {
        return WhatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        WhatsApp = whatsApp;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getOcultarValor() {
        return OcultarValor;
    }

    public void setOcultarValor(String ocultarValor) {
        OcultarValor = ocultarValor;
    }
}
