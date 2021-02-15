package com.joel.a0800restinga.Model;

public class FinalizaVendaModel {
    private String CodigoDaEmpresa, Produto, Quantidade, Valor;

    public FinalizaVendaModel() {

    }

    public FinalizaVendaModel(String codigoDaEmpresa, String produto, String quantidade, String valor) {
        CodigoDaEmpresa = codigoDaEmpresa;
        Produto = produto;
        Quantidade = quantidade;
        Valor = valor;
    }

    public String getCodigoDaEmpresa() {
        return CodigoDaEmpresa;
    }

    public void setCodigoDaEmpresa(String codigoDaEmpresa) {
        CodigoDaEmpresa = codigoDaEmpresa;
    }

    public String getProduto() {
        return Produto;
    }

    public void setProduto(String produto) {
        Produto = produto;
    }

    public String getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(String quantidade) {
        Quantidade = quantidade;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }
}
