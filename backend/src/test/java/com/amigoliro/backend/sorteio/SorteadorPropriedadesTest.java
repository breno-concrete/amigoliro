package com.amigoliro.backend.sorteio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

// Teste de propriedade (SC-003): em vez de escolher 1 cenário à mão, gera milhares de
// cenários aleatórios e confere que as REGRAS valem em todos eles.
class SorteadorPropriedadesTest {

    private static final int EXECUCOES = 10_000;

    // Trava de segurança: backtracking pode explodir em casos patológicos; se um dia
    // isso acontecer, o teste falha em vez de travar o CI para sempre.
    @Test
    @Timeout(60)
    void dezMilSorteiosAleatoriosSemNenhumaViolacao() {
        int impossiveis = 0;

        for (int i = 0; i < EXECUCOES; i++) {
            // Uma semente por execução: se a execução 7342 falhar, a mensagem mostra a
            // semente, e dá para reproduzir exatamente aquele cenário.
            long semente = 1_000L + i;
            Random gerador = new Random(semente);

            List<UUID> participantes = participantes(3 + gerador.nextInt(28)); // 3 a 30
            Set<ParExcluido> exclusoes = exclusoesAleatorias(participantes, gerador);
            Sorteador sorteador = new Sorteador(new Random(semente));

            try {
                Map<UUID, UUID> pares = sorteador.sortear(participantes, exclusoes);
                assertSorteioValido(participantes, exclusoes, pares, "semente " + semente);
            } catch (SorteioImpossivelException e) {
                // Aqui só contamos: quem prova que era impossível MESMO é o
                // casosImpossiveisBatemComForcaBruta, em cenários pequenos.
                impossiveis++;
            }
        }

        // Garante que o gerador está produzindo casos possíveis de verdade;
        // se quase tudo fosse impossível, o teste acima não estaria provando nada.
        assertThat(impossiveis).isLessThan(EXECUCOES / 2);
    }

    // Segunda metade do SC-003: o Sorteador nunca diz "impossível" quando existe solução,
    // e nunca entrega resultado quando não existe. Quem decide o certo é a força bruta.
    @Test
    @Timeout(60)
    void casosImpossiveisBatemComForcaBruta() {
        int impossiveis = 0;

        for (int i = 0; i < 2_000; i++) {
            long semente = 50_000L + i;
            Random gerador = new Random(semente);

            List<UUID> participantes = participantes(3 + gerador.nextInt(5)); // 3 a 7: o vovô dá conta
            Set<ParExcluido> exclusoes = exclusoesAleatorias(participantes, gerador);
            Sorteador sorteador = new Sorteador(new Random(semente));

            if (existeSorteioValido(participantes, exclusoes)) {
                assertThatCode(() -> sorteador.sortear(participantes, exclusoes))
                        .as("semente %d: existe solução, o Sorteador não podia desistir", semente)
                        .doesNotThrowAnyException();
            } else {
                impossiveis++;
                assertThatThrownBy(() -> sorteador.sortear(participantes, exclusoes))
                        .as("semente %d: não existe solução, o Sorteador tinha que avisar", semente)
                        .isInstanceOf(SorteioImpossivelException.class);
            }
        }

        // Se o gerador nunca produzisse um caso impossível, metade deste teste não rodaria.
        assertThat(impossiveis).isGreaterThan(0);
    }

    // ---- O "vovô": força bruta, de propósito sem nenhum atalho ----

    // Existe ALGUMA forma de distribuir os amigos que respeite as regras?
    static boolean existeSorteioValido(List<UUID> participantes, Set<ParExcluido> exclusoes) {
        return montarPermutacoes(participantes, exclusoes, new ArrayList<>(), new HashSet<>());
    }

    // Monta todas as listas "quem recebe" possíveis, uma posição por vez,
    // e só confere as regras quando a lista está completa.
    private static boolean montarPermutacoes(List<UUID> participantes, Set<ParExcluido> exclusoes,
                                             List<UUID> permutacao, Set<UUID> usados) {
        if (permutacao.size() == participantes.size()) {
            return distribuicaoValida(participantes, permutacao, exclusoes);
        }
        for (UUID pessoa : participantes) {
            if (usados.contains(pessoa)) {
                continue; // a mesma pessoa não pode aparecer duas vezes na lista
            }
            permutacao.add(pessoa);
            usados.add(pessoa);
            if (montarPermutacoes(participantes, exclusoes, permutacao, usados)) {
                return true; // achou uma válida: a resposta já é "sim"
            }
            permutacao.remove(permutacao.size() - 1);
            usados.remove(pessoa);
        }
        return false;
    }

    // Confere uma lista pronta: participantes.get(i) tira permutacao.get(i).
    // Escrito do zero (sem usar o podeTirar do Sorteador): o oráculo precisa ser independente.
    private static boolean distribuicaoValida(List<UUID> participantes, List<UUID> permutacao,
                                              Set<ParExcluido> exclusoes) {
        for (int i = 0; i < participantes.size(); i++) {
            UUID quemDa = participantes.get(i);
            UUID quemRecebe = permutacao.get(i);
            if (quemDa.equals(quemRecebe)
                    || exclusoes.contains(new ParExcluido(quemDa, quemRecebe))
                    || exclusoes.contains(new ParExcluido(quemRecebe, quemDa))) {
                return false;
            }
        }
        return true;
    }

    // As três regras do FR-018 num lugar só, reutilizáveis por qualquer teste.
    static void assertSorteioValido(List<UUID> participantes, Set<ParExcluido> exclusoes,
                                    Map<UUID, UUID> pares, String contexto) {
        assertThat(pares).as(contexto).containsOnlyKeys(participantes);
        assertThat(pares.values()).as(contexto).containsExactlyInAnyOrderElementsOf(participantes);
        pares.forEach((quemDa, quemRecebe) -> {
            assertThat(quemRecebe).as(contexto).isNotEqualTo(quemDa);
            assertThat(exclusoes).as(contexto)
                    .doesNotContain(new ParExcluido(quemDa, quemRecebe), new ParExcluido(quemRecebe, quemDa));
        });
    }

    // Até n-1 exclusões entre pares aleatórios, guardadas numa ordem qualquer
    // (o Sorteador precisa funcionar nos dois sentidos).
    private static Set<ParExcluido> exclusoesAleatorias(List<UUID> participantes, Random gerador) {
        int n = participantes.size();
        int quantidade = gerador.nextInt(n);
        Set<ParExcluido> exclusoes = new HashSet<>();
        for (int k = 0; k < quantidade; k++) {
            UUID a = participantes.get(gerador.nextInt(n));
            UUID b = participantes.get(gerador.nextInt(n));
            if (!a.equals(b)) {
                exclusoes.add(new ParExcluido(a, b));
            }
        }
        return exclusoes;
    }

    private static List<UUID> participantes(int quantidade) {
        return Stream.generate(UUID::randomUUID).limit(quantidade).toList();
    }
}
