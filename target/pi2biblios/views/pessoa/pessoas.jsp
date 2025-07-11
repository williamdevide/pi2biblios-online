<%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
        <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        
            <jsp:include page="/views/templates/header.jsp" />

            <h2>Gerenciamento de Pessoas</h2>
            <hr>
            <div class="d-flex justify-content-between align-items-center mb-3">
                <!-- Formulário de Filtros de Status -->
                <form action="pessoas" method="get">
                    <input type="hidden" name="action" value="listar">
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="filtroStatus" id="filtroAtivos" value="ativos" ${param.filtroStatus == 'ativos' || empty param.filtroStatus ? 'checked' : ''} onchange="this.form.submit()">
                        <label class="form-check-label" for="filtroAtivos">Ativos</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="filtroStatus" id="filtroInativos" value="inativos" ${param.filtroStatus == 'inativos' ? 'checked' : ''} onchange="this.form.submit()">
                        <label class="form-check-label" for="filtroInativos">Inativos</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="filtroStatus" id="filtroTodos" value="todos" ${param.filtroStatus == 'todos' ? 'checked' : ''} onchange="this.form.submit()">
                        <label class="form-check-label" for="filtroTodos">Todos</label>
                    </div>
                </form>
                <div>
                    <a href="pessoas?action=carregarCadastro" class="btn btn-success">Cadastrar Nova Pessoa</a>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-6">
                    <form action="pessoas" method="get" class="d-flex">
                        <input type="hidden" name="action" value="buscar">
                        <input type="text" name="termoBusca" class="form-control me-2"
                            placeholder="Buscar por ID ou Nome..." value="${param.termoBusca}">
                        <button type="submit" class="btn btn-primary">Buscar</button>
                    </form>
                </div>
            </div>


            <c:if test="${not empty mensagem}">
                <div class="alert alert-success" role="alert">
                    ${mensagem}
                </div>
            </c:if>

            <table class="table table-striped table-hover">
                <c:set var="reverseSortDir" value="${sortDir == 'asc' ? 'desc' : 'asc'}" />

                <thead class="table-dark">
                    <tr>
                        <th>Foto</th>
                        <th>
                            <a href="pessoas?action=listar&filtroStatus=${param.filtroStatus}&sortField=id_pessoa&sortDir=${sortField == 'id_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                ID <c:if test="${sortField == 'id_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="pessoas?action=listar&filtroStatus=${param.filtroStatus}&sortField=nome_pessoa&sortDir=${sortField == 'nome_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Nome <c:if test="${sortField == 'nome_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="pessoas?action=listar&filtroStatus=${param.filtroStatus}&sortField=email_pessoa&sortDir=${sortField == 'email_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Email <c:if test="${sortField == 'email_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="pessoas?action=listar&filtroStatus=${param.filtroStatus}&sortField=telefone_pessoa&sortDir=${sortField == 'telefone_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Telefone <c:if test="${sortField == 'telefone_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="pessoas?action=listar&filtroStatus=${param.filtroStatus}&sortField=nasc_pessoa&sortDir=${sortField == 'nasc_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Data de Nascimento <c:if test="${sortField == 'nasc_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>Status Empréstimos</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pessoa" items="${listaPessoas}">
                        <tr>
                            <td>
                                <div class="profile-pic-container">
                                    <c:if test="${not empty pessoa.base64Image}">
                                        <%-- A foto da pessoa preenche o container --%>
                                            <img src="data:image/jpeg;base64,${pessoa.base64Image}"
                                                alt="Foto de ${pessoa.nome}">
                                    </c:if>
                                    <c:if test="${empty pessoa.base64Image}">
                                        <%-- O ícone é centralizado dentro do container --%>
                                            <i class="bi bi-person-circle" style="font-size: 2rem; color: #6c757d;"></i>
                                    </c:if>
                                </div>
                            </td>
                            <td>${pessoa.id}</td>
                            <td>${pessoa.nome}</td>
                            <td>${pessoa.email}</td>
                            <td>${pessoa.telefone}</td>
                            <td>
                                <fmt:formatDate value="${pessoa.dataNascimento}" pattern="dd/MM/yyyy"
                                    timeZone="America/Sao_Paulo" />
                            </td>
                            <td>
                                <div><span class="badge bg-secondary">Concluídos: ${pessoa.emprestimosConcluidos}</span>
                                </div>
                                <div><span class="badge bg-success">Em dia: ${pessoa.emprestimosEmDia}</span></div>
                                <div><span class="badge bg-danger">Atrasados: ${pessoa.emprestimosAtrasados}</span>
                                </div>
                            </td>
                            <td> <%-- Célula das Ações --%>
                                <div class="d-flex gap-1">
                            
                                    <%-- A lógica principal verifica se a pessoa está ATIVA ou INATIVA --%>
                                    <c:choose>
                                        
                                        <%-- CASO 1: A PESSOA ESTÁ ATIVA --%>
                                        <c:when test="${pessoa.ativo}">
                                            
                                            <%-- O botão Editar está sempre funcional para pessoas ativas --%>
                                            <a href="pessoas?action=carregarEdicao&id=${pessoa.id}" class="btn btn-warning btn-sm w-100">Editar</a>
                            
                                            <%-- Lógica interna para o botão Inativar --%>
                                            <c:choose>
                                                <%-- Se não tiver pendências, o botão Inativar é funcional --%>
                                                <c:when test="${pessoa.emprestimosEmDia == 0 && pessoa.emprestimosAtrasados == 0}">
                                                    <a href="pessoas?action=inativar&id=${pessoa.id}" class="btn btn-danger btn-sm w-100" onclick="return confirm('Tem certeza que deseja inativar esta pessoa?')">Inativar</a>
                                                </c:when>
                                                <%-- Se tiver pendências, o botão Inativar fica desabilitado --%>
                                                <c:otherwise>
                                                    <span class="btn btn-danger btn-sm disabled w-100" title="Não é possível inativar pessoas com empréstimos em aberto">Inativar</span>
                                                </c:otherwise>
                                            </c:choose>
                            
                                        </c:when>
                            
                                        <%-- CASO 2: A PESSOA ESTÁ INATIVA --%>
                                        <c:otherwise>
                                            
                                            <%-- O botão Editar fica desabilitado para pessoas inativas --%>
                                            <span class="btn btn-warning btn-sm disabled w-100">Editar</span>
                            
                                            <%-- O botão Reativar aparece funcional --%>
                                            <a href="pessoas?action=reativar&id=${pessoa.id}" class="btn btn-success btn-sm w-100">Reativar</a>
                            
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <jsp:include page="/views/templates/footer.jsp" />