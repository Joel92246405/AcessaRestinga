package com.joel.a0800restinga.Model;

public class SaudePublicaModel {

    String titulo, horarios;

    public SaudePublicaModel(String titulo, String horarios) {
        this.titulo = titulo;
        this.horarios = horarios;
    }

    public SaudePublicaModel() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }
}
