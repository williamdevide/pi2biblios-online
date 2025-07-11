<%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <jsp:include page="/views/templates/header.jsp" />

        <h2>Gerenciamento de Livros</h2>
        <hr>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <!-- Formulário de Filtros de Status -->
            <form action="livros" method="get">
                <input type="hidden" name="action" value="listar">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="filtroStatus" id="filtroAtivos" value="ativos"
                        ${param.filtroStatus=='ativos' || empty param.filtroStatus ? 'checked' : '' }
                        onchange="this.form.submit()">
                    <label class="form-check-label" for="filtroAtivos">Ativos</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="filtroStatus" id="filtroInativos"
                        value="inativos" ${param.filtroStatus=='inativos' ? 'checked' : '' }
                        onchange="this.form.submit()">
                    <label class="form-check-label" for="filtroInativos">Inativos</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="filtroStatus" id="filtroTodos" value="todos"
                        ${param.filtroStatus=='todos' ? 'checked' : '' } onchange="this.form.submit()">
                    <label class="form-check-label" for="filtroTodos">Todos</label>
                </div>
            </form>
            <div>
                <a href="livros?action=carregarCadastro" class="btn btn-success">Cadastrar Novo Livro</a>
            </div>
        </div>

        <div class="row mb-3">
            <div class="col-md-6">
                <form action="livros" method="get" class="d-flex">
                    <input type="hidden" name="action" value="buscar">
                    <input type="text" name="termoBusca" class="form-control me-2"
                        placeholder="Buscar por ID ou Título..." value="${param.termoBusca}">
                    <button type="submit" class="btn btn-primary">Buscar</button>
                </form>
            </div>
        </div>

        <table class="table table-striped table-hover align-middle">
            <c:set var="reverseSortDir" value="${sortDir == 'asc' ? 'desc' : 'asc'}" />
            <thead class="table-dark">
                <tr>
                    <th>Capa</th>
                    <th>
                        <a href="livros?action=listar&filtroStatus=${param.filtroStatus}&sortField=id_livro&sortDir=${sortField == 'id_livro' ? reverseSortDir : 'asc'}"
                            class="text-white text-decoration-none">
                            ID <c:if test="${sortField == 'id_livro'}"><i
                                    class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                        </a>
                    </th>
                    <th>
                        <a href="livros?action=listar&filtroStatus=${param.filtroStatus}&sortField=nome_livro&sortDir=${sortField == 'nome_livro' ? reverseSortDir : 'asc'}"
                            class="text-white text-decoration-none">
                            Título <c:if test="${sortField == 'nome_livro'}"><i
                                    class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                        </a>
                    </th>
                    <th>
                        <a href="livros?action=listar&filtroStatus=${param.filtroStatus}&sortField=autor_livro&sortDir=${sortField == 'autor_livro' ? reverseSortDir : 'asc'}"
                            class="text-white text-decoration-none">
                            Autor <c:if test="${sortField == 'autor_livro'}"><i
                                    class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                        </a>
                    </th>
                    <th>
                        <a href="livros?action=listar&filtroStatus=${param.filtroStatus}&sortField=categoria_livro&sortDir=${sortField == 'categoria_livro' ? reverseSortDir : 'asc'}"
                            class="text-white text-decoration-none">
                            Categoria <c:if test="${sortField == 'categoria_livro'}"><i
                                    class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                        </a>
                    </th>
                    <th>
                        <a href="livros?action=listar&filtroStatus=${param.filtroStatus}&sortField=status_emprestimo&sortDir=${sortField == 'status_emprestimo' ? reverseSortDir : 'asc'}"
                            class="text-white text-decoration-none">
                            Status <c:if test="${sortField == 'status_emprestimo'}"><i
                                    class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                        </a>
                    </th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="livro" items="${listaLivros}">
                    <tr>
                        <td>
                            <div class="profile-pic-container">
                                <c:if test="${not empty livro.base64Image}"><img
                                        src="data:image/jpeg;base64,${livro.base64Image}" alt="Capa de ${livro.nome}">
                                </c:if>
                                <c:if test="${empty livro.base64Image}"><i class="bi bi-book"
                                        style="font-size: 2rem; color: #6c757d;"></i></c:if>
                            </div>
                        </td>
                        <td>${livro.id}</td>
                        <td>${livro.nome}</td>
                        <td>${livro.autor}</td>
                        <td>${livro.categoria}</td>
                        <td>
                            <c:choose>
                                <c:when test="${livro.statusEmprestimo == 'Disponível'}">
                                    <span class="badge bg-success">${livro.statusEmprestimo}</span>
                                </c:when>
                                <c:when test="${livro.statusEmprestimo == 'Emprestado (Em dia)'}">
                                    <span class="badge bg-warning text-dark">${livro.statusEmprestimo}</span>
                                </c:when>
                                <c:when test="${livro.statusEmprestimo == 'Emprestado (Atrasado)'}">
                                    <span class="badge bg-danger">${livro.statusEmprestimo}</span>
                                </c:when>
                            </c:choose>
                        </td>
                        <td> <%-- Célula das Ações --%>
                                <div class="d-flex gap-1">

                                    <%-- A lógica principal verifica se o livro está ATIVO ou INATIVO --%>
                                        <c:choose>

                                            <%-- CASO 1: O LIVRO ESTÁ ATIVO --%>
                                                <c:when test="${livro.ativo}">

                                                    <%-- O botão Editar está sempre funcional para livros ativos --%>
                                                        <a href="livros?action=carregarEdicao&id=${livro.id}"
                                                            class="btn btn-warning btn-sm w-100">Editar</a>

                                                        <%-- Lógica interna para o botão Inativar, baseada no status de
                                                            empréstimo --%>
                                                            <c:choose>
                                                                <%-- Se o livro está disponível, o botão Inativar é
                                                                    funcional --%>
                                                                    <c:when
                                                                        test="${livro.statusEmprestimo == 'Disponível'}">
                                                                        <a href="livros?action=inativar&id=${livro.id}"
                                                                            class="btn btn-danger btn-sm w-100"
                                                                            onclick="return confirm('Tem certeza que deseja inativar este livro?')">Inativar</a>
                                                                    </c:when>
                                                                    <%-- Se o livro está emprestado, o botão Inativar
                                                                        fica desabilitado --%>
                                                                        <c:otherwise>
                                                                            <span
                                                                                class="btn btn-danger btn-sm disabled w-100"
                                                                                title="Não é possível inativar um livro que está emprestado">Inativar</span>
                                                                        </c:otherwise>
                                                            </c:choose>

                                                </c:when>

                                                <%-- CASO 2: O LIVRO ESTÁ INATIVO --%>
                                                    <c:otherwise>

                                                        <%-- O botão Editar fica desabilitado para livros inativos --%>
                                                            <span
                                                                class="btn btn-warning btn-sm disabled w-100">Editar</span>

                                                            <%-- O botão Reativar aparece funcional --%>
                                                                <a href="livros?action=reativar&id=${livro.id}"
                                                                    class="btn btn-success btn-sm w-100">Reativar</a>

                                                    </c:otherwise>
                                        </c:choose>
                                </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <jsp:include page="/views/templates/footer.jsp" />