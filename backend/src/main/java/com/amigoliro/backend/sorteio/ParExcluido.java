package com.amigoliro.backend.sorteio;

import java.util.UUID;

// Dois participantes que não podem se tirar, em nenhum dos dois sentidos (a exclusão é mútua, FR-017).
// É um record, não a entidade Exclusao: o Sorteador não conhece JPA nem banco.
public record ParExcluido(UUID a, UUID b) {
}
