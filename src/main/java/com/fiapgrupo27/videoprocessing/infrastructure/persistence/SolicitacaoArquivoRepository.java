package com.fiapgrupo27.videoprocessing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiapgrupo27.videoprocessing.domain.entity.SolicitacaoArquivo;

@Repository
public interface SolicitacaoArquivoRepository extends JpaRepository<SolicitacaoArquivo, Integer> {
}
