package com.amigoliro.backend.sorteio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

// Teste unitário puro: sem @SpringBootTest, sem Testcontainers. Roda em milissegundos.
class SorteadorTest {

    // Semente fixa: se um teste falhar, ele falha sempre do mesmo jeito (dá para depurar).
    private final Sorteador sorteador = new Sorteador(new Random(42));

    @Test
    void ninguemTiraASiMesmo() {
        List<UUID> participantes = participantes(5);

        Map<UUID, UUID> pares = sorteador.sortear(participantes, Set.of());

        assertThat(pares).allSatisfy((quemDa, quemRecebe) ->
                assertThat(quemRecebe).isNotEqualTo(quemDa));
    }

    @Test
    void cadaUmDaERecebeExatamenteUmaVez() {
        List<UUID> participantes = participantes(7);

        Map<UUID, UUID> pares = sorteador.sortear(participantes, Set.of());

        assertThat(pares).containsOnlyKeys(participantes);
        assertThat(pares.values()).containsExactlyInAnyOrderElementsOf(participantes);
    }

    @Test
    void exclusoesSaoRespeitadasNosDoisSentidos() {
        List<UUID> participantes = participantes(4);

        ParExcluido ex1 = new ParExcluido(participantes.get(0), participantes.get(1));
        ParExcluido ex2 = new ParExcluido(participantes.get(2), participantes.get(3));

        Set<ParExcluido> exclusoes = Set.of(ex1, ex2);

        for (int i = 0; i < 100; i++) {
            Map<UUID, UUID> pares = sorteador.sortear(participantes, exclusoes);

            for (ParExcluido ex : exclusoes) {
                assertThat(pares.get(ex.a())).isNotEqualTo(ex.b());
                assertThat(pares.get(ex.b())).isNotEqualTo(ex.a());
            }
        }
    }

    @Test
    void casoImpossivelLancaExcecao() {
        List<UUID> participantes = participantes(3);
        ParExcluido ex1 = new ParExcluido(participantes.get(0), participantes.get(1));
        Set<ParExcluido> exclusoes = Set.of(ex1);

        assertThatThrownBy(() -> sorteador.sortear(participantes, exclusoes))
                .isInstanceOf(IllegalStateException.class);
    }

    private static List<UUID> participantes(int quantidade) {
        return Stream.generate(UUID::randomUUID).limit(quantidade).toList();
    }
}
