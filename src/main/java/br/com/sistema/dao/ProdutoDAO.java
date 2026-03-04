package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProdutoDAO {

    public void salvar(Produto produto) {
        // Comando SQL com "?" para evitar SQL Injection (segurança)
        String sql = "INSERT INTO produtos (sku, nome, descricao) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Cria a conexão chamando nosso Connection Factory
            conn = ConnectionFactory.createConnectionToMySQL();

            // Prepara a execução do comando SQL
            pstm = conn.prepareStatement(sql);

            // Substitui os "?" pelos valores do objeto produto
            pstm.setString(1, produto.getSku());
            pstm.setString(2, produto.getNome());
            pstm.setString(3, produto.getDescricao());

            // Executa o comando no banco
            pstm.execute();
            System.out.println("Produto salvo com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
        } finally {
            // Fecha as conexões para não travar o banco
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}