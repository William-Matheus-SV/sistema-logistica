package br.com.sistema.model;

public class Estoque {
    private int id;
    private Produto produto;
    private Endereco endereco;
    private int quantidade;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public Produto getProduto() {return produto;}

    public void setProduto(Produto produto) {this.produto = produto;}

    public Endereco getEndereco() {return endereco;}

    public void setEndereco(Endereco endereco) {this.endereco = endereco;}

    public int getQuantidade() {return quantidade;}

    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
}
