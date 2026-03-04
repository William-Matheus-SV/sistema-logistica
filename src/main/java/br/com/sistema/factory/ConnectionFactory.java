package br.com.sistema.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Configurações do XAMPP
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; //
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/sistema_logistica";

    public static Connection createConnectionToMySQL() throws Exception {
        // Faz a conexão com o banco de dados
        Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        return connection;
    }

    public static void main(String[] args) {
        // Teste simples para ver se conectou
        try {
            Connection con = createConnectionToMySQL();
            if (con != null) {
                System.out.println("Conexão obtida com sucesso!");
                con.close();
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}