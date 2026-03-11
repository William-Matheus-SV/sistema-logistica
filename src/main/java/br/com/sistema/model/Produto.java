package br.com.sistema.model;

import jakarta.persistence.Transient;
import jakarta.persistence.*;


@Entity // Transforma a classe em Entidade de Banco
@Table(name = "produtos") // Garante que aponte para a tabela correta
public class Produto {
    @Id // Define que este é o ID (Chave Primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz que o MySQL que gera o número (Auto_increment)
    private Integer id;
    private String sku;
    private String nome;
    private String descricao;

    // Construtor vazio
    public Produto() {}
    @Transient
    private Integer quantidadeInicial;

    // Getters e Setters (Para acessar e modificar os dados)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {this.id = id; }

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

    public Integer getQuantidadeInicial() { return quantidadeInicial; }
    public void setQuantidadeInicial(Integer quantidadeInicial) { this.quantidadeInicial = quantidadeInicial; }
}