package br.ufms.minhacritica.repository;

import br.ufms.minhacritica.model.Critica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriticaRepository extends JpaRepository<Critica, Long> {

    boolean existsByAutorIdAndObraId(Long autorId, Long obraId);
}
