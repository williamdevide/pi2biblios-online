package br.com.biblios.dao;
// ... (todas as importações necessárias)
import br.com.biblios.model.Emprestimo;
import br.com.biblios.model.Livro;
import br.com.biblios.model.Pessoa;
import br.com.biblios.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class EmprestimoDAO {

    // Lista de colunas permitidas para ordenação, com aliases de tabela
    private static final Set<String> COLUNAS_PERMITIDAS = Set.of(
        "l.nome_livro", "p.nome_pessoa", "emp.data_emprestimo", 
        "emp.data_prevista_entrega", "emp.data_efetiva_entrega"
    );

    public void cadastrar(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (id_pessoa, id_livro, data_emprestimo, data_prevista_entrega, status_entrega, obs_emprestimo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emprestimo.getPessoa().getId());
            stmt.setInt(2, emprestimo.getLivro().getId());
            stmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
            stmt.setDate(4, new java.sql.Date(emprestimo.getDataPrevistaEntrega().getTime()));
            stmt.setString(5, "Em andamento");
            stmt.setString(6, emprestimo.getObsEmprestimo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar empréstimo: ", e);
        }
    }

    public List<Emprestimo> listar(String filtro, String sortField, String sortDir) {
        List<Emprestimo> emprestimos = new ArrayList<>();
        
        // Validação e construção da cláusula ORDER BY
        String orderByClause = " ORDER BY " +
            (COLUNAS_PERMITIDAS.contains(sortField) ? sortField : "emp.data_emprestimo") + " " +
            ("desc".equalsIgnoreCase(sortDir) ? "DESC" : "DESC"); // Padrão DESC para empréstimos

        String sql = "SELECT emp.*, p.nome_pessoa, p.img_pessoa, l.nome_livro, l.img_livro " + // Adicionado img_pessoa e img_livro
             "FROM emprestimo emp " +
             "JOIN pessoa p ON emp.id_pessoa = p.id_pessoa " +
             "JOIN livro l ON emp.id_livro = l.id_livro ";

        switch (filtro) {
            case "abertos": sql += "WHERE emp.data_efetiva_entrega IS NULL "; break;
            case "atrasados": sql += "WHERE emp.data_efetiva_entrega IS NULL AND emp.data_prevista_entrega < CURDATE() "; break;
            case "em_dia": sql += "WHERE emp.data_efetiva_entrega IS NULL AND emp.data_prevista_entrega >= CURDATE() "; break;
            case "finalizados": sql += "WHERE emp.data_efetiva_entrega IS NOT NULL "; break;
        }
        sql += orderByClause;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getInt("id_pessoa"));
                pessoa.setNome(rs.getString("nome_pessoa"));
                pessoa.setImgPessoa(rs.getBytes("img_pessoa"));
                Livro livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setNome(rs.getString("nome_livro"));
                livro.setImgLivro(rs.getBytes("img_livro")); 
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getInt("id_emprestimo"));
                emprestimo.setDataEmprestimo(rs.getDate("data_emprestimo"));
                emprestimo.setDataPrevistaEntrega(rs.getDate("data_prevista_entrega"));
                emprestimo.setDataEfetivaEntrega(rs.getDate("data_efetiva_entrega"));
                emprestimo.setPessoa(pessoa);
                emprestimo.setLivro(livro);
                emprestimo.setObsEmprestimo(rs.getString("obs_emprestimo"));
                emprestimo.setObsDevolucao(rs.getString("obs_devolucao"));
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empréstimos: ", e);
        }
        return emprestimos;
    }

    public void devolver(int idEmprestimo, String obsDevolucao) {
        String sql = "UPDATE emprestimo SET data_efetiva_entrega = CURDATE(), status_entrega = 'Finalizado', obs_devolucao = ? WHERE id_emprestimo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obsDevolucao);
               stmt.setInt(2, idEmprestimo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao devolver livro: ", e);
        }
    }
}