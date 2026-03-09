package br.com.sistema.model;

import jakarta.persistence.*;

@Entity // Transforma a classe em Entidade de Banco
@Table(name = "enderecos") // Garante que aponte para a tabela correta
public class Endereco {
    @Id // Define que este é o ID (Chave Primária)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Diz que o MySQL que gera o número (Auto_increment)
    private Integer id;
    private String rua;
    private int bloco;
    private int nivel;
    private boolean disponivel;

    public Endereco() {}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getRua() {return rua;}

    public void setRua(String rua) {this.rua = rua;}

    public int getBloco() { return bloco;}

    public void setBloco(int bloco) {this.bloco = bloco;}

    public int getNivel() {return nivel;}

    public void setNivel(int nivel) {this.nivel = nivel;}

    public boolean isDisponivel() {return disponivel;}

    public void setDisponivel(boolean disponivel) {this.disponivel = disponivel;}

}
