package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Estoque;
import br.com.sistema.model.Produto;
import br.com.sistema.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    // 1. MÉTODO PARA ALOCAR (O coração da sua apresentação)
    public void alocarProduto(Estoque estoque) {
        String sqlEstoque = "INSERT INTO estoque (produto_id, endereco_id, quantidade) VALUES (?, ?, ?)";
        String sqlEndereco = "UPDATE enderecos SET disponivel = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            conn.setAutoCommit(false); // Ativa o controle transacional (ACID)

            try (PreparedStatement pstmEstoque = conn.prepareStatement(sqlEstoque);
                 PreparedStatement pstmEndereco = conn.prepareStatement(sqlEndereco)) {

                // Preenche o INSERT do Estoque
                pstmEstoque.setInt(1, estoque.getProduto().getId());
                pstmEstoque.setInt(2, estoque.getEndereco().getId());
                pstmEstoque.setInt(3, estoque.getQuantidade());
                pstmEstoque.execute();

                // Preenche o UPDATE do Endereço (marca como ocupado/false)
                pstmEndereco.setBoolean(1, false);
                pstmEndereco.setInt(2, estoque.getEndereco().getId());
                pstmEndereco.execute();

                conn.commit(); // Salva as duas operações juntas
                System.out.println("SQL: Transação concluída com sucesso!");

            } catch (Exception e) {
                conn.rollback(); // Se algo falhar, cancela tudo
                throw new RuntimeException("Erro na transação de alocação: " + e.getMessage());
            }
            // Acima temos o conceito apresentado como tudo ou nada do ACID com o Rollback (Atomicidade)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. MÉTODO PARA LISTAR (Para mostrar na sua tabela HTML)
    public List<Estoque> listarGeral() {
        String sql = "SELECT e.id, e.quantidade, p.nome as nome_produto, end.rua, end.bloco " +
                "FROM estoque e " +
                "JOIN produtos p ON e.produto_id = p.id " +
                "JOIN enderecos end ON e.endereco_id = end.id";

        List<Estoque> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Estoque estoque = new Estoque();
                estoque.setId(rset.getInt("id"));
                estoque.setQuantidade(rset.getInt("quantidade"));

                // Estanciando objetos internos para o Produto
                Produto p = new Produto();
                p.setNome(rset.getString("nome_produto"));
                estoque.setProduto(p);
                // Estanciando objetos internos para o Endereço
                Endereco end = new Endereco();
                end.setRua(rset.getString("rua"));
                end.setBloco(rset.getInt("bloco"));
                estoque.setEndereco(end);
                // Adiciona o array lista na tabela estoque com produto + endereço
                lista.add(estoque);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}