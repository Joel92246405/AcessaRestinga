package com.joel.a0800restinga.Model;

public class EmpresaModel {
    private String CodigoDaEmpresaAleatorio;
    private String Nome;

    public EmpresaModel() {

    }

    public String getCodigoDaEmpresaAleatorio() {
        return CodigoDaEmpresaAleatorio;
    }

    public void setCodigoDaEmpresaAleatorio(String codigoDaEmpresaAleatorio) {
        CodigoDaEmpresaAleatorio = codigoDaEmpresaAleatorio;
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

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String geteMailControle() {
        return eMailControle;
    }

    public void seteMailControle(String eMailControle) {
        this.eMailControle = eMailControle;
    }

    public String getImagemLogo() {
        return imagemLogo;
    }

    public void setImagemLogo(String imagemLogo) {
        this.imagemLogo = imagemLogo;
    }

    public EmpresaModel(String codigoDaEmpresaAleatorio, String nome, String telefone, String descricao, String eMailControle, String imagemLogo) {
        CodigoDaEmpresaAleatorio = codigoDaEmpresaAleatorio;
        Nome = nome;
        Telefone = telefone;
        Descricao = descricao;
        this.eMailControle = eMailControle;
        this.imagemLogo = imagemLogo;
    }

    private String Telefone;
    private String Descricao;
    private String eMailControle;
    private String imagemLogo;
}
