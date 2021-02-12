package com.joel.a0800restinga.Model;

public class Usuario {
    private String login, token, dataDeLogin, situacao;

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Usuario() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDataDeLogin() {
        return dataDeLogin;
    }

    public void setDataDeLogin(String dataDeLogin) {
        this.dataDeLogin = dataDeLogin;
    }

    public Usuario(String login, String token, String dataDeLogin, String situacao) {
        this.login = login;
        this.token = token;
        this.dataDeLogin = dataDeLogin;
        this.situacao = situacao;
    }
}
