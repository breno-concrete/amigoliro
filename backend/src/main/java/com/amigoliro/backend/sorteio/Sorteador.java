package com.amigoliro.backend.sorteio;

import java.util.*;

// Java puro: sem @Component, sem entidade, sem banco. Entra lista de ids, sai o mapa
// "quem dá → quem recebe". Assim dá para testar milhares de sorteios em milissegundos.
public class Sorteador {

    // Recebido de fora (não criado aqui): no app vai ser um SecureRandom (FR-020);
    // no teste, um Random com semente fixa, para o resultado ser repetível.
    private final Random random;

    public Sorteador(Random random) {
        this.random = random;
    }

    public Map<UUID, UUID> sortear(List<UUID> participantes, Set<ParExcluido> exclusoes) {

        List<UUID> listaCopia = new ArrayList<>(participantes);
        Collections.shuffle(listaCopia, random);

        Map<UUID, UUID> resultado = new HashMap<>();
        Set<UUID> jaRecebeu = new HashSet<>();

        if (!escolherAmigo(0, resultado, jaRecebeu, listaCopia, exclusoes)) {
            throw new SorteioImpossivelException();
        }
        return resultado;
    }

    private boolean escolherAmigo(int posicao, Map<UUID, UUID> resultado, Set<UUID> jaRecebeu, List<UUID> participantes, Set<ParExcluido> exclusoes) {
        if (posicao >= participantes.size()) {
            return true;
        }
        UUID doador = participantes.get(posicao);
        List<UUID> copiaParticipantes = new ArrayList<>(participantes);
        Collections.shuffle(copiaParticipantes, random);

        for (UUID candidato : copiaParticipantes) {
            if (!podeTirar(doador, candidato, exclusoes, jaRecebeu )) {
                continue;
            }

            resultado.put(doador, candidato);
            jaRecebeu.add(candidato);

            if(escolherAmigo(posicao + 1, resultado, jaRecebeu, participantes, exclusoes)) {
                return true;
            }

            resultado.remove(doador);
            jaRecebeu.remove(candidato);
        }

        return false;
    }

    private boolean podeTirar(UUID doador, UUID candidato, Set<ParExcluido> exclusoes, Set<UUID> jaRecebeu) {
        return !doador.equals(candidato) && !exclusoes.contains(new ParExcluido(doador, candidato)) && !exclusoes.contains(new ParExcluido(candidato, doador)) && !jaRecebeu.contains(candidato);
    }
}
