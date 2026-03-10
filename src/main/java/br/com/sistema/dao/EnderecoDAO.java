package br.com.sistema.dao;

import br.com.sistema.factory.ConnectionFactory;
import br.com.sistema.model.Endereco;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoDAO {

    // Método para listar apenas endereços vazios (Alocação de material)
    public List<Endereco> listarDisponiveis() {
        String sql = "SELECT * FROM enderecos WHERE disponivel = 1";
        List<Endereco> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Endereco e = new Endereco();
                e.setId(rset.getInt("id"));
                e.setRua(rset.getString("rua"));
                e.setBloco(rset.getInt("bloco"));
                e.setNivel(rset.getInt("nivel"));
                e.setDisponivel(rset.getBoolean("disponivel"));
                lista.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Endereco> listarTodos() {
        String sql = "SELECT * FROM enderecos ORDER BY rua ASC, nivel ASC, bloco ASC";
        List<Endereco> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.createConnectionToMySQL();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rset = pstm.executeQuery()) {

            while (rset.next()) {
                Endereco e = new Endereco();
                e.setId(rset.getInt("id"));
                e.setRua(rset.getString("rua"));
                e.setBloco(rset.getInt("bloco"));
                e.setNivel(rset.getInt("nivel"));
                e.setDisponivel(rset.getBoolean("disponivel"));
                lista.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}