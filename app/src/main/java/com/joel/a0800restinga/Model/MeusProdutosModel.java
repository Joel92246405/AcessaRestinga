package com.joel.a0800restinga.Model;

public class MeusProdutosModel {
    private String CodigoDaEmpresa, Nome, Descricao, Valor;

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public MeusProdutosModel() {

    }

    public String getCodigoDaEmpresa() {
        return CodigoDaEmpresa;
    }

    public void setCodigoDaEmpresa(String codigoDaEmpresa) {
        CodigoDaEmpresa = codigoDaEmpresa;
    }

    public MeusProdutosModel(String codigoDaEmpresa, String nome, String descricao, String valor) {
        CodigoDaEmpresa = codigoDaEmpresa;
        Nome = nome;
        Descricao = descricao;
        Valor = valor;
    }
}
