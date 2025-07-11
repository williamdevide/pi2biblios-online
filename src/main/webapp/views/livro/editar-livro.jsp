<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <jsp:include page="/views/templates/header.jsp" />

    <h2>Editar Livro</h2>
    <hr>
    <form action="livros" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="atualizar">
        <input type="hidden" name="id" value="${livro.id}">
        <div class="mb-3">
            <label for="nome" class="form-label">Título</label>
            <input type="text" class="form-control" id="nome" name="nome" value="${livro.nome}" required>
        </div>
        <div class="mb-3">
            <label for="autor" class="form-label">Autor</label>
            <input type="text" class="form-control" id="autor" name="autor" value="${livro.autor}" required>
        </div>
        <div class="mb-3">
            <label for="categoria" class="form-label">Categoria</label>
            <input type="text" class="form-control" id="categoria" name="categoria" value="${livro.categoria}" required>
        </div>
        <div class="mb-3">
            <label for="capa" class="form-label">Alterar Capa do Livro</label>
            <input class="form-control" type="file" id="capa" name="capa">
            <div class="form-text">Deixe em branco para manter a capa atual.</div>
        </div>
        <a href="livros?action=listar" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary">Salvar Alterações</button>
    </form>

    <jsp:include page="/views/templates/footer.jsp" />