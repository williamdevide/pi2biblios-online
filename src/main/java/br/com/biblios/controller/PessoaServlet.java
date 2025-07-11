package br.com.biblios.controller;

import br.com.biblios.dao.PessoaDAO;
import br.com.biblios.model.Pessoa;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MultipartConfig
@WebServlet("/pessoas")
public class PessoaServlet extends HttpServlet {
    private PessoaDAO pessoaDAO = new PessoaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) { action = "listar"; }
        switch (action) {
            case "carregarEdicao": carregarEdicao(request, response); break;
            case "inativar": inativarPessoa(request, response); break;
            case "reativar": reativarPessoa(request, response); break;
            case "buscar": buscarPessoas(request, response); break;
            case "carregarCadastro": carregarCadastro(request, response); break;
            default: listarPessoas(request, response); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "atualizar": atualizarPessoa(request, response); break;
                case "cadastrar": cadastrarPessoa(request, response); break;
            }
        }
    }

    private byte[] lerFoto(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null; // Nenhum arquivo foi enviado
        }
        try (InputStream inputStream = filePart.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    private void cadastrarPessoa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        byte[] fotoBytes = lerFoto(request.getPart("foto"));

        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(request.getParameter("nome"));
        novaPessoa.setEmail(request.getParameter("email"));
        novaPessoa.setTelefone(request.getParameter("telefone"));
        novaPessoa.setImgPessoa(fotoBytes);

        try {
            String dataNascimentoStr = request.getParameter("dataNascimento");
            Date dataNascimento = new SimpleDateFormat("yyyy-MM-dd").parse(dataNascimentoStr);
            novaPessoa.setDataNascimento(dataNascimento);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de data inválido", e);
        }

        pessoaDAO.cadastrar(novaPessoa);
        response.sendRedirect("pessoas?action=listar");
    }

    private void atualizarPessoa(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        byte[] fotoBytes = lerFoto(request.getPart("foto"));

        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setNome(request.getParameter("nome"));
        pessoa.setEmail(request.getParameter("email"));
        pessoa.setTelefone(request.getParameter("telefone"));

        if (fotoBytes == null) {
            // Se nenhuma foto nova foi enviada, busca a foto antiga no banco para não apagá-la
            Pessoa pessoaExistente = pessoaDAO.buscarPorId(id);
            pessoa.setImgPessoa(pessoaExistente.getImgPessoa());
        } else {
            // Se uma foto nova foi enviada, usa o novo array de bytes
            pessoa.setImgPessoa(fotoBytes);
        }

        pessoaDAO.atualizar(pessoa);
        response.sendRedirect("pessoas?action=listar");
    }

    // --- MÉTODOS EXISTENTES (sem alterações) ---
    private void listarPessoas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filtroStatus = request.getParameter("filtroStatus");
        if (filtroStatus == null || filtroStatus.isEmpty()) {
            filtroStatus = "ativos"; // Filtro padrão
        }
    
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        if (sortField == null) sortField = "id_pessoa";
        if (sortDir == null) sortDir = "asc";
    
        List<Pessoa> listaPessoas = pessoaDAO.listarTodos(filtroStatus, sortField, sortDir);
    
        request.setAttribute("listaPessoas", listaPessoas);
        request.setAttribute("filtroStatus", filtroStatus); // Envia o filtro de volta para o JSP
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
    
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/pessoa/pessoas.jsp");
        dispatcher.forward(request, response);
    }

    private void carregarEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Pessoa pessoa = pessoaDAO.buscarPorId(id);
        request.setAttribute("pessoa", pessoa);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/pessoa/editar-pessoa.jsp");
        dispatcher.forward(request, response);
    }
    private void inativarPessoa(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Pessoa pessoa = pessoaDAO.buscarPorId(id); // Busca a pessoa para checar o status

        // VALIDAÇÃO DE SEGURANÇA
        if (pessoa != null && pessoa.getEmprestimosEmDia() == 0 && pessoa.getEmprestimosAtrasados() == 0) {
            pessoaDAO.inativar(id);
            request.getSession().setAttribute("mensagem", "Pessoa inativada com sucesso!");
            response.sendRedirect("pessoas?action=listar");
        } else {
            // Se tiver pendências, redireciona para a lista com uma mensagem de erro
            request.getSession().setAttribute("erro", "Ação não permitida: a pessoa possui empréstimos em aberto.");
            response.sendRedirect("pessoas?action=listar");
        }
    }
    private void reativarPessoa(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        pessoaDAO.reativar(id);
        request.getSession().setAttribute("mensagem", "Pessoa reativada com sucesso!");
        response.sendRedirect("pessoas?filtroStatus=inativos"); // Volta para a lista de inativos
    }
    private void carregarCadastro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/pessoa/cadastrar-pessoa.jsp");
        dispatcher.forward(request, response);
    }
    private void buscarPessoas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String termoBusca = request.getParameter("termoBusca");
    
        // Mesma lógica de ler, definir padrão e passar para o DAO e JSP
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        if (sortField == null) sortField = "id_pessoa";
        if (sortDir == null) sortDir = "asc";

        List<Pessoa> listaPessoas = new ArrayList<>();
        try {
            int id = Integer.parseInt(termoBusca);
            // O buscarPorId não tem ordenação, mas a lista de resultado tem só 1 item.
            Pessoa pessoa = pessoaDAO.buscarPorId(id);
            if (pessoa != null) { listaPessoas.add(pessoa); }
        } catch (NumberFormatException e) {
            listaPessoas = pessoaDAO.buscarPorNome(termoBusca, sortField, sortDir);
        }
        
        request.setAttribute("listaPessoas", listaPessoas);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/pessoa/pessoas.jsp");
        dispatcher.forward(request, response);
    }
}