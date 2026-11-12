package com.amigoliro.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// Sobe o app inteiro contra o Postgres do Testcontainers: prova que o Flyway roda
// e que o ddl-auto=validate aceita as entidades.
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
