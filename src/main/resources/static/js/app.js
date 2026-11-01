// Minha Crítica — comportamentos leves de interface (o site funciona sem JavaScript).
document.addEventListener('DOMContentLoaded', () => {

    // Confirmação antes de enviar formulários de exclusão: <form data-confirmar="mensagem">
    document.querySelectorAll('form[data-confirmar]').forEach((form) => {
        form.addEventListener('submit', (evento) => {
            if (!window.confirm(form.dataset.confirmar)) {
                evento.preventDefault();
            }
        });
    });

    // Contador de caracteres anunciado por leitores de tela: <textarea data-contador="id-do-elemento">
    document.querySelectorAll('textarea[data-contador]').forEach((campo) => {
        const saida = document.getElementById(campo.dataset.contador);
        if (!saida) return;
        const textoOriginal = saida.textContent;
        const atualizar = () => {
            const total = campo.value.length;
            saida.textContent = total === 0
                ? textoOriginal
                : `${total} de ${campo.maxLength} caracteres usados.`;
        };
        campo.addEventListener('input', atualizar);
        atualizar();
    });

    // Leva o foco para a primeira mensagem de erro após enviar um formulário inválido.
    const primeiroErro = document.querySelector('[aria-invalid="true"]');
    if (primeiroErro) {
        primeiroErro.focus();
    }
});
