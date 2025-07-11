-- Cria o banco de dados se ele não existir
CREATE DATABASE IF NOT EXISTS pi2biblios;

-- Usa o banco de dados recém-criado ou existente
USE pi2biblios;

-- -----------------------------------------------------
-- Tabela `pessoa`
-- Armazena os dados dos leitores da biblioteca.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS pessoa (
  id_pessoa INT PRIMARY KEY AUTO_INCREMENT,
  nome_pessoa VARCHAR(100) NOT NULL,
  email_pessoa VARCHAR(100) NOT NULL UNIQUE,
  telefone_pessoa VARCHAR(20) NOT NULL,
  nasc_pessoa DATE NOT NULL,
  img_pessoa MEDIUMBLOB NULL,
  ativo_pessoa BOOLEAN DEFAULT TRUE
);

-- -----------------------------------------------------
-- Tabela `livro`
-- Armazena os dados dos livros do acervo.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS livro (
  id_livro INT PRIMARY KEY AUTO_INCREMENT,
  nome_livro VARCHAR(150) NOT NULL,
  autor_livro VARCHAR(100) NOT NULL,
  categoria_livro VARCHAR(50) NOT NULL,
  img_livro MEDIUMBLOB NULL,
  ativo_livro BOOLEAN DEFAULT TRUE
);

-- -----------------------------------------------------
-- Tabela `emprestimo`
-- Registra os empréstimos de livros para as pessoas.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS emprestimo (
  id_emprestimo INT PRIMARY KEY AUTO_INCREMENT,
  id_pessoa INT NOT NULL,
  id_livro INT NOT NULL,
  data_emprestimo DATE NOT NULL,
  data_prevista_entrega DATE NOT NULL,
  data_efetiva_entrega DATE NULL,
  obs_emprestimo TEXT NULL,
  obs_devolucao TEXT NULL,
  status_entrega VARCHAR(20) NOT NULL, -- Exemplos: 'Em andamento', 'Finalizado', 'Atrasado'
  FOREIGN KEY (id_pessoa) REFERENCES pessoa(id_pessoa),
  FOREIGN KEY (id_livro) REFERENCES livro(id_livro)
);


-- -----------------------------------------------------
-- INSERÇÃO DE DADOS DE EXEMPLO
-- -----------------------------------------------------

-- Inserindo 20 Pessoas
INSERT INTO pessoa (nome_pessoa, email_pessoa, telefone_pessoa, nasc_pessoa, ativo_pessoa) VALUES
('Ana Clara Souza', 'ana.souza@example.com', '(11) 98765-4321', '1995-03-15', TRUE),
('Bruno Costa Oliveira', 'bruno.costa@example.com', '(21) 91234-5678', '1988-11-20', TRUE),
('Carla Dias Ferreira', 'carla.dias@example.com', '(31) 99988-7766', '2001-07-08', TRUE),
('Daniel Martins Rodrigues', 'daniel.martins@example.com', '(41) 98877-6655', '1999-01-30', TRUE),
('Eduarda Lima Pereira', 'eduarda.lima@example.com', '(51) 97766-5544', '2003-05-25', TRUE),
('Fábio Almeida Gomes', 'fabio.almeida@example.com', '(61) 96655-4433', '1992-09-10', TRUE),
('Gabriela Santos Silva', 'gabriela.santos@example.com', '(71) 95544-3322', '1997-12-01', TRUE),
('Heitor Ribeiro Azevedo', 'heitor.ribeiro@example.com', '(81) 94433-2211', '1985-04-18', FALSE),
('Isabela Barbosa Castro', 'isabela.barbosa@example.com', '(91) 93322-1100', '2000-02-22', TRUE),
('João Victor Barros', 'joao.barros@example.com', '(12) 92211-0099', '1998-06-14', TRUE),
('Larissa Mendes Correia', 'larissa.mendes@example.com', '(22) 91100-9988', '1996-10-28', TRUE),
('Lucas Cardoso Rocha', 'lucas.cardoso@example.com', '(32) 90099-8877', '1994-08-05', TRUE),
('Mariana Nogueira Pinto', 'mariana.nogueira@example.com', '(42) 98988-7766', '2002-11-11', TRUE),
('Nicolas Vieira Lopes', 'nicolas.vieira@example.com', '(52) 97877-6655', '1993-03-03', TRUE),
('Olivia Cunha Mota', 'olivia.cunha@example.com', '(62) 96766-5544', '1990-07-19', FALSE),
('Pedro Henrique Jesus', 'pedro.jesus@example.com', '(72) 95655-4433', '2004-01-01', TRUE),
('Quintino Sales da Paz', 'quintino.paz@example.com', '(82) 94544-3322', '1980-09-09', TRUE),
('Rafael Teixeira Neves', 'rafael.neves@example.com', '(92) 93433-2211', '1991-05-05', TRUE),
('Sofia Farias Brandão', 'sofia.brandao@example.com', '(13) 92322-1100', '1999-08-17', TRUE),
('Thiago Melo Furtado', 'thiago.furtado@example.com', '(23) 91211-0099', '1989-12-24', TRUE);

-- Inserindo 20 Livros
INSERT INTO livro (nome_livro, autor_livro, categoria_livro, ativo_livro) VALUES
('O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', 'Fantasia', TRUE),
('1984', 'George Orwell', 'Ficção Distópica', TRUE),
('Dom Casmurro', 'Machado de Assis', 'Literatura Brasileira', TRUE),
('A Arte da Guerra', 'Sun Tzu', 'Estratégia', TRUE),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Infantojuvenil', TRUE),
('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', TRUE),
('Cem Anos de Solidão', 'Gabriel García Márquez', 'Realismo Mágico', TRUE),
('O Alquimista', 'Paulo Coelho', 'Autoajuda', TRUE),
('Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', 'Não Ficção', TRUE),
('Orgulho e Preconceito', 'Jane Austen', 'Romance Clássico', TRUE),
('O Guia do Mochileiro das Galáxias', 'Douglas Adams', 'Ficção Científica', TRUE),
('A Revolução dos Bichos', 'George Orwell', 'Sátira Política', TRUE),
('Grande Sertão: Veredas', 'João Guimarães Rosa', 'Literatura Brasileira', TRUE),
('Fahrenheit 451', 'Ray Bradbury', 'Ficção Científica', TRUE),
('Crime e Castigo', 'Fiódor Dostoiévski', 'Romance Psicológico', TRUE),
('O Sol é para Todos', 'Harper Lee', 'Ficção', FALSE),
('Moby Dick', 'Herman Melville', 'Aventura', TRUE),
('A Culpa é das Estrelas', 'John Green', 'Young Adult', TRUE),
('O Nome do Vento', 'Patrick Rothfuss', 'Fantasia', TRUE),
('Mindset: A Nova Psicologia do Sucesso', 'Carol S. Dweck', 'Desenvolvimento Pessoal', TRUE);

-- Inserindo 10 Empréstimos (7 em andamento, 3 finalizados)
-- Empréstimos Finalizados
INSERT INTO emprestimo (id_pessoa, id_livro, data_emprestimo, data_prevista_entrega, data_efetiva_entrega, status_entrega, obs_emprestimo, obs_devolucao) VALUES
(1, 10, '2025-05-01', '2025-05-15', '2025-05-14', 'Finalizado', '', ''), -- Ana pegou 'Orgulho e Preconceito'
(3, 5, '2025-05-10', '2025-05-24', '2025-05-22', 'Finalizado', '', ''), -- Carla pegou 'O Pequeno Príncipe'
(5, 8, '2025-04-20', '2025-05-04', '2025-05-04', 'Finalizado', '', ''); -- Eduarda pegou 'O Alquimista'

-- Empréstimos em Andamento
INSERT INTO emprestimo (id_pessoa, id_livro, data_emprestimo, data_prevista_entrega, data_efetiva_entrega, status_entrega, obs_emprestimo, obs_devolucao) VALUES
(2, 1, CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 4 DAY, NULL, 'Em andamento', '', ''), -- Bruno pegou 'O Senhor dos Anéis'
(4, 2, CURDATE() - INTERVAL 5 DAY, CURDATE() + INTERVAL 9 DAY, NULL, 'Em andamento', '', ''), -- Daniel pegou '1984'
(6, 6, CURDATE() - INTERVAL 8 DAY, CURDATE() + INTERVAL 6 DAY, NULL, 'Em andamento', '', ''), -- Fábio pegou 'Harry Potter'
(7, 9, CURDATE() - INTERVAL 1 DAY, CURDATE() + INTERVAL 13 DAY, NULL, 'Em andamento', '', ''), -- Gabriela pegou 'Sapiens'
(9, 11, CURDATE(), CURDATE() + INTERVAL 14 DAY, NULL, 'Em andamento', '', ''), -- Isabela pegou 'O Guia do Mochileiro das Galáxias'
(10, 14, CURDATE() - INTERVAL 3 DAY, CURDATE() + INTERVAL 11 DAY, NULL, 'Em andamento', '', ''), -- João pegou 'Fahrenheit 451'
(12, 19, CURDATE() - INTERVAL 2 DAY, CURDATE() + INTERVAL 12 DAY, NULL, 'Em andamento', '', ''); -- Lucas pegou 'O Nome do Vento'