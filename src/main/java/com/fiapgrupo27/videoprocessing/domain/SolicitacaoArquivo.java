package com.fiapgrupo27.videoprocessing.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_arquivo")
public class SolicitacaoArquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArquivo;

    private int idSolicitacao;

    private int idSolicitante;

    private String nomeArquivo;

    private String status;

    private LocalDateTime dataInclusao;

    // Getters e Setters

    public int getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(int idArquivo) {
        this.idArquivo = idArquivo;
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(int idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public boolean isProcessado() {
        return "PROCESSADO".equalsIgnoreCase(this.status);
    }

    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
    }
}
