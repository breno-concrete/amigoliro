package com.amigoliro.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

// Configuração só de teste: sobe um Postgres descartável em Docker para cada execução da suíte.
// Mesma versão do docker-compose.yml, para o teste rodar contra o mesmo banco do dev.
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    // @ServiceConnection: o Spring lê URL, usuário e senha do container (porta aleatória)
    // e sobrescreve o spring.datasource.* do application.properties. Sem isso, o teste
    // tentaria o localhost:5433, que no CI não existe.
    @Bean
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return new PostgreSQLContainer("postgres:17");
    }
}
