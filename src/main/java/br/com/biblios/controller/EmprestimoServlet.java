package br.com.biblios.controller;

import br.com.biblios.dao.EmprestimoDAO;
import br.com.biblios.dao.LivroDAO;
import br.com.biblios.dao.PessoaDAO;
import br.com.biblios.model.Emprestimo;
import br.com.biblios.model.Livro;
import br.com.biblios.model.Pessoa;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
// import java.util.HashSet;

@WebServlet("/emprestimos")
public class EmprestimoServlet extends HttpServlet {
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
    private PessoaDAO pessoaDAO = new PessoaDAO();
    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) { action = "listar"; }

        switch (action) {
            case "carregarCadastro": carregarFormularioInicial(request, response); break;
            case "selecionarPessoa": carregarFormularioComPessoa(request, response); break;
            case "adicionarLivro": adicionarLivroAoCarrinho(request, response); break;
            case "removerLivro": removerLivroDoCarrinho(request, response); break;
            // case "devolver": devolverLivro(request, response); break;
            default: listarEmprestimos(request, response); break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if ("cadastrar".equals(action)) {
            cadastrarEmprestimo(request, response);
        } else if ("devolver".equals(action)) {
            devolverLivro(request, response);
        }
    }

    private void listarEmprestimos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lê os parâmetros da URL
        String filtro = request.getParameter("filtro");
        if (filtro == null || filtro.isEmpty()) { filtro = "abertos"; }
        
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        if (sortField == null) sortField = "emp.data_emprestimo";
        if (sortDir == null) sortDir = "desc";
    
        // 2. Passa todos os parâmetros para o DAO
        List<Emprestimo> listaEmprestimos = emprestimoDAO.listar(filtro, sortField, sortDir);
        
        // 3. Envia tudo de volta para o JSP
        request.setAttribute("listaEmprestimos", listaEmprestimos);
        request.setAttribute("hoje", new Date());
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprestimo/emprestimos.jsp");
        dispatcher.forward(request, response);
    }

    private void carregarFormularioInicial(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Limpa qualquer carrinho de livros existente ao iniciar um novo empréstimo
        request.getSession().removeAttribute("carrinhoDeLivros");
        
        List<Pessoa> pessoasDisponiveis = pessoaDAO.listarDisponiveis();
        request.setAttribute("listaPessoasDisponiveis", pessoasDisponiveis);
        
        LocalDate hoje = LocalDate.now();
        LocalDate dataDevolucaoPadrao = hoje.plusDays(7);
        request.setAttribute("dataEmprestimo", hoje.toString());
        request.setAttribute("dataDevolucao", dataDevolucaoPadrao.toString());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprestimo/cadastrar-emprestimo.jsp");
        dispatcher.forward(request, response);
    }

    private void adicionarLivroAoCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Pega o carrinho da sessão ou cria um novo se não existir
        @SuppressWarnings("unchecked")
        List<Livro> carrinho = (List<Livro>) request.getSession().getAttribute("carrinhoDeLivros");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        // Adiciona o novo livro ao carrinho
        int livroId = Integer.parseInt(request.getParameter("livroId"));
        Livro livro = livroDAO.buscarPorId(livroId);
        carrinho.add(livro);

        // Guarda o carrinho atualizado na sessão
        request.getSession().setAttribute("carrinhoDeLivros", carrinho);
        
        // Recarrega a página no estado de "pessoa selecionada"
        carregarFormularioComPessoa(request, response);
    }
    
    private void removerLivroDoCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        @SuppressWarnings("unchecked")
        List<Livro> carrinho = (List<Livro>) request.getSession().getAttribute("carrinhoDeLivros");
        if (carrinho != null) {
            int livroIdToRemove = Integer.parseInt(request.getParameter("livroId"));
            // Remove o livro da lista usando uma expressão lambda
            carrinho.removeIf(livro -> livro.getId() == livroIdToRemove);
            request.getSession().setAttribute("carrinhoDeLivros", carrinho);
        }
        carregarFormularioComPessoa(request, response);
    }
    
    private void carregarFormularioComPessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pega os dados da requisição
        int pessoaId = Integer.parseInt(request.getParameter("pessoaId"));
        String dataEmprestimo = request.getParameter("dataEmprestimo");
        String dataDevolucao = request.getParameter("dataDevolucao");
        
        // Pega o carrinho da sessão
        @SuppressWarnings("unchecked")
        List<Livro> carrinho = (List<Livro>) request.getSession().getAttribute("carrinhoDeLivros");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
    
        // --- LÓGICA CORRIGIDA ---
        // 1. Cria um Set com os IDs dos livros que já estão no carrinho.
        Set<Integer> carrinhoIds = carrinho.stream()
                                           .map(Livro::getId)
                                           .collect(Collectors.toSet());
    
        Pessoa pessoaSelecionada = pessoaDAO.buscarPorId(pessoaId);
        List<Livro> livrosDisponiveis = livroDAO.listarDisponiveis();
        
        // Envia os dados de volta para a página, incluindo o novo Set de IDs
        request.setAttribute("pessoaSelecionada", pessoaSelecionada);
        request.setAttribute("listaLivrosDisponiveis", livrosDisponiveis);
        request.setAttribute("dataEmprestimo", dataEmprestimo);
        request.setAttribute("dataDevolucao", dataDevolucao);
        request.setAttribute("carrinhoIds", carrinhoIds); // <-- NOVO ATRIBUTO ENVIADO PARA O JSP
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/emprestimo/cadastrar-emprestimo.jsp");
        dispatcher.forward(request, response);
    }

    private void cadastrarEmprestimo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int pessoaId = Integer.parseInt(request.getParameter("pessoaId"));
            String obsEmprestimo = request.getParameter("obsEmprestimo");
            String dataDevolucaoStr = request.getParameter("dataDevolucao");
            Date dataDevolucao = new SimpleDateFormat("yyyy-MM-dd").parse(dataDevolucaoStr);

            Pessoa pessoa = new Pessoa();
            pessoa.setId(pessoaId);
            
            // Pega a lista final de livros da sessão
            @SuppressWarnings("unchecked")
            List<Livro> carrinho = (List<Livro>) request.getSession().getAttribute("carrinhoDeLivros");

            if (carrinho != null && !carrinho.isEmpty()) {
                // Para cada livro no carrinho, cria um registro de empréstimo separado
                for (Livro livro : carrinho) {
                    Emprestimo emprestimo = new Emprestimo();
                    emprestimo.setPessoa(pessoa);
                    emprestimo.setLivro(livro);
                    emprestimo.setDataEmprestimo(new Date());
                    emprestimo.setDataPrevistaEntrega(dataDevolucao);
                    emprestimo.setObsEmprestimo(obsEmprestimo);
                    
                    emprestimoDAO.cadastrar(emprestimo);
                }
            }
            
            // Limpa o carrinho após o sucesso e redireciona
            request.getSession().removeAttribute("carrinhoDeLivros");
            response.sendRedirect("emprestimos?filtro=abertos");

        } catch (NumberFormatException | ParseException e) {
            response.sendRedirect("emprestimos?action=carregarCadastro&erro=dadosInvalidos");
        }
    }

    private void devolverLivro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int idEmprestimo = Integer.parseInt(request.getParameter("idEmprestimo"));
        String obsDevolucao = request.getParameter("obsDevolucao");
        emprestimoDAO.devolver(idEmprestimo, obsDevolucao);
        response.sendRedirect("emprestimos?filtro=abertos");
    }
}