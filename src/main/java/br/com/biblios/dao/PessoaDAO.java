package br.com.biblios.dao;

import br.com.biblios.model.Pessoa;
import br.com.biblios.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PessoaDAO {

    // Lista de colunas permitidas para ordenação para evitar SQL Injection
    private static final Set<String> COLUNAS_PERMITIDAS = Set.of(
            "id_pessoa", "nome_pessoa", "email_pessoa", "telefone_pessoa", "nasc_pessoa");

    // Método para listar todas as pessoas ativas
    public List<Pessoa> listarTodos(String filtroStatus, String sortField, String sortDir) {
        List<Pessoa> pessoas = new ArrayList<>();
        // Validação e construção da cláusula ORDER BY
        String whereClause = "";
        switch (filtroStatus) {
            case "ativos":
                whereClause = "WHERE p.ativo_pessoa = TRUE ";
                break;
            case "inativos":
                whereClause = "WHERE p.ativo_pessoa = FALSE ";
                break;
            // "todos" não precisa de WHERE
        }

        String orderByClause = " ORDER BY " +
                (COLUNAS_PERMITIDAS.contains(sortField) ? sortField : "id_pessoa") + " " +
                ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");

        final String sql = "SELECT p.*, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NOT NULL THEN 1 ELSE 0 END), 0) AS concluidos, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega >= CURDATE() THEN 1 ELSE 0 END), 0) AS em_dia, "
                +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega < CURDATE() THEN 1 ELSE 0 END), 0) AS atrasados "
                +
                "FROM pessoa p " +
                "LEFT JOIN emprestimo e ON p.id_pessoa = e.id_pessoa " +
                whereClause + // Adiciona o filtro dinâmico aqui
                "GROUP BY p.id_pessoa, p.nome_pessoa, p.email_pessoa, p.telefone_pessoa, p.nasc_pessoa, p.ativo_pessoa, p.img_pessoa"
                +
                orderByClause;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getInt("id_pessoa"));
                pessoa.setNome(rs.getString("nome_pessoa"));
                pessoa.setEmail(rs.getString("email_pessoa"));
                pessoa.setTelefone(rs.getString("telefone_pessoa"));
                pessoa.setDataNascimento(rs.getDate("nasc_pessoa"));
                pessoa.setImgPessoa(rs.getBytes("img_pessoa"));
                pessoa.setAtivo(rs.getBoolean("ativo_pessoa"));
                pessoa.setEmprestimosConcluidos(rs.getInt("concluidos"));
                pessoa.setEmprestimosEmDia(rs.getInt("em_dia"));
                pessoa.setEmprestimosAtrasados(rs.getInt("atrasados"));
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pessoas: ", e);
        }
        return pessoas;
    }

    // Método para buscar uma pessoa pelo ID
    public Pessoa buscarPorId(int id) {
        Pessoa pessoa = null;
        final String sql = "SELECT p.*, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NOT NULL THEN 1 ELSE 0 END), 0) AS concluidos, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega >= CURDATE() THEN 1 ELSE 0 END), 0) AS em_dia, "
                +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega < CURDATE() THEN 1 ELSE 0 END), 0) AS atrasados "
                +
                "FROM pessoa p " +
                "LEFT JOIN emprestimo e ON p.id_pessoa = e.id_pessoa " +
                "WHERE p.id_pessoa = ? " +
                "GROUP BY p.id_pessoa, p.nome_pessoa, p.email_pessoa, p.telefone_pessoa, p.nasc_pessoa, p.ativo_pessoa, p.img_pessoa";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pessoa = new Pessoa();
                    pessoa.setId(rs.getInt("id_pessoa"));
                    pessoa.setNome(rs.getString("nome_pessoa"));
                    pessoa.setEmail(rs.getString("email_pessoa"));
                    pessoa.setTelefone(rs.getString("telefone_pessoa"));
                    pessoa.setDataNascimento(rs.getDate("nasc_pessoa"));
                    pessoa.setAtivo(rs.getBoolean("ativo_pessoa"));
                    pessoa.setImgPessoa(rs.getBytes("img_pessoa"));
                    pessoa.setEmprestimosConcluidos(rs.getInt("concluidos"));
                    pessoa.setEmprestimosEmDia(rs.getInt("em_dia"));
                    pessoa.setEmprestimosAtrasados(rs.getInt("atrasados"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa por ID: ", e);
        }
        return pessoa;
    }

    // Método para atualizar os dados de uma pessoa
    public void atualizar(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET nome_pessoa = ?, email_pessoa = ?, telefone_pessoa = ?, img_pessoa = ? WHERE id_pessoa = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setString(3, pessoa.getTelefone());
            stmt.setBytes(4, pessoa.getImgPessoa()); // <-- ALTERADO PARA setBytes
            stmt.setInt(5, pessoa.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pessoa: ", e);
        }
    }

    // Método para inativar uma pessoa (soft delete)
    public void inativar(int id) {
        String sql = "UPDATE pessoa SET ativo_pessoa = FALSE WHERE id_pessoa = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inativar pessoa: ", e);
        }
    }

    public void reativar(int id) {
        String sql = "UPDATE pessoa SET ativo_pessoa = TRUE WHERE id_pessoa = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reativar pessoa: ", e);
        }
    }

    // Método para buscar pessoas pelo nome (busca parcial)
    public List<Pessoa> buscarPorNome(String nome, String sortField, String sortDir) { // Novos parâmetros
        List<Pessoa> pessoas = new ArrayList<>();

        String orderByClause = " ORDER BY " +
                (COLUNAS_PERMITIDAS.contains(sortField) ? sortField : "id_pessoa") + " " +
                ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC");

        // O SQL usa LIKE com '%' para buscar nomes que contenham o texto digitado
        final String sql = "SELECT p.*, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NOT NULL THEN 1 ELSE 0 END), 0) AS concluidos, " +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega >= CURDATE() THEN 1 ELSE 0 END), 0) AS em_dia, "
                +
                "COALESCE(SUM(CASE WHEN e.data_efetiva_entrega IS NULL AND e.data_prevista_entrega < CURDATE() THEN 1 ELSE 0 END), 0) AS atrasados "
                +
                "FROM pessoa p " +
                "LEFT JOIN emprestimo e ON p.id_pessoa = e.id_pessoa " +
                "WHERE p.nome_pessoa LIKE ? " +
                "GROUP BY p.id_pessoa, p.nome_pessoa, p.email_pessoa, p.telefone_pessoa, p.nasc_pessoa, p.ativo_pessoa, p.img_pessoa"
                +
                orderByClause;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // O '%' é um coringa. %nome% busca qualquer ocorrência do nome.
            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setId(rs.getInt("id_pessoa"));
                    pessoa.setNome(rs.getString("nome_pessoa"));
                    pessoa.setEmail(rs.getString("email_pessoa"));
                    pessoa.setTelefone(rs.getString("telefone_pessoa"));
                    pessoa.setDataNascimento(rs.getDate("nasc_pessoa"));
                    pessoa.setAtivo(rs.getBoolean("ativo_pessoa"));
                    pessoa.setImgPessoa(rs.getBytes("img_pessoa"));
                    pessoa.setEmprestimosConcluidos(rs.getInt("concluidos"));
                    pessoa.setEmprestimosEmDia(rs.getInt("em_dia"));
                    pessoa.setEmprestimosAtrasados(rs.getInt("atrasados"));
                    pessoas.add(pessoa);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoas por nome: ", e);
        }
        return pessoas;
    }

    // Método para cadastrar uma nova pessoa
    public void cadastrar(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome_pessoa, email_pessoa, telefone_pessoa, nasc_pessoa, img_pessoa, ativo_pessoa) VALUES (?, ?, ?, ?, ?, TRUE)";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getEmail());
            stmt.setString(3, pessoa.getTelefone());
            // java.util.Date precisa ser convertido para java.sql.Date para o JDBC
            stmt.setDate(4, new java.sql.Date(pessoa.getDataNascimento().getTime()));
            stmt.setBytes(5, pessoa.getImgPessoa());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar nova pessoa: ", e);
        }
    }

    public List<Pessoa> listarDisponiveis() {
        List<Pessoa> pessoas = new ArrayList<>();
        // Busca apenas pessoas ATIVAS e que NÃO tenham pendências
        String sql = "SELECT p.id_pessoa, p.nome_pessoa, p.img_pessoa FROM pessoa p " +
                "LEFT JOIN emprestimo e ON p.id_pessoa = e.id_pessoa AND e.data_efetiva_entrega IS NULL " +
                "WHERE p.ativo_pessoa = TRUE " +
                "GROUP BY p.id_pessoa " +
                "HAVING COUNT(e.id_emprestimo) = 0 ORDER BY p.nome_pessoa";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getInt("id_pessoa"));
                pessoa.setNome(rs.getString("nome_pessoa"));
                pessoa.setImgPessoa(rs.getBytes("img_pessoa"));
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoas disponíveis: ", e);
        }
        return pessoas;
    }
}