<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <jsp:include page="/views/templates/header.jsp" />

    <h2>Cadastrar Novo Livro</h2>
    <hr>
    <form action="livros" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="cadastrar">
        <div class="mb-3">
            <label for="nome" class="form-label">Título</label>
            <input type="text" class="form-control" id="nome" name="nome" required>
        </div>
        <div class="mb-3">
            <label for="autor" class="form-label">Autor</label>
            <input type="text" class="form-control" id="autor" name="autor" required>
        </div>
        <div class="mb-3">
            <label for="categoria" class="form-label">Categoria</label>
            <input type="text" class="form-control" id="categoria" name="categoria" required>
        </div>
        <div class="mb-3">
            <label for="capa" class="form-label">Capa do Livro</label>
            <input class="form-control" type="file" id="capa" name="capa">
        </div>
        <a href="livros?action=listar" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary">Cadastrar</button>
    </form>

    <jsp:include page="/views/templates/footer.jsp" />