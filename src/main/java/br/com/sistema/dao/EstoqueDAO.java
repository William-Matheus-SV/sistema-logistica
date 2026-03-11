package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Estoque;
import br.com.sistema.model.Produto;
import br.com.sistema.model.Endereco;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EstoqueDAO {
    // Método para Alocar
    public void alocarProduto(Estoque estoque) {
        String sqlEstoque = "INSERT INTO estoque (id_produto, id_endereco, quantidade) VALUES (?, ?, ?)";
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

    // Método para Listar
    public List<Estoque> listarGeral() {
        String sql = "SELECT e.id, e.quantidade, p.sku, p.nome as nome_produto, end.rua, end.bloco, end.nivel " +
                "FROM estoque e " +
                "JOIN produtos p ON e.id_produto = p.id " +
                "JOIN enderecos end ON e.id_endereco = end.id";

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
                end.setNivel(rset.getInt("nivel"));
                estoque.setEndereco(end);
                // Adiciona o array lista na tabela estoque com produto + endereço
                lista.add(estoque);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizarPosicao(int estoqueId, int idEnderecoAntigo, int idEnderecoNovo) {
        String sqlEstoque = "UPDATE estoque SET id_endereco = ? WHERE id = ?";
        String sqlLibera = "UPDATE enderecos SET disponivel = 1 WHERE id = ?";
        String sqlOcupa = "UPDATE enderecos SET disponivel = 0 WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            conn.setAutoCommit(false); // Inicia transação

            try (PreparedStatement p1 = conn.prepareStatement(sqlEstoque);
                 PreparedStatement p2 = conn.prepareStatement(sqlLibera);
                 PreparedStatement p3 = conn.prepareStatement(sqlOcupa)) {

                p1.setInt(1, idEnderecoNovo);
                p1.setInt(2, estoqueId);
                p1.executeUpdate();

                p2.setInt(1, idEnderecoAntigo);
                p2.executeUpdate();

                p3.setInt(1, idEnderecoNovo);
                p3.executeUpdate();

                conn.commit(); // Salva tudo se não der erro
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao remanejar: " + e.getMessage());
        }
    }

    public Estoque buscarPorId(int id) {
        Estoque estoque = null;
        // O JOIN é fundamental para trazer os nomes do produto e da rua/bloco/nível
        String sql = "SELECT e.*, p.nome as nome_produto, p.sku, " +
                "end.rua, end.bloco, end.nivel " +
                "FROM estoque e " +
                "JOIN produtos p ON e.id_produto = p.id " +
                "JOIN enderecos end ON e.id_endereco = end.id " +
                "WHERE e.id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    estoque = new Estoque();
                    estoque.setId(rset.getInt("id"));
                    estoque.setQuantidade(rset.getInt("quantidade"));

                    // Montando o objeto Produto
                    Produto p = new Produto();
                    p.setId(rset.getInt("id_produto"));
                    p.setSku(rset.getString("sku"));
                    p.setNome(rset.getString("nome_produto"));
                    estoque.setProduto(p);

                    // Montando o objeto Endereco
                    Endereco end = new Endereco();
                    end.setId(rset.getInt("id_endereco"));
                    end.setRua(rset.getString("rua"));
                    end.setBloco(rset.getInt("bloco"));
                    end.setNivel(rset.getInt("nivel"));
                    estoque.setEndereco(end);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar registro de estoque: " + e.getMessage());
        }
        return estoque;
    }

    public Estoque buscarPorProdutoId(int idProduto) {
        Estoque estoque = null;
        String sql = "SELECT e.*, end.rua, end.bloco, end.nivel " +
                "FROM estoque e " +
                "JOIN enderecos end ON e.id_endereco = end.id " +
                "WHERE e.id_produto = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, idProduto);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    estoque = new Estoque();
                    estoque.setId(rset.getInt("id"));
                    estoque.setQuantidade(rset.getInt("quantidade"));

                    Endereco end = new Endereco();
                    end.setId(rset.getInt("id_endereco"));
                    end.setRua(rset.getString("rua"));
                    end.setBloco(rset.getInt("bloco"));
                    end.setNivel(rset.getInt("nivel"));
                    estoque.setEndereco(end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return estoque;
    }

    public void atualizarComRemanejamento(Estoque estoque, int idVagaAntiga) {
        String sqlEstoque = "UPDATE estoque SET quantidade = ?, id_endereco = ? WHERE id = ?";
        String sqlLibera = "UPDATE enderecos SET disponivel = 1 WHERE id = ?";
        String sqlOcupa = "UPDATE enderecos SET disponivel = 0 WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            conn.setAutoCommit(false); // Segurança total

            try (PreparedStatement p1 = conn.prepareStatement(sqlEstoque);
                 PreparedStatement p2 = conn.prepareStatement(sqlLibera);
                 PreparedStatement p3 = conn.prepareStatement(sqlOcupa)) {

                // Atualiza o registro do estoque
                p1.setInt(1, estoque.getQuantidade());
                p1.setInt(2, estoque.getEndereco().getId());
                p1.setInt(3, estoque.getId());
                p1.executeUpdate();

                // Libera a vaga de onde o produto saiu
                p2.setInt(1, idVagaAntiga);
                p2.executeUpdate();

                // Ocupa a vaga para onde o produto foi
                p3.setInt(1, estoque.getEndereco().getId());
                p3.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar remanejamento: " + e.getMessage());
        }
    }

    public void excluirPorProdutoId(int idProduto) {
        // Primeiro descobrimos qual vaga ele ocupa para liberá-la
        String sqlBusca = "SELECT id_endereco FROM estoque WHERE id_produto = ?";
        String sqlDelete = "DELETE FROM estoque WHERE id_produto = ?";
        String sqlLiberaVaga = "UPDATE enderecos SET disponivel = 1 WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlBusca);
                 PreparedStatement p2 = conn.prepareStatement(sqlDelete);
                 PreparedStatement p3 = conn.prepareStatement(sqlLiberaVaga)) {

                p1.setInt(1, idProduto);
                ResultSet rs = p1.executeQuery();

                if (rs.next()) {
                    int idVaga = rs.getInt("id_endereco");

                    // Deleta do estoque
                    p2.setInt(1, idProduto);
                    p2.executeUpdate();

                    // Libera o endereço (deixa verde no mapa)
                    p3.setInt(1, idVaga);
                    p3.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}