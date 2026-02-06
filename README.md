# Garden Hostel - API de Gestão Hoteleira


![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-4.0.1-blue)

## Descrição do projeto 🏨

O Garden Hostel é um sistema robusto de backend desenvolvido para a gestão completa de hostels e albergues. O projeto representa uma refatoração profunda de um sistema legado em PHP, transformando-o em uma **API RESTful** moderna e escalável.

A API gerencia o fluxo de ponta a ponta: desde a disponibilidade de vagas individuais (camas) até o controle de check-in, check-out, regras de limpeza e processamento de pagamentos presenciais e online.

## Começando
Para rodar o projeto localmente, você precisará:

- Java 21+
- Maven
- PostgreSQL (ou H2 para ambiente de teste)
- IDE de sua preferência (IntelliJ, Eclipse, VSCode)

### Configuração do banco de dados

No `application.properties`, configure a conexão com seu banco PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/alberguedb
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```
## Desenvolvimento

Para iniciar o desenvolvimento, é necessário clonar o projeto do GitHub:

```shell
  cd "diretorio de sua preferencia"
git clone https://github.com/victorpizzarro/Backend-GardenHostel_Java
```

### Construção e Execução

Para compilar e rodar o projeto, utilize o Maven:

```shell
  mvn clean install
  mvn spring-boot:run
```

## Segurança e Níveis de Acesso 🔐

A API utiliza Spring Security + JWT para garantir que apenas usuários autorizados acessem recursos específicos:

- ROLE_ADMIN: Gestão total do sistema, incluindo deleção de registros e controle de usuários.


- ROLE_ATENDENTE: Focado na operação diária: reservas de balcão, check-ins e check-outs.


- ROLE_CLIENTE: Acesso ao portal do hóspede para reservas online e histórico pessoal.

## API Endpoints
Caso o Swagger não esteja disponível, você pode testar a API usando Postman ou curl.

*Base URL: http://localhost:8080*

- Autenticação
```
POST /auth/login - Retorna o Token JWT.

GET /auth/me - Retorna os dados do usuário logado através do token.
```
- Vagas (Camas)

```
GET /api/vagas/disponiveis - Lista camas livres no período.
```

- Reservas
```
POST http://localhost:8080/api/reservas
Content-Type: application/json

{
  "dataCheckin": "2026-02-10T14:00:00",
  "dataCheckout": "2026-02-15T12:00:00",
  "origemReserva": "SITE",
  "clienteId": "uuid-do-cliente",
  "vagaId": 1
}

RESPONSE 201 Created
```

- Check-in / Check-out
```
PATCH /api/reservas/{id}/checkin - Realiza o check-in e altera status da vaga para OCUPADA.

PATCH /api/reservas/{id}/checkout - Finaliza estadia e altera status da vaga para LIMPEZA.
```

## Tecnologias e Boas Práticas
### Backend e Frameworks:
- Java 21+: Utilização das versões mais recentes da linguagem para performance e segurança.


- Spring Boot 4: Base para a criação da API REST, Injeção de Dependência e Inversão de Controle.


- Spring Data JPA: Abstração de persistência para PostgreSQL (Produção) e H2 (Testes).


- Spring Security + JWT: Proteção de endpoints e autenticação stateless.


- PostgreSQL: Banco de dados relacional robusto para integridade dos dados hoteleiros.

### Boas práticas de desenvolvimento:
- Refatoração de Legado: Migração de lógica PHP procedural para Orientação a Objetos avançada em Java.


- Arquitetura em Camadas: Separação clara entre Controllers, Services, DTOs e Repositories (SOLID).


- Tratamento de Erros: Respostas HTTP consistentes e validações com jakarta.validation.


- Regras de Negócio Complexas: Implementação de travas para impedir check-outs antes de check-ins ou reservas em datas retroativas.


- Clean Code: Métodos pequenos, nomes semânticos e código autodocumentado.

## Links

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Springdoc OpenAPI](https://springdoc.org/)
- [H2 Database](https://www.h2database.com/html/main.html)
- [PostgreSQL](https://www.postgresql.org/)
- [Swagger - Servidor On ](http://localhost:8080/swagger-ui/index.html)

```
Obs. Para acessar a documentação interativa da API, rode o projeto localmente e acesse o Swagger UI no link acima.
```

**Desenvolvido por Victor Pizzarro** Estudante de Análise e Desenvolvimento de Sistemas na FAETERJ. Desenvolvedor Backend apaixonado por Java e boas práticas de arquitetura.
- [Dúvidas? Me chame no LikedIn](https://www.linkedin.com/in/victor-pizzarro/)