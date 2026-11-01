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

    // Capa com imagem quebrada: remove a imagem e deixa aparecer a capa desenhada pelo site.
    document.querySelectorAll('img[data-capa]').forEach((img) => {
        const remover = () => img.remove();
        if (img.complete && img.naturalWidth === 0) remover();
        img.addEventListener('error', remover);
    });

    // Texto ao lado das estrelas ("4 de 5 — muito bom"), só visual; o rádio já é anunciado.
    const nomes = ['', 'Ruim', 'Fraco', 'Bom', 'Muito bom', 'Excelente'];
    document.querySelectorAll('.opcoes-nota').forEach((grupo) => {
        const texto = grupo.querySelector('[data-nota-texto]');
        if (!texto) return;
        const atualizar = () => {
            const marcado = grupo.querySelector('input:checked');
            texto.textContent = marcado ? `${marcado.value} de 5 · ${nomes[marcado.value]}` : 'Escolha uma nota';
        };
        grupo.addEventListener('change', atualizar);
        atualizar();
    });

    // Leva o foco para o primeiro campo com erro após enviar um formulário inválido.
    const primeiroErro = document.querySelector('[aria-invalid="true"]');
    if (primeiroErro) {
        primeiroErro.focus();
    }
});
