package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Estoque;
import br.com.sistema.model.Produto;
import br.com.sistema.model.Endereco;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EstoqueDAO {
    // Método para Alocar
    public void alocarProduto(Estoque estoque) {
        String sqlEstoque = "INSERT INTO estoque (produto_id, endereco_id, quantidade) VALUES (?, ?, ?)";
        String sqlEndereco = "UPDATE enderecos SET disponivel = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            conn.setAutoCommit(false); // Ativa o controle transacional (ACID)

            try (PreparedStatement pstmEstoque = conn.prepareStatement(sqlEstoque);
                 PreparedStatement pstmEndereco = conn.prepareStatement(sqlEndereco)) {

                // Preenche o INSERT do Estoque
                pstmEstoque.setObject(1, estoque.getProduto().getId());
                pstmEstoque.setObject(2, estoque.getEndereco().getId());
                pstmEstoque.setInt(3, estoque.getQuantidade());
                pstmEstoque.execute();

                // Preenche o UPDATE do Endereço (marca como ocupado/false)
                pstmEndereco.setBoolean(1, false);
                pstmEndereco.setObject(2, estoque.getEndereco().getId());
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

    // Método para Listar
    public List<Estoque> listarGeral() {
        String sql = "SELECT e.id, e.quantidade, p.sku, p.nome as nome_produto, end.rua, end.bloco " +
                "FROM estoque e " +
                "JOIN produtos p ON e.produto_id = p.id " +
                "JOIN enderecos end ON e.endereco_id = end.id";

        List<Estoque> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Estoque estoque = new Estoque();
                estoque.setId((Integer) rset.getObject("id"));
                estoque.setQuantidade(rset.getInt("quantidade"));

                // Estanciando objetos internos para o Produto
                Produto p = new Produto();
                p.setNome(rset.getString("nome_produto"));
                p.setSku(rset.getString("sku"));
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

//Criar classe para Criar e Editar endereços pelo Estoque