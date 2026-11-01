package br.ufms.minhacritica.repository;

import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.model.TipoObra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObraRepository extends JpaRepository<Obra, Long> {

    List<Obra> findByTituloContainingIgnoreCaseOrderByTituloAsc(String titulo);

    List<Obra> findByTipoAndTituloContainingIgnoreCaseOrderByTituloAsc(TipoObra tipo, String titulo);

    List<Obra> findTop6ByOrderByCriadoEmDesc();

    boolean existsByTituloIgnoreCaseAndTipoAndAnoLancamento(String titulo, TipoObra tipo, Integer anoLancamento);

    boolean existsByTituloIgnoreCaseAndTipoAndAnoLancamentoAndIdNot(String titulo, TipoObra tipo,
                                                                    Integer anoLancamento, Long id);
}
