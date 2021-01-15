package com.joel.a0800restinga.Model;

public class ContatosAgenda {

    private String nome, telefone;

    public ContatosAgenda(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }
    public ContatosAgenda() {

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
}
