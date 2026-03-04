package br.com.sistema.model;

public class Produto {
    private int id;
    private String sku;
    private String nome;
    private String descricao;

    // Construtor vazio
    public Produto() {}

    // Getters e Setters (Para acessar e modificar os dados)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}