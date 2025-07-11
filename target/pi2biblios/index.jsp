<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/views/templates/header.jsp" />

<%-- O conteúdo principal da página inicial começa aqui --%>
<div class="container mt-4">

    <%-- Painel de Boas-Vindas (Jumbotron) --%>
    <div class="p-5 mb-4 bg-light rounded-3 shadow-sm">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">Bem-vindo ao BibliOS!</h1>
            <p class="col-md-8 fs-4">Sua plataforma completa para gerenciamento de bibliotecas. Use os cartões abaixo para navegar de forma rápida e intuitiva pelas principais funcionalidades do sistema.</p>
        </div>
    </div>

    <%-- Grade de Cards com os Recursos --%>
    <%-- A classe 'row-cols-md-2' garante que teremos 2 cards por linha em telas médias ou maiores --%>
    <div class="row row-cols-1 row-cols-md-2 g-4">

        <div class="col">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center p-4">
                    <i class="bi bi-people-fill text-primary" style="font-size: 3rem;"></i>
                    <h5 class="card-title mt-3">Gerenciar Pessoas</h5>
                    <p class="card-text">Cadastre novos leitores, edite informações, consulte o status de empréstimos e gerencie o acesso à biblioteca.</p>
                </div>
                <div class="card-footer bg-white border-0 p-3">
                    <a href="${pageContext.request.contextPath}/pessoas?action=listar" class="btn btn-primary w-100">Acessar Pessoas</a>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center p-4">
                    <i class="bi bi-book-half text-success" style="font-size: 3rem;"></i>
                    <h5 class="card-title mt-3">Gerenciar Livros</h5>
                    <p class="card-text">Adicione novos títulos ao acervo, atualize detalhes, faça upload de capas e verifique a disponibilidade de cada livro.</p>
                </div>
                <div class="card-footer bg-white border-0 p-3">
                    <a href="${pageContext.request.contextPath}/livros?action=listar" class="btn btn-success w-100">Acessar Livros</a>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="card h-100 shadow-sm">
                <div class="card-body text-center p-4">
                    <i class="bi bi-arrow-left-right text-warning" style="font-size: 3rem;"></i>
                    <h5 class="card-title mt-3">Gerenciar Empréstimos</h5>
                    <p class="card-text">Realize novos empréstimos, adicione múltiplos livros, registre devoluções e consulte o histórico completo de movimentações.</p>
                </div>
                <div class="card-footer bg-white border-0 p-3">
                    <a href="${pageContext.request.contextPath}/emprestimos?action=listar" class="btn btn-warning text-dark w-100">Acessar Empréstimos</a>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="card h-100 shadow-sm text-muted">
                 <div class="card-body text-center p-4">
                    <i class="bi bi-graph-up" style="font-size: 3rem;"></i>
                    <h5 class="card-title mt-3">Dashboard e Relatórios</h5>
                    <p class="card-text">(Em breve) Visualize estatísticas importantes, como os livros mais emprestados e o desempenho geral da biblioteca.</p>
                </div>
                <div class="card-footer bg-white border-0 p-3">
                    <a href="#" class="btn btn-secondary disabled w-100">Acessar Relatórios</a>
                </div>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/views/templates/footer.jsp" />