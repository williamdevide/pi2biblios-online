<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <jsp:include page="/views/templates/header.jsp" />

    <h2>Cadastrar Nova Pessoa</h2>
    <hr>

    <form action="pessoas" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="cadastrar">

        <div class="mb-3">
            <label for="nome" class="form-label">Nome Completo</label>
            <input type="text" class="form-control" id="nome" name="nome" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="telefone" class="form-label">Telefone</label>
            <input type="text" class="form-control" id="telefone" name="telefone" required>
        </div>
        <div class="mb-3">
            <label for="dataNascimento" class="form-label">Data de Nascimento</label>
            <input type="date" class="form-control" id="dataNascimento" name="dataNascimento" required>
        </div>
        <div class="mb-3">
            <label for="foto" class="form-label">Foto de Perfil</label>
            <input class="form-control" type="file" id="foto" name="foto">
        </div>

        <a href="pessoas?action=listar" class="btn btn-secondary">Cancelar</a>
        <button type="submit" class="btn btn-primary">Cadastrar</button>
    </form>

    <jsp:include page="/views/templates/footer.jsp" />