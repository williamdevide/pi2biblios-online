package br.com.biblios.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Lê as informações de conexão das variáveis de ambiente
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            // Verifica se as variáveis foram configuradas no ambiente de deploy
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                // Se não estiverem, usa a configuração local para desenvolvimento
                System.out.println("AVISO: Usando configuração de banco de dados local.");
                return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3307/pi2biblios?useUnicode=true&characterEncoding=UTF-8",
                    "root",
                    ""
                );
            }

            // Usa as variáveis de ambiente para conectar na produção
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado!", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados.", e);
        }
    }
}