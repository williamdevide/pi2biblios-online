<%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
        <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
            <jsp:include page="/views/templates/header.jsp" />

            <h2>Gerenciamento de Empréstimos</h2>
            <hr>
            <div class="d-flex justify-content-between align-items-center mb-3">
                <form action="emprestimos" method="get">
                    <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="filtro"
                            id="filtroAbertos" value="abertos" ${param.filtro=='abertos' || empty param.filtro
                            ? 'checked' : '' } onchange="this.form.submit()"><label class="form-check-label"
                            for="filtroAbertos">Abertos</label></div>
                    <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="filtro"
                            id="filtroEmDia" value="em_dia" ${param.filtro=='em_dia' ? 'checked' : '' }
                            onchange="this.form.submit()"><label class="form-check-label" for="filtroEmDia">Abertos (Em
                            Dia)</label></div>
                    <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="filtro"
                            id="filtroAtrasados" value="atrasados" ${param.filtro=='atrasados' ? 'checked' : '' }
                            onchange="this.form.submit()"><label class="form-check-label" for="filtroAtrasados">Abertos
                            (Atrasados)</label></div>
                    <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="filtro"
                            id="filtroFinalizados" value="finalizados" ${param.filtro=='finalizados' ? 'checked' : '' }
                            onchange="this.form.submit()"><label class="form-check-label"
                            for="filtroFinalizados">Finalizados</label></div>
                    <div class="form-check form-check-inline"><input class="form-check-input" type="radio" name="filtro"
                            id="filtroTodos" value="todos" ${param.filtro=='todos' ? 'checked' : '' }
                            onchange="this.form.submit()"><label class="form-check-label"
                            for="filtroTodos">Todos</label></div>
                </form>
                <div><a href="emprestimos?action=carregarCadastro" class="btn btn-success">Novo Empréstimo</a></div>
            </div>

            <table class="table table-hover align-middle">
                <c:set var="reverseSortDir" value="${sortDir == 'asc' ? 'desc' : 'asc'}" />

                <thead class="table-dark">
                    <tr>
                        <th>Capa</th>
                        <th>
                            <a href="emprestimos?filtro=${param.filtro}&sortField=l.nome_livro&sortDir=${sortField == 'l.nome_livro' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Livro <c:if test="${sortField == 'l.nome_livro'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>Foto</th>
                        <th>
                            <a href="emprestimos?filtro=${param.filtro}&sortField=p.nome_pessoa&sortDir=${sortField == 'p.nome_pessoa' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Pessoa <c:if test="${sortField == 'p.nome_pessoa'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="emprestimos?filtro=${param.filtro}&sortField=emp.data_emprestimo&sortDir=${sortField == 'emp.data_emprestimo' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Data Empréstimo <c:if test="${sortField == 'emp.data_emprestimo'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="emprestimos?filtro=${param.filtro}&sortField=emp.data_prevista_entrega&sortDir=${sortField == 'emp.data_prevista_entrega' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Data Prev. Devolução <c:if test="${sortField == 'emp.data_prevista_entrega'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>
                            <a href="emprestimos?filtro=${param.filtro}&sortField=emp.data_efetiva_entrega&sortDir=${sortField == 'emp.data_efetiva_entrega' ? reverseSortDir : 'asc'}" class="text-white text-decoration-none">
                                Data Efet. Devolução <c:if test="${sortField == 'emp.data_efetiva_entrega'}"><i class="bi ${sortDir == 'asc' ? 'bi-sort-up' : 'bi-sort-down'}"></i></c:if>
                            </a>
                        </th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="emp" items="${listaEmprestimos}">
                        <%-- LINHA 1: DADOS PRINCIPAIS DO EMPRÉSTIMO --%>
                        <tr class="align-middle">
                            <td>
                                <div class="profile-pic-container">
                                    <c:if test="${not empty emp.livro.base64Image}"><img src="data:image/jpeg;base64,${emp.livro.base64Image}" alt="Capa de ${emp.livro.nome}"></c:if>
                                    <c:if test="${empty emp.livro.base64Image}"><i class="bi bi-book" style="font-size: 2rem; color: #6c757d;"></i></c:if>
                                </div>
                            </td>
                            <td>${emp.livro.nome}</td>
                            <td>
                                <div class="profile-pic-container">
                                    <c:if test="${not empty emp.pessoa.base64Image}"><img src="data:image/jpeg;base64,${emp.pessoa.base64Image}" alt="Foto de ${emp.pessoa.nome}"></c:if>
                                    <c:if test="${empty emp.pessoa.base64Image}"><i class="bi bi-person-circle" style="font-size: 2rem; color: #6c757d;"></i></c:if>
                                </div>
                            </td>
                            <td>${emp.pessoa.nome}</td>
                            <td><fmt:formatDate value="${emp.dataEmprestimo}" pattern="dd/MM/yyyy" timeZone="America/Sao_Paulo"/></td>
                            <td><fmt:formatDate value="${emp.dataPrevistaEntrega}" pattern="dd/MM/yyyy" timeZone="America/Sao_Paulo"/></td>
                            <td>
                                <%-- Usamos c:if pois esta data pode ser nula para empréstimos abertos --%>
                                <c:if test="${not empty emp.dataEfetivaEntrega}">
                                    <fmt:formatDate value="${emp.dataEfetivaEntrega}" pattern="dd/MM/yyyy" timeZone="America/Sao_Paulo" />
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <%-- Se a data de devolução não for nula, o empréstimo está finalizado --%>
                                    <c:when test="${not empty emp.dataEfetivaEntrega}">
                                        <span class="badge bg-secondary">Finalizado</span>
                                    </c:when>
                                    <%-- Se a data prevista for anterior a hoje, está atrasado --%>
                                    <c:when test="${emp.dataPrevistaEntrega < hoje}">
                                         <span class="badge bg-danger">Atrasado</span>
                                    </c:when>
                                    <%-- Caso contrário, está em dia --%>
                                    <c:otherwise>
                                         <span class="badge bg-success">Em dia</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="d-flex gap-1">
                                    <%-- Botão de Devolução (sem alteração) --%>
                                    <c:if test="${empty emp.dataEfetivaEntrega}">
                                        <button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#modalDevolucao" data-id-emprestimo="${emp.id}">Devolver</button>
                                    </c:if>
                
                                    <%-- NOVO BOTÃO PARA EXIBIR/OCULTAR OBSERVAÇÕES --%>
                                    <button class="btn btn-info btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#obs-${emp.id}" aria-expanded="false" aria-controls="obs-${emp.id}">
                                        <i class="bi bi-eye"></i> <%-- Ícone de olho --%>
                                    </button>
                                </div>
                            </td>
                        </tr>
                        
                        <%-- LINHA 2: OBSERVAÇÕES (ESCONDIDA POR PADRÃO) --%>
                        <tr class="collapse" id="obs-${emp.id}">
                            <%-- Usamos 'colspan' para que esta célula ocupe todas as colunas da tabela --%>
                            <td colspan="8">
                                <div class="p-3 bg-light border rounded">
                                    <strong>Observação do Empréstimo:</strong>
                                    <p class="mb-2 fst-italic">${not empty emp.obsEmprestimo ? emp.obsEmprestimo : "Nenhuma."}</p>
                                    
                                    <strong>Observação da Devolução:</strong>
                                    <p class="mb-0 fst-italic">${not empty emp.obsDevolucao ? emp.obsDevolucao : "Nenhuma."}</p>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="modal fade" id="modalDevolucao" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Confirmar Devolução</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form action="emprestimos" method="post">
                            <div class="modal-body">
                                <input type="hidden" name="action" value="devolver">
                                <input type="hidden" id="idEmprestimoModal" name="idEmprestimo">
                                <div class="mb-3">
                                    <label for="obsDevolucao" class="form-label">Observação de Devolução
                                        (Opcional):</label>
                                    <textarea class="form-control" id="obsDevolucao" name="obsDevolucao"
                                        rows="3"></textarea>
                                </div>
                                <p>Tem certeza que deseja marcar este livro como devolvido?</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary"
                                    data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary">Confirmar Devolução</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script>
                // Pequeno script para passar o ID do empréstimo para o modal
                const modalDevolucao = document.getElementById('modalDevolucao');
                modalDevolucao.addEventListener('show.bs.modal', function (event) {
                    const button = event.relatedTarget;
                    const idEmprestimo = button.getAttribute('data-id-emprestimo');
                    const modalInput = modalDevolucao.querySelector('#idEmprestimoModal');
                    modalInput.value = idEmprestimo;
                });
            </script>
            <jsp:include page="/views/templates/footer.jsp" />