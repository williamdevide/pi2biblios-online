package br.com.biblios.controller;

import br.com.biblios.dao.LivroDAO;
import br.com.biblios.model.Livro;

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
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
@WebServlet("/livros")
public class LivroServlet extends HttpServlet {
    private LivroDAO livroDAO = new LivroDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) { action = "listar"; }
        switch (action) {
            case "carregarEdicao": carregarEdicao(request, response); break;
            case "inativar": inativarLivro(request, response); break;
            case "reativar": reativarLivro(request, response); break;
            case "buscar": buscarLivros(request, response); break;
            case "carregarCadastro": carregarCadastro(request, response); break;
            default: listarLivros(request, response); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "atualizar": atualizarLivro(request, response); break;
                case "cadastrar": cadastrarLivro(request, response); break;
            }
        }
    }

    private byte[] lerFoto(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) { return null; }
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

    private void cadastrarLivro(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Livro novoLivro = new Livro();
        novoLivro.setNome(request.getParameter("nome"));
        novoLivro.setAutor(request.getParameter("autor"));
        novoLivro.setCategoria(request.getParameter("categoria"));
        novoLivro.setImgLivro(lerFoto(request.getPart("capa")));
        livroDAO.cadastrar(novoLivro);
        response.sendRedirect("livros?action=listar");
    }

    private void atualizarLivro(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        byte[] fotoBytes = lerFoto(request.getPart("capa"));
        Livro livro = new Livro();
        livro.setId(id);
        livro.setNome(request.getParameter("nome"));
        livro.setAutor(request.getParameter("autor"));
        livro.setCategoria(request.getParameter("categoria"));
        if (fotoBytes == null) {
            Livro livroExistente = livroDAO.buscarPorId(id);
            livro.setImgLivro(livroExistente.getImgLivro());
        } else {
            livro.setImgLivro(fotoBytes);
        }
        livroDAO.atualizar(livro);
        response.sendRedirect("livros?action=listar");
    }

    private void listarLivros(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filtroStatus = request.getParameter("filtroStatus");
        if (filtroStatus == null || filtroStatus.isEmpty()) {
            filtroStatus = "ativos";
        }
        
        // 1. Lê os parâmetros da URL
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        if (sortField == null) sortField = "id_livro";
        if (sortDir == null) sortDir = "asc";
    
        // 2. Passa para o DAO
        List<Livro> listaLivros = livroDAO.listarTodos(filtroStatus, sortField, sortDir);
        
        // 3. Envia de volta para o JSP
        request.setAttribute("listaLivros", listaLivros);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livro/livros.jsp");
        dispatcher.forward(request, response);
    }
    
    private void carregarEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Livro livro = livroDAO.buscarPorId(id);
        request.setAttribute("livro", livro);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livro/editar-livro.jsp");
        dispatcher.forward(request, response);
    }
  
    private void inativarLivro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Livro livro = livroDAO.buscarPorId(id); // Precisamos buscar o livro para checar o status
    
        // VALIDAÇÃO DE SEGURANÇA
        if (livro != null && "Disponível".equals(livro.getStatusEmprestimo())) {
            livroDAO.inativar(id);
            request.getSession().setAttribute("mensagem", "Livro inativado com sucesso!");
            response.sendRedirect("livros?action=listar");
        } else {
            // Se o livro não está disponível, redireciona para a lista com uma mensagem de erro
            request.getSession().setAttribute("erro", "Ação não permitida: o livro não está disponível.");
            response.sendRedirect("livros?action=listar");
        }
    }

    private void reativarLivro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        livroDAO.reativar(id);
        request.getSession().setAttribute("mensagem", "Livro reativado com sucesso!");
        response.sendRedirect("livros?filtroStatus=inativos");
    }
    
    private void carregarCadastro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livro/cadastrar-livro.jsp");
        dispatcher.forward(request, response);
    }

    private void buscarLivros(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String termoBusca = request.getParameter("termoBusca");
        
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        if (sortField == null) sortField = "id_livro";
        if (sortDir == null) sortDir = "asc";
        
        List<Livro> listaLivros = new ArrayList<>();
        try {
            int id = Integer.parseInt(termoBusca);
            Livro livro = livroDAO.buscarPorId(id);
            if (livro != null) { listaLivros.add(livro); }
        } catch (NumberFormatException e) {
            listaLivros = livroDAO.buscarPorNome(termoBusca, sortField, sortDir);
        }
        
        request.setAttribute("listaLivros", listaLivros);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
    
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/livro/livros.jsp");
        dispatcher.forward(request, response);
    }
}