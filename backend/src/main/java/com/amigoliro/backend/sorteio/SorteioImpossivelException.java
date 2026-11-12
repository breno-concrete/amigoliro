package com.amigoliro.backend.sorteio;

// Caso impossível do FR-019: as exclusões não deixam nenhuma combinação válida.
// RuntimeException (unchecked) por dois motivos:
// 1. o @Transactional do Service (Dia 8) só faz rollback sozinho com RuntimeException,
//    e o FR-019 exige que o evento NÃO seja alterado;
// 2. o @RestControllerAdvice (Dia 5) vai capturar este tipo e responder com uma mensagem
//    em linguagem simples, sem obrigar todo método do caminho a declarar "throws".
public class SorteioImpossivelException extends RuntimeException {

    public SorteioImpossivelException() {
        super("Não existe sorteio possível com essas exclusões. Remova alguma exclusão e tente de novo.");
    }
}
