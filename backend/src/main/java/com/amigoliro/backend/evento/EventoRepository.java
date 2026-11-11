package com.amigoliro.backend.evento;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// Só a interface: o Spring Data cria a implementação na subida do app.
// JpaRepository<Entidade, TipoDoId> já traz save, findById, findAll, delete...
public interface EventoRepository extends JpaRepository<Evento, UUID> {

    // Query derivada do nome do método: vira
    // SELECT ... FROM evento WHERE codigo_publico = ?
    // Optional porque o código pode não existir (link errado) e quem chama decide o 404.
    Optional<Evento> findByCodigoPublico(String codigoPublico);

    // Toda requisição de admin passa por aqui; o uk_evento_token_admin_hash é o índice que deixa rápido.
    Optional<Evento> findByTokenAdminHash(String tokenAdminHash);
}
