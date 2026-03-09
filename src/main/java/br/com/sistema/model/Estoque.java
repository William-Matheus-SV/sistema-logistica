package br.com.sistema.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque")
public class Estoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   // Porque muitos registros de estoque podem se referir a um único produto ou endereço.
    @ManyToOne
    @JoinColumn(name = "id_produto") // Nome da coluna FK no MySQL
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "id_endereco") // Nome da coluna FK no MySQL
    private Endereco endereco;

    private int quantidade;

    public Estoque() {
        this.produto = new Produto();
        this.endereco = new Endereco();
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Produto getProduto() {return produto;}

    public void setProduto(Produto produto) {this.produto = produto;}

    public Endereco getEndereco() {return endereco;}

    public void setEndereco(Endereco endereco) {this.endereco = endereco;}

    public int getQuantidade() {return quantidade;}

    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}
}
