package com.example.myapplication;
public class Pessoa {

    private String titulo, data, descricao, local, solucao, foto;
    private int tipo, status, id;


    public Pessoa() {
    }
    public Pessoa(String titulo, String data, String descricao, String local, int tipo, int status) {
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
    }
    public Pessoa(String solucao, String titulo, String data, String descricao, String local, int tipo, int status, int id) {
        this.solucao = solucao;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.id = id;
    }

    public Pessoa(String solucao, String titulo, String data, String descricao, String local, int tipo, int status, int id, String foto) {
        this.solucao = solucao;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.id = id;
        this.foto = foto;
    }

    public Pessoa(String titulo) {
        this.titulo = titulo;
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSolucao(){return solucao;}
    public void setSolucao(String solucao){this.solucao = solucao;}

    public int getId(){return id;}

    public String getFoto(){return foto;}

    public void setFoto(){this.foto = foto;}
}