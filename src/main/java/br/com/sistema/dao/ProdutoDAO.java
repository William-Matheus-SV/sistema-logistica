package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Produto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {
    //Aplicando o CRUD na aplicação
    //Salvar -> Create
    public void salvar(Produto produto) {
            // Comando SQL com "?" para evitar SQL Injection (segurança)
            String sql = "INSERT INTO produtos (sku, nome, descricao) VALUES (?, ?, ?)";

            try (Connection conn = ConnectionFactory.createConnectionToMySQL();
                 PreparedStatement pstm = conn.prepareStatement(sql)) {

                pstm.setString(1, produto.getSku());
                pstm.setString(2, produto.getNome());
                pstm.setString(3, produto.getDescricao());

                pstm.execute();
                System.out.println("Produto salvo com sucesso!");

            } catch (Exception e) {
                System.out.println("Erro ao salvar produto: " + e.getMessage());
            }
        }
    //Listar -> Read
    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM produtos";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Produto p = new Produto();
                p.setId((Integer) rset.getObject("id"));
                p.setSku(rset.getString("sku"));
                p.setNome(rset.getString("nome"));
                p.setDescricao(rset.getString("descricao"));
                produtos.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }
    public List<Produto> listarNaoAlocados() {
        List<Produto> lista = new ArrayList<>();
        // O segredo está nesta query SQL
        String sql = "SELECT * FROM produtos WHERE id NOT IN (SELECT id_produto FROM estoque)";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Produto p = new Produto();
                p.setId(rset.getInt("id"));
                p.setSku(rset.getString("sku"));
                p.setNome(rset.getString("nome"));
                p.setDescricao(rset.getString("descricao"));

                lista.add(p);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao filtrar produtos não alocados: " + e.getMessage());
        }
        return lista;
    }
    // Buscar um único produto pelo ID para carregar na tela de edição
    public Produto buscarPorId(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        Produto p = null;

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            // Usamos setObject para manter o padrão de segurança com Integer
            pstm.setObject(1, id);

            try (ResultSet rset = pstm.executeQuery()) {
                if (rset.next()) {
                    p = new Produto();
                    p.setId((Integer) rset.getObject("id"));
                    p.setSku(rset.getString("sku"));
                    p.setNome(rset.getString("nome"));
                    p.setDescricao(rset.getString("descricao"));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return p;
    }
    //Atualizar -> Update
    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET sku = ?, nome = ?, descricao = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            // Preenchendo os valores para o SQL
            pstm.setString(1, produto.getSku());
            pstm.setString(2, produto.getNome());
            pstm.setString(3, produto.getDescricao());

            // O ID vai por último porque é o critério do WHERE
            pstm.setObject(4, produto.getId()); // O ID diz ao MySQL qual registro será alterado

            pstm.execute();
            System.out.println("Produto ID " + produto.getId() + " atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }
    //Deletar -> Delete
    public void excluir(Integer id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setObject(1, id);

            pstm.execute();
            System.out.println("Produto ID " + id + " removido com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
    }
}