package com.fiapgrupo27.videoprocessing.infrastructure.persistence;

import com.fiapgrupo27.videoprocessing.domain.SolicitacaoArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoArquivoRepository extends JpaRepository<SolicitacaoArquivo, Integer> {
}
