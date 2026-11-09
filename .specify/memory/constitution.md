# Amigo da Onça Constitution

Plataforma web de sorteio de amigo oculto, aberta a qualquer grupo e ocasião (amigos, família, empresas), com visual low-poly amigável e uso pensado primeiro para o celular.

## Core Principles

### I. Sigilo do Sorteio (NÃO NEGOCIÁVEL)

O resultado do sorteio de um participante só pode ser visto por esse participante.

- Ninguém, incluindo o organizador, pode ver quem outra pessoa tirou por nenhuma tela, endpoint, mensagem de erro, log ou exportação.
- A escolha de identidade pelo link do evento acontece somente **antes** do sorteio. Depois do sorteio, o acesso de quem não se identificou é feito apenas por link individual gerado pelo organizador.
- Todo código, token ou identificador exposto em URL é gerado com fonte criptograficamente segura e tem no mínimo 128 bits de entropia. IDs sequenciais do banco nunca aparecem em URLs ou respostas públicas.
- Qualquer ação do organizador que refaça o acesso de um participante deixa um aviso visível para esse participante.
- Logs nunca registram pares do sorteio, tokens de acesso ou listas de desejos.
- Toda rota acessível com o token de administração tem teste automatizado garantindo que a resposta não contém o resultado de nenhum participante.

### II. Mobile-First

O produto é desenhado para o celular; o desktop é uma adaptação.

- Todas as telas são projetadas e validadas primeiro em 360px de largura. Estilos de telas maiores são adicionados com media queries de largura mínima.
- Alvos de toque têm no mínimo 44×44px, e todos os fluxos podem ser concluídos com uma mão.
- Nenhum fluxo depende de hover, teclado físico ou tela grande.
- Toda funcionalidade é validada em um celular Android físico antes de ser considerada pronta.

### III. Zero Atrito

Participar de um amigo oculto não pode exigir esforço.

- Participantes nunca criam conta, fazem login ou instalam app.
- O organizador cria um evento e compartilha o link em até 2 minutos.
- Após o sorteio, o participante vê quem tirou em até 3 toques a partir do link.
- Cada tela tem uma única ação principal, visualmente dominante.
- Toda mensagem de erro diz o que aconteceu e o que a pessoa deve fazer em seguida, em linguagem simples.

### IV. 3D como Enfeite, Nunca como Requisito

O 3D low-poly é o diferencial visual, mas o produto funciona por completo sem ele.

- O 3D aparece apenas na revelação do resultado e em elementos decorativos. Formulários, botões e textos são sempre HTML, nunca desenhados no canvas.
- O código 3D é carregado sob demanda, somente na tela que o usa. A primeira tela do produto não carrega bibliotecas 3D.
- Há fallback em 2D (CSS) quando o aparelho não suporta WebGL, quando a pessoa ativa redução de movimento no sistema, ou quando o desempenho medido fica abaixo do alvo.
- Orçamento de desempenho da cena 3D: densidade de pixels limitada a 2, sem sombras em tempo real, sem pós-processamento, total de assets 3D comprimidos de até 500 KB, e renderização pausada quando nada se move ou a aba está em segundo plano.
- O estilo visual é low-poly, amigável e neutro, servindo a qualquer ocasião. Temas específicos (como selva ou Natal) são variações, nunca a identidade base.

### V. Simplicidade e Escopo Rígido

Lançar a tempo da temporada vale mais do que lançar completo.

- Só é implementado o que está na especificação ativa. Ideias novas entram na lista de fora de escopo, não no código.
- Nenhuma abstração é criada sem pelo menos dois usos reais no código.
- Nenhuma dependência nova entra sem resolver um problema presente na especificação ativa.
- Quando prazo e completude entram em conflito, corta-se escopo, nunca qualidade dos princípios I e VI.

### VI. Testes na Regra de Negócio

A lógica do sorteio é o coração do produto e precisa ser provada, não presumida.

- O algoritmo de sorteio e as regras de exclusão são desenvolvidos com testes escritos antes da implementação.
- Os testes do sorteio verificam propriedades em execuções repetidas com sementes variadas: ninguém tira a si mesmo, cada participante dá e recebe exatamente uma vez, todas as exclusões são respeitadas, e combinações impossíveis são detectadas sem alterar o estado do evento.
- As regras de sigilo do princípio I têm testes de integração.
- O CI bloqueia qualquer merge com teste falhando.

### VII. Privacidade e Dados Mínimos

O produto coleta apenas o necessário para o sorteio acontecer, em conformidade com a LGPD.

- Os únicos dados pessoais coletados são o nome (ou apelido) do participante e a lista de desejos que ele mesmo escreve. Telefone, e-mail e documentos não são coletados.
- Os dados de um evento são apagados automaticamente 60 dias após a data do evento. O organizador pode apagar o evento a qualquer momento.
- Não há ferramentas de rastreamento de terceiros que identifiquem participantes. Métricas de uso são apenas agregadas.
- Uma política de privacidade em linguagem simples é publicada antes do lançamento.

## Restrições Técnicas

- **Backend:** Java 21, Spring Boot 3, PostgreSQL, Flyway para migrações de banco e Bean Validation para validação de entrada.
- **Frontend:** React, TypeScript e Vite. 3D com React Three Fiber e drei. Estilos com Tailwind CSS, cujos breakpoints já seguem a lógica mobile-first.
- **Infraestrutura:** Docker para o backend, frontend publicado como site estático, CI/CD com GitHub Actions e HTTPS obrigatório em todos os ambientes.
- **Segurança:** limite de requisições nos endpoints públicos, CORS restrito ao domínio do frontend, tokens de acesso armazenados no banco apenas como hash.
- **Datas:** armazenadas em UTC e exibidas no fuso America/Sao_Paulo.
- **Idioma:** interface em português do Brasil. Nomes de domínio no código em português (Evento, Participante, Sorteio, Exclusao) e termos técnicos em inglês (Controller, Service, Repository).
- **Navegadores suportados:** as duas versões mais recentes de Chrome para Android e Safari para iOS são o alvo principal; navegadores de desktop são suportados como secundários.

## Fluxo de Desenvolvimento

- O trabalho segue o fluxo do Spec Kit: especificação, plano, tarefas e implementação, nessa ordem.
- Cada funcionalidade vive em uma branch no formato `###-nome-da-funcionalidade`, com sua pasta correspondente em `specs/`.
- Toda mudança entra na `main` por pull request com CI verde, mesmo em desenvolvimento solo.
- Mensagens de commit seguem o padrão Conventional Commits.
- Uma funcionalidade só está pronta quando: os testes passam, ela foi validada em celular físico, a home mantém nota de desempenho mobile 90 ou mais no Lighthouse, e nenhum dado protegido pelo princípio I aparece em logs.
- A `main` é publicada automaticamente em produção após o CI.

## Governance

- Esta constituição prevalece sobre qualquer outra prática, preferência ou documento do projeto.
- Todo plano de implementação passa por uma verificação de conformidade com esta constituição antes de começar. Violações só são aceitas se registradas e justificadas na seção de rastreamento de complexidade do plano.
- Emendas são feitas por alteração neste arquivo, com versionamento semântico:
  - **MAJOR:** remoção ou redefinição incompatível de um princípio.
  - **MINOR:** novo princípio ou nova seção.
  - **PATCH:** ajustes de redação ou esclarecimentos sem mudança de regra.
- Toda emenda atualiza a data de última alteração e é revisada contra as especificações e planos em andamento.

**Version**: 1.0.0 | **Ratified**: 2026-11-08 | **Last Amended**: 2026-11-08
