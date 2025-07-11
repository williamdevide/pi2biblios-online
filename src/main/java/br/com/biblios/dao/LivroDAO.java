package br.com.biblios.dao;

import br.com.biblios.model.Livro;
import br.com.biblios.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LivroDAO {
    
    // Lista de colunas permitidas para ordenação para evitar SQL Injection
    private static final Set<String> COLUNAS_PERMITIDAS = Set.of(
        "id_livro", "nome_livro", "autor_livro", "categoria_livro", "status_emprestimo"
    );

    public List<Livro> listarTodos(String filtroStatus, String sortField, String sortDir) {
        List<Livro> livros = new ArrayList<>();
        
        String whereClause = "";
        switch (filtroStatus) {
            case "ativos":
                whereClause = "WHERE l.ativo_livro = TRUE ";
                break;
            case "inativos":
                whereClause = "WHERE l.ativo_livro = FALSE ";
                break;
        }

        String orderByClause = " ORDER BY " +
            (COLUNAS_PERMITIDAS.contains(sortField) ? sortField : "id_livro") + " " +
            ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");
            
        final String sql = "SELECT l.*, " +
                       "CASE " +
                       "    WHEN e.id_emprestimo IS NULL THEN 'Disponível' " +
                       "    WHEN e.data_prevista_entrega >= CURDATE() THEN 'Emprestado (Em dia)' " +
                       "    ELSE 'Emprestado (Atrasado)' " +
                       "END AS status_emprestimo " +
                       "FROM livro l " +
                       "LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.data_efetiva_entrega IS NULL " +
                       whereClause + 
                       "GROUP BY l.id_livro, l.nome_livro, l.autor_livro, l.categoria_livro, l.ativo_livro, l.img_livro " +
                       orderByClause;
    
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setNome(rs.getString("nome_livro"));
                livro.setAutor(rs.getString("autor_livro"));
                livro.setCategoria(rs.getString("categoria_livro"));
                livro.setAtivo(rs.getBoolean("ativo_livro"));
                livro.setImgLivro(rs.getBytes("img_livro"));
                livro.setStatusEmprestimo(rs.getString("status_emprestimo"));
                livros.add(livro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros com status: ", e);
        }
        return livros;
    }

    public Livro buscarPorId(int id) {
        Livro livro = null;
        final String sql = "SELECT l.*, " +
                        "CASE " +
                        "    WHEN e.id_emprestimo IS NULL THEN 'Disponível' " +
                        "    WHEN e.data_prevista_entrega >= CURDATE() THEN 'Emprestado (Em dia)' " +
                        "    ELSE 'Emprestado (Atrasado)' " +
                        "END AS status_emprestimo " +
                        "FROM livro l " +
                        "LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.data_efetiva_entrega IS NULL " +
                        "WHERE l.id_livro = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    livro = new Livro();
                    livro.setId(rs.getInt("id_livro"));
                    livro.setNome(rs.getString("nome_livro"));
                    livro.setAutor(rs.getString("autor_livro"));
                    livro.setCategoria(rs.getString("categoria_livro"));
                    livro.setAtivo(rs.getBoolean("ativo_livro"));
                    livro.setImgLivro(rs.getBytes("img_livro"));
                    livro.setStatusEmprestimo(rs.getString("status_emprestimo"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID com status: ", e);
        }
        return livro;
    }

    
    public List<Livro> buscarPorNome(String nome, String sortField, String sortDir) {
        List<Livro> livros = new ArrayList<>();
        
        String orderByClause = " ORDER BY " +
            (COLUNAS_PERMITIDAS.contains(sortField) ? sortField : "id_livro") + " " +
            ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");

        final String sql = "SELECT l.*, " +
                           "CASE " +
                           "    WHEN e.id_emprestimo IS NULL THEN 'Disponível' " +
                           "    WHEN e.data_prevista_entrega >= CURDATE() THEN 'Emprestado (Em dia)' " +
                           "    ELSE 'Emprestado (Atrasado)' " +
                           "END AS status_emprestimo " +
                           "FROM livro l " +
                           "LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.data_efetiva_entrega IS NULL " +
                           "WHERE l.nome_livro LIKE ?" +
                            orderByClause;
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getInt("id_livro"));
                    livro.setNome(rs.getString("nome_livro"));
                    livro.setAutor(rs.getString("autor_livro"));
                    livro.setCategoria(rs.getString("categoria_livro"));
                    livro.setAtivo(rs.getBoolean("ativo_livro"));
                    livro.setImgLivro(rs.getBytes("img_livro"));
                    livro.setStatusEmprestimo(rs.getString("status_emprestimo"));
                    livros.add(livro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros por nome com status: ", e);
        }
        return livros;
    }

    public void cadastrar(Livro livro) {
        String sql = "INSERT INTO livro (nome_livro, autor_livro, categoria_livro, img_livro, ativo_livro) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getNome());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getCategoria());
            stmt.setBytes(4, livro.getImgLivro());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar novo livro: ", e);
        }
    }

    public void atualizar(Livro livro) {
        String sql = "UPDATE livro SET nome_livro = ?, autor_livro = ?, categoria_livro = ?, img_livro = ? WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, livro.getNome());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getCategoria());
            stmt.setBytes(4, livro.getImgLivro());
            stmt.setInt(5, livro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: ", e);
        }
    }

    public void inativar(int id) {
        String sql = "UPDATE livro SET ativo_livro = FALSE WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inativar livro: ", e);
        }
    }

    public void reativar(int id) {
        String sql = "UPDATE livro SET ativo_livro = TRUE WHERE id_livro = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reativar livro: ", e);
        }
    }

    public List<Livro> listarDisponiveis() {
        List<Livro> livros = new ArrayList<>();
        // Busca apenas livros ATIVOS e DISPONÍVEIS (sem empréstimo ativo)
        String sql = "SELECT l.id_livro, l.nome_livro, l.img_livro FROM livro l " +
                 "LEFT JOIN emprestimo e ON l.id_livro = e.id_livro AND e.data_efetiva_entrega IS NULL " +
                 "WHERE l.ativo_livro = TRUE AND e.id_emprestimo IS NULL ORDER BY l.nome_livro";
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setNome(rs.getString("nome_livro"));
                livro.setImgLivro(rs.getBytes("img_livro"));
                livros.add(livro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros disponíveis: ", e);
        }
        return livros;
    }
}