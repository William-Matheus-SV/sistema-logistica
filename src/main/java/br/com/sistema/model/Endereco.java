package br.com.sistema.model;

public class Endereco {
    private int id;
    private String rua;
    private int bloco;
    private int nivel;
    private boolean disponivel;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getRua() {return rua;}

    public void setRua(String rua) {this.rua = rua;}

    public int getBloco() { return bloco;}

    public void setBloco(int bloco) {this.bloco = bloco;}

    public int getNivel() {return nivel;}

    public void setNivel(int nivel) {this.nivel = nivel;}

    public boolean isDisponivel() {return disponivel;}

    public void setDisponivel(boolean disponivel) {this.disponivel = disponivel;}

}
