# Projeto BibliOS - Sistema de Gerenciamento de Biblioteca
Desenvolvido por: William D. Komel (Instrutor SENAI Ourinhos - Unidade 7.94) 

## 1. Visão Geral do Projeto

O **BibliOS** é um sistema web desenvolvido para o gerenciamento completo de uma biblioteca escolar. O projeto nasceu da necessidade de modernizar e automatizar os processos manuais de uma biblioteca, substituindo os antigos cadernos e fichas de papel por uma solução digital, segura e intuitiva.

O sistema atende à solicitação do Sr. Afonso, diretor da "Escola Aprender Mais", que buscava uma ferramenta para controlar o cadastro de leitores (pessoas) e do acervo (livros), e principalmente, gerenciar todo o fluxo de empréstimos e devoluções, incluindo o controle de prazos e o estado de conservação dos itens.

O projeto foi desenvolvido seguindo a arquitetura **MVC (Model-View-Controller)**, utilizando tecnologias Java para o backend e páginas dinâmicas com JSP para o frontend.

## 2. Tecnologias Utilizadas

* **Backend:**
    * **Java 21:** Linguagem de programação principal.
    * **Jakarta Servlets 6.0:** Para o controle das requisições web (Controller).
    * **JSP (Jakarta Server Pages) 3.1:** Para a criação das páginas web dinâmicas (View).
    * **JSTL (Jakarta Standard Tag Library) 3.0:** Para facilitar a escrita de lógicas nas páginas JSP.
    * **JDBC (Java Database Connectivity):** Para a conexão com o banco de dados.

* **Banco de Dados:**
    * **MySQL 8.0:** Sistema de Gerenciamento de Banco de Dados.

* **Frontend:**
    * **HTML5** e **CSS3**.
    * **Bootstrap 5.3:** Framework CSS para criação de interfaces responsivas e modernas.
    * **Bootstrap Icons 1.11:** Biblioteca de ícones.

* **Gerenciamento e Build:**
    * **Apache Maven 3.9:** Ferramenta de automação de build e gerenciamento de dependências.

* **Servidor de Aplicação:**
    * **Apache Tomcat 10.1:** Contêiner de servlets onde a aplicação é executada.

* **Ambiente de Desenvolvimento:**
    * **Visual Studio Code (VSCode)** com a extensão **Community Server Connectors**.

## 3. Pré-requisitos e Configuração do Ambiente

Para compilar e executar este projeto em sua máquina local, certifique-se de ter os seguintes softwares instalados e configurados:

1.  **JDK (Java Development Kit):** Versão 21 ou superior.
2.  **Apache Maven:** Versão 3.9 ou superior.
3.  **MySQL Server:** Versão 8.0 ou superior.
4.  **Visual Studio Code:** Com as seguintes extensões:
    * `Extension Pack for Java` (da Microsoft).
    * `Community Server Connectors` (da Red Hat).
5.  **(Opcional) Cliente de Banco de Dados:** Uma ferramenta como DBeaver ou MySQL Workbench para gerenciar o banco de dados.

## 4. Estrutura do Projeto

O projeto segue a estrutura padrão do Maven para aplicações web, organizada no padrão MVC. Abaixo está a lista detalhada de todos os arquivos e diretórios relevantes criados.

```
pi2biblios/
├── sql/
|   ├── pi2biblios.sql          (Arquivo com as instruções de criação do BD e conteúdo de exemplo com fotos)
│   └── pi2biblios-semfotos.sql (Arquivo com as instruções de criação do BD e conteúdo de exemplo sem fotos)
├── src/
│   └── main/
│       ├── java/
│       │   └── br/com/biblios/
│       │       ├── controller/ (Servlets - A camada Controller)
│       │       │   ├── PessoaServlet.java
│       │       │   ├── LivroServlet.java
│       │       │   └── EmprestimoServlet.java
│       │       │
│       │       ├── dao/        (Data Access Objects - Comunicação com o BD)
│       │       │   ├── PessoaDAO.java
│       │       │   ├── LivroDAO.java
│       │       │   └── EmprestimoDAO.java
│       │       │
│       │       ├── model/      (JavaBeans - A camada Model)
│       │       │   ├── Pessoa.java
│       │       │   ├── Livro.java
│       │       │   └── Emprestimo.java
│       │       │
│       │       └── util/       (Classes utilitárias)
│       │           ├── ConnectionFactory.java
│       │           └── EncodingFilter.java
│       │
│       └── webapp/
│           ├── assets/
│           │   └── css/
│           │       └── style.css
│           │
│           ├── views/          (Páginas JSP - A camada View)
│           │   ├── emprestimo/
│           │   │   ├── emprestimos.jsp
│           │   │   └── cadastrar-emprestimo.jsp
│           │   │
│           │   ├── livro/
│           │   │   ├── livros.jsp
│           │   │   ├── cadastrar-livro.jsp
│           │   │   └── editar-livro.jsp
│           │   │
│           │   ├── pessoa/
│           │   │   ├── pessoas.jsp
│           │   │   ├── cadastrar-pessoa.jsp
│           │   │   └── editar-pessoa.jsp
│           │   │
│           │   └── templates/
│           │       ├── header.jsp
│           │       └── footer.jsp
│           │
│           ├── WEB-INF/
│           │   └── web.xml     (Descritor de Implantação)
│           │
│           └── index.jsp       (Página inicial)
│
├── target/                     (Diretório gerado pelo Maven com os arquivos compilados)
│
├── pom.xml                     (Arquivo de configuração do Maven)
└── README.md                   (Este arquivo de documentação)
```

## 5. Instalação e Execução

Siga estes passos para colocar o projeto em funcionamento:

#### Passo 1: Configuração do Banco de Dados

1.  Abra seu cliente MySQL (ou o terminal).
2.  Importe e execute o arquivo de backup do banco de dados (`pi2biblios_backup.sql` ou similar) fornecido com o projeto. Este arquivo criará o banco de dados `pi2biblios` e todas as tabelas, já populadas com dados de exemplo, incluindo as imagens.
3.  **Verifique as credenciais:** Abra o arquivo `src/main/java/br/com/biblios/util/ConnectionFactory.java` e certifique-se de que o usuário (`root`) e a senha (`""`) correspondem à configuração do seu servidor MySQL local.

#### Passo 2: Build do Projeto com Maven

1.  Abra o projeto no Visual Studio Code.
2.  Abra o terminal integrado do VSCode (`Ctrl+'`).
3.  Execute o comando abaixo para que o Maven baixe todas as dependências e compile o projeto.
    ```
    mvn clean package
    ```
4.  Aguarde a mensagem **`[INFO] BUILD SUCCESS`**. Isso criará uma pasta `target` na raiz do projeto.

#### Passo 3: Configuração do Servidor Tomcat

1.  No painel esquerdo do VSCode, vá para a aba **"SERVERS"**.
2.  Clique no ícone de `+` e, se ainda não tiver feito, baixe e configure um servidor **Tomcat v10.1.x**.
3.  Com o servidor configurado, clique com o botão direito sobre ele e selecione **"Add Deployment"**.
4.  Escolha a opção **"Exploded"** que corresponde ao seu projeto (`pi2biblios`).

#### Passo 4: Execução

1.  Na aba "SERVERS", clique com o botão direito no nome do servidor Tomcat e selecione **"Start"**.
2.  Aguarde os logs no terminal indicarem que o servidor iniciou (`Server startup in [XXX] ms`).
3.  Abra seu navegador e acesse a URL:
    **`http://localhost:8080/pi2biblios/`**

A página inicial do sistema BibliOS deverá ser exibida.

## 6. Funcionalidades Implementadas

* **Pessoas (Leitores):**
    * Cadastro, Edição, Busca e Inativação de pessoas.
    * Upload de foto de perfil.
    * Validação que impede a inativação de pessoas com empréstimos ativos.
    * Exibição de um resumo de empréstimos (concluídos, em dia, atrasados).
    * Ordenação da lista por todas as colunas.

* **Livros:**
    * Cadastro, Edição, Busca e Inativação de livros.
    * Upload de imagem da capa.
    * Validação que impede a inativação de livros que estão emprestados.
    * Exibição do status de disponibilidade (Disponível, Emprestado em dia, Emprestado Atrasado).
    * Ordenação da lista por todas as colunas.

* **Empréstimos:**
    * Lançamento de novos empréstimos para múltiplos livros de uma só vez.
    * Fluxo de cadastro em etapas (Seleção da Pessoa -> Adição dos Livros -> Finalização).
    * Definição de data de devolução com valor padrão de 7 dias.
    * Registro de observações no ato do empréstimo e na devolução.
    * Listagem completa de empréstimos com filtros (Abertos, Atrasados, Finalizados, etc.).
    * Ordenação da lista por todas as colunas.

* **Interface e Usabilidade:**
    * Interface responsiva e moderna com Bootstrap.
    * Página inicial com cards para acesso rápido às funcionalidades.
    * Validações visuais que desabilitam botões de ações não permitidas.
