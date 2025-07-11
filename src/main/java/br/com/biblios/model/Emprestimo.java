package br.com.biblios.model;

import java.util.Date;

public class Emprestimo {
    private int id;
    private Pessoa pessoa;
    private Livro livro;
    private Date dataEmprestimo;
    private Date dataPrevistaEntrega;
    private Date dataEfetivaEntrega;
    private String obsEmprestimo;
    private String obsDevolucao;
    private String statusEntrega;

    // Getters e Setters para todos os campos...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
    
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    
    public Date getDataPrevistaEntrega() { return dataPrevistaEntrega; }
    public void setDataPrevistaEntrega(Date dataPrevistaEntrega) { this.dataPrevistaEntrega = dataPrevistaEntrega; }
    
    public Date getDataEfetivaEntrega() { return dataEfetivaEntrega; }
    public void setDataEfetivaEntrega(Date dataEfetivaEntrega) { this.dataEfetivaEntrega = dataEfetivaEntrega; }

    public String getObsEmprestimo() { return obsEmprestimo; }
    public void setObsEmprestimo(String obsEmprestimo) { this.obsEmprestimo = obsEmprestimo; }

    public String getObsDevolucao() { return obsDevolucao; }
    public void setObsDevolucao(String obsDevolucao) { this.obsDevolucao = obsDevolucao; }
    
    public String getStatusEntrega() { return statusEntrega; }
    public void setStatusEntrega(String statusEntrega) { this.statusEntrega = statusEntrega; }
}