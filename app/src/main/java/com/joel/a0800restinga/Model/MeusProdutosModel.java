package com.joel.a0800restinga.Model;

public class MeusProdutosModel {
    private String CodigoDaEmpresa;
    private String CodigoDoProduto;
    private String Relevancia;
    private String Nome;
    private String Descricao;
    private String Valor;

    public String getImagemDoProduto() {
        return ImagemDoProduto;
    }

    public void setImagemDoProduto(String imagemDoProduto) {
        ImagemDoProduto = imagemDoProduto;
    }

    public MeusProdutosModel(String codigoDaEmpresa, String odigoDoProduto, String relevancia, String nome, String descricao, String valor, String imagemDoProduto) {
        CodigoDaEmpresa = codigoDaEmpresa;
        this.CodigoDoProduto = odigoDoProduto;
        Relevancia = relevancia;
        Nome = nome;
        Descricao = descricao;
        Valor = valor;
        ImagemDoProduto = imagemDoProduto;
    }

    private String ImagemDoProduto;

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

    public String getCodigoDoProduto() {
        return CodigoDoProduto;
    }

    public void setCodigoDoProduto(String codigoDoProduto) {
        this.CodigoDoProduto = codigoDoProduto;
    }

    public String getRelevancia() {
        return Relevancia;
    }

    public void setRelevancia(String relevancia) {
        Relevancia = relevancia;
    }

}
