package br.ufms.minhacritica.service;

import br.ufms.minhacritica.dto.CriticaForm;
import br.ufms.minhacritica.model.Critica;
import br.ufms.minhacritica.model.Obra;
import br.ufms.minhacritica.model.Usuario;
import br.ufms.minhacritica.repository.CriticaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CriticaService {

    private final CriticaRepository criticaRepository;
    private final ObraService obraService;
    private final UsuarioService usuarioService;

    public CriticaService(CriticaRepository criticaRepository, ObraService obraService,
                          UsuarioService usuarioService) {
        this.criticaRepository = criticaRepository;
        this.obraService = obraService;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public boolean jaCriticou(Long usuarioId, Long obraId) {
        return criticaRepository.existsByAutorIdAndObraId(usuarioId, obraId);
    }

    /** RN03: uma crítica por usuário em cada obra, nota de 1 a 5. */
    @Transactional
    public Critica publicar(Long obraId, CriticaForm form, Long usuarioId) {
        if (jaCriticou(usuarioId, obraId)) {
            throw new RegraNegocioException("Você já publicou uma crítica para esta obra.");
        }
        Obra obra = obraService.buscar(obraId);
        Usuario autor = usuarioService.buscarPorId(usuarioId);
        Critica critica = new Critica(form.getNota(), form.getComentario().trim(), autor, obra);
        obra.getCriticas().add(critica);
        return criticaRepository.save(critica);
    }

    /**
     * RN04: o autor pode excluir a própria crítica; o administrador pode excluir qualquer uma (moderação).
     * @return id da obra, para redirecionar de volta à página dela
     */
    @Transactional
    public Long excluir(Long criticaId, UsuarioLogado usuario) {
        Critica critica = criticaRepository.findById(criticaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crítica não encontrada"));
        boolean ehAutor = critica.getAutor().getId().equals(usuario.getId());
        if (!ehAutor && !usuario.isAdmin()) {
            throw new AccessDeniedException("Você não pode excluir a crítica de outra pessoa.");
        }
        Obra obra = critica.getObra();
        obra.getCriticas().remove(critica);
        criticaRepository.delete(critica);
        return obra.getId();
    }
}
