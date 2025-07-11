package br.com.biblios.model;

import java.util.Base64;

public class Livro {
    private int id;
    private String nome;
    private String autor;
    private String categoria;
    private byte[] imgLivro;
    private boolean ativo;
    private String statusEmprestimo;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public byte[] getImgLivro() { return imgLivro; }
    public void setImgLivro(byte[] imgLivro) { this.imgLivro = imgLivro; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getStatusEmprestimo() { return statusEmprestimo; }
    public void setStatusEmprestimo(String statusEmprestimo) { this.statusEmprestimo = statusEmprestimo; }

    // Método auxiliar para converter a imagem para Base64 para o JSP
    public String getBase64Image() {
        if (imgLivro != null && imgLivro.length > 0) {
            return Base64.getEncoder().encodeToString(imgLivro);
        }
        return "";
    }
}