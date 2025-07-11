package br.com.biblios.model;

import java.util.Base64;
import java.util.Date;

public class Pessoa {
    // Atributos que correspondem às colunas da tabela
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private Date dataNascimento;
    private byte[] imgPessoa;
    private boolean ativo;

    private int emprestimosConcluidos;
    private int emprestimosEmDia;
    private int emprestimosAtrasados;

    // Getters e Setters para cada atributo
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public byte[] getImgPessoa() { return imgPessoa; }
    public void setImgPessoa(byte[] imgPessoa) { this.imgPessoa = imgPessoa; }
   
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public int getEmprestimosConcluidos() { return emprestimosConcluidos; }
    public void setEmprestimosConcluidos(int emprestimosConcluidos) { this.emprestimosConcluidos = emprestimosConcluidos; }
    
    public int getEmprestimosEmDia() { return emprestimosEmDia; }
    public void setEmprestimosEmDia(int emprestimosEmDia) { this.emprestimosEmDia = emprestimosEmDia; }
    
    public int getEmprestimosAtrasados() { return emprestimosAtrasados; }
    public void setEmprestimosAtrasados(int emprestimosAtrasados) { this.emprestimosAtrasados = emprestimosAtrasados; }

    // --- MÉTODO AUXILIAR PARA A PÁGINA JSP ---
    // Este método converte o array de bytes da imagem para uma String Base64
    // que pode ser usada diretamente em uma tag <img> no HTML.
    public String getBase64Image() {
        if (imgPessoa != null && imgPessoa.length > 0) {
            return Base64.getEncoder().encodeToString(imgPessoa);
        }
        return ""; // Retorna string vazia se não houver imagem
    }
}