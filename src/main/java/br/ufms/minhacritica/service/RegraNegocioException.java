package br.ufms.minhacritica.service;

/** Lançada quando uma regra de negócio é violada; a mensagem é exibida ao usuário. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
