package br.ufms.minhacritica.service;

import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.model.TipoObra;
import br.ufms.minhacritica.repository.ObraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ObraService {

    private final ObraRepository obraRepository;

    public ObraService(ObraRepository obraRepository) {
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<Obra> listar(String busca, TipoObra tipo) {
        String termo = busca == null ? "" : busca.trim();
        if (tipo == null) {
            return obraRepository.findByTituloContainingIgnoreCaseOrderByTituloAsc(termo);
        }
        return obraRepository.findByTipoAndTituloContainingIgnoreCaseOrderByTituloAsc(tipo, termo);
    }

    @Transactional(readOnly = true)
    public List<Obra> recentes() {
        return obraRepository.findTop6ByOrderByCriadoEmDesc();
    }

    @Transactional(readOnly = true)
    public long contar() {
        return obraRepository.count();
    }

    @Transactional(readOnly = true)
    public Obra buscar(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Obra não encontrada"));
    }

    /** RN07: não pode haver duas obras com mesmo título, tipo e ano. */
    @Transactional
    public Obra criar(Obra obra) {
        obra.setId(null);
        normalizar(obra);
        if (obraRepository.existsByTituloIgnoreCaseAndTipoAndAnoLancamento(
                obra.getTitulo(), obra.getTipo(), obra.getAnoLancamento())) {
            throw new RegraNegocioException("Já existe uma obra com este título, tipo e ano.");
        }
        return obraRepository.save(obra);
    }

    @Transactional
    public Obra atualizar(Long id, Obra dados) {
        Obra obra = buscar(id);
        normalizar(dados);
        if (obraRepository.existsByTituloIgnoreCaseAndTipoAndAnoLancamentoAndIdNot(
                dados.getTitulo(), dados.getTipo(), dados.getAnoLancamento(), id)) {
            throw new RegraNegocioException("Já existe outra obra com este título, tipo e ano.");
        }
        obra.setTitulo(dados.getTitulo());
        obra.setTipo(dados.getTipo());
        obra.setGenero(dados.getGenero());
        obra.setAnoLancamento(dados.getAnoLancamento());
        obra.setAutorOuDiretor(dados.getAutorOuDiretor());
        obra.setSinopse(dados.getSinopse());
        return obra;
    }

    /** RN05: a exclusão remove também todas as críticas da obra (cascade). */
    @Transactional
    public void excluir(Long id) {
        obraRepository.delete(buscar(id));
    }

    private void normalizar(Obra obra) {
        obra.setTitulo(obra.getTitulo().trim());
        obra.setGenero(obra.getGenero().trim());
        if (obra.getAutorOuDiretor() != null) {
            String autor = obra.getAutorOuDiretor().trim();
            obra.setAutorOuDiretor(autor.isEmpty() ? null : autor);
        }
    }
}
