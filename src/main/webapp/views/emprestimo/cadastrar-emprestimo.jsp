<%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <jsp:include page="/views/templates/header.jsp" />

        <h2>Novo Empréstimo</h2>
        <hr>

        <%-- PASSO 1: SELECIONAR PESSOA (só aparece se ninguém foi selecionado ainda) --%>
            <c:if test="${empty pessoaSelecionada}">
                <div class="alert alert-info">Passo 1 de 3: Selecione a Pessoa</div>
                <form action="emprestimos" method="get">
                    <input type="hidden" name="action" value="selecionarPessoa">
                    <div class="row g-3 align-items-end">
                        <div class="col-md-6"><label for="dataEmprestimo" class="form-label">Data do
                                Empréstimo</label><input type="date" id="dataEmprestimo" name="dataEmprestimo"
                                class="form-control" value="${dataEmprestimo}" readonly></div>
                        <div class="col-md-6"><label for="dataDevolucao" class="form-label">Data Agendada de
                                Devolução</label><input type="date" id="dataDevolucao" name="dataDevolucao"
                                class="form-control" value="${dataDevolucao}" required></div>
                        <div class="col-md-8"><label for="pessoaId" class="form-label mt-3">Pessoas
                                disponíveis:</label><select id="pessoaId" name="pessoaId" class="form-select" required>
                                <option value="" selected disabled>Selecione...</option>
                                <c:forEach var="pessoa" items="${listaPessoasDisponiveis}">
                                    <option value="${pessoa.id}">${pessoa.nome}</option>
                                </c:forEach>
                            </select></div>
                        <div class="col-md-4 mt-3"><button type="submit" class="btn btn-success w-100">Confirmar Pessoa
                                <i class="bi bi-arrow-right-circle"></i></button></div>
                    </div>
                </form>
            </c:if>

            <%-- SEÇÃO EXIBIDA APÓS SELECIONAR A PESSOA --%>
                <c:if test="${not empty pessoaSelecionada}">
                    <%-- CARD DA PESSOA SELECIONADA --%>
                        <div class="card mb-4">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <strong>Pessoa Selecionada</strong>
                                <a href="emprestimos?action=carregarCadastro" class="btn btn-danger btn-sm"><i
                                        class="bi bi-x-lg"></i> Iniciar Novo</a>
                            </div>
                            <div class="card-body d-flex align-items-center">
                                <%-- 1. APLICAMOS A NOVA CLASSE AQUI --%>
                                    <div class="profile-pic-container-large me-3">
                                        <c:if test="${not empty pessoaSelecionada.base64Image}">
                                            <img src="data:image/jpeg;base64,${pessoaSelecionada.base64Image}"
                                                alt="Foto">
                                        </c:if>
                                        <c:if test="${empty pessoaSelecionada.base64Image}">
                                            <%-- 2. AUMENTAMOS O ÍCONE PARA SER PROPORCIONAL --%>
                                                <i class="bi bi-person-circle"
                                                    style="font-size: 5rem; color: #6c757d;"></i>
                                        </c:if>
                                    </div>
                                    <div>
                                        <h5 class="card-title mb-1">${pessoaSelecionada.nome}</h5>
                                        <p class="card-text mb-0"><small class="text-muted">Email:
                                                ${pessoaSelecionada.email}</small></p>
                                        <p class="card-text mb-0"><small class="text-muted">Telefone:
                                                ${pessoaSelecionada.telefone}</small></p>
                                    </div>
                            </div>
                        </div>

                        <%-- PASSO 2: EXIBE A LISTA DE LIVROS JÁ ADICIONADOS (O CARRINHO) --%>
                            <c:if test="${not empty sessionScope.carrinhoDeLivros}">
                                <h5>Livros no Empréstimo</h5>
                                <%-- Usamos o Grid do Bootstrap para alinhar os cards lado a lado --%>
                                    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3 mb-4">
                                        <c:forEach var="livroNoCarrinho" items="${sessionScope.carrinhoDeLivros}">
                                            <div class="col">
                                                <div class="card h-100 shadow-sm">
                                                    <%-- Imagem da Capa --%>
                                                        <div class="card-img-top bg-light d-flex align-items-center justify-content-center"
                                                            style="height: 180px;">
                                                            <c:if test="${not empty livroNoCarrinho.base64Image}">
                                                                <img src="data:image/jpeg;base64,${livroNoCarrinho.base64Image}"
                                                                    alt="Capa de ${livroNoCarrinho.nome}"
                                                                    style="max-height: 100%; max-width: 100%;">
                                                            </c:if>
                                                            <c:if test="${empty livroNoCarrinho.base64Image}">
                                                                <i class="bi bi-book"
                                                                    style="font-size: 3rem; color: #6c757d;"></i>
                                                            </c:if>
                                                        </div>

                                                        <div class="card-body">
                                                            <%-- Título do Livro --%>
                                                                <h6 class="card-title" style="font-size: 0.9rem;">
                                                                    ${livroNoCarrinho.nome}</h6>
                                                                <%-- Autor em fonte menor --%>
                                                                    <p class="card-text mb-0"><small
                                                                            class="text-muted">${livroNoCarrinho.autor}</small>
                                                                    </p>
                                                        </div>

                                                        <div class="card-footer bg-white border-0 p-2">
                                                            <%-- Botão para Remover o livro do carrinho --%>
                                                                <a href="emprestimos?action=removerLivro&livroId=${livroNoCarrinho.id}&pessoaId=${pessoaSelecionada.id}&dataEmprestimo=${dataEmprestimo}&dataDevolucao=${dataDevolucao}"
                                                                    class="btn btn-danger btn-sm w-100">
                                                                    <i class="bi bi-trash"></i> Remover
                                                                </a>
                                                        </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                            </c:if>

                            <%-- FORMULÁRIO PARA ADICIONAR UM NOVO LIVRO AO CARRINHO --%>
                                <div class="alert alert-info">Passo 2 de 3: Adicione os Livros</div>
                                <form action="emprestimos" method="get">
                                    <input type="hidden" name="action" value="adicionarLivro">
                                    <input type="hidden" name="pessoaId" value="${pessoaSelecionada.id}">
                                    <input type="hidden" name="dataEmprestimo" value="${dataEmprestimo}">
                                    <input type="hidden" name="dataDevolucao" value="${dataDevolucao}">
                                    <div class="row align-items-end">
                                        <div class="col-md-8">
                                            <label for="livroId" class="form-label">Livros Disponíveis:</label>
                                            <select id="livroId" name="livroId" class="form-select" required>
                                                <option value="" selected disabled>Selecione um livro para adicionar...
                                                </option>

                                                <c:forEach var="livro" items="${listaLivrosDisponiveis}">
                                                    <%-- A condição agora é muito mais simples e usa o Set enviado pelo
                                                        servlet --%>
                                                        <c:if test="${!carrinhoIds.contains(livro.id)}">
                                                            <option value="${livro.id}">${livro.nome}</option>
                                                        </c:if>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-4">
                                            <button type="submit" class="btn btn-info w-100">Adicionar Livro</button>
                                        </div>
                                    </div>
                                </form>

                                <hr class="my-4">

                                <%-- PASSO 3: FORMULÁRIO FINAL PARA CONCLUIR O EMPRÉSTIMO --%>
                                    <div class="alert alert-info">Passo 3 de 3: Adicione uma observação e finalize</div>
                                    <form action="emprestimos" method="post">
                                        <input type="hidden" name="action" value="cadastrar">
                                        <input type="hidden" name="pessoaId" value="${pessoaSelecionada.id}">
                                        <input type="hidden" name="dataEmprestimo" value="${dataEmprestimo}">
                                        <input type="hidden" name="dataDevolucao" value="${dataDevolucao}">

                                        <div class="mb-3">
                                            <label for="obsEmprestimo" class="form-label">Observação Geral
                                                (Opcional)</label>
                                            <textarea class="form-control" id="obsEmprestimo" name="obsEmprestimo"
                                                rows="3"></textarea>
                                        </div>
                                        <div class="d-grid">
                                            <button type="submit" class="btn btn-primary btn-lg" ${empty
                                                sessionScope.carrinhoDeLivros ? 'disabled' : '' }>
                                                Realizar Empréstimo de ${sessionScope.carrinhoDeLivros.size()} Livro(s)
                                            </button>
                                        </div>
                                    </form>
                                    <div class="mt-4">
                                        <a href="emprestimos?filtro=abertos" class="btn btn-secondary">Voltar para a
                                            Lista</a>
                                    </div>
                </c:if>

                <jsp:include page="/views/templates/footer.jsp" />