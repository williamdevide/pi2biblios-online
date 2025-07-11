<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <jsp:include page="/views/templates/header.jsp" />

    <h2>Editar Pessoa</h2>
    <hr>

    <form action="pessoas" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="atualizar">
        <input type="hidden" name="id" value="${pessoa.id}">

        <div class="mb-3">
            <label for="nome" class="form-label">Nome Completo</label>
            <input type="text" class="form-control" id="nome" name="nome" value="${pessoa.nome}" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" value="${pessoa.email}" required>
        </div>
        <div class="mb-3">
            <label for="telefone" class="form-label">Telefone</label>
            <input type="text" class="form-control" id="telefone" name="telefone" value="${pessoa.telefone}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Data de Nascimento (Não editável)</label>
            <input type="text" class="form-control" value="${pessoa.dataNascimento}" disabled>
        </div>
        <div class="mb-3">
            <label for="foto" class="form-label">Alterar Foto de Perfil</label>
            <input class="form-control" type="file" id="foto" name="foto">
            <div class="form-text">Deixe em branco para manter a foto atual.</div>
        </div>

        <a href="pessoas?action=listar" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary">Salvar Alterações</button>
    </form>

    <jsp:include page="/views/templates/footer.jsp" />