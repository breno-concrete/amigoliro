package com.amigoliro.backend.exclusao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExclusaoRepository extends JpaRepository<Exclusao, UUID> {
}
