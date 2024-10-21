## Gerenciamento de Processos Jurídicos - Microsserviço
Este projeto tem como objetivo o desenvolvimento de um microsserviço para gerenciar processos jurídicos, incluindo partes envolvidas, ações e status do processo.

### Escopo
O sistema permitirá o gerenciamento completo de processos jurídicos, incluindo:

* Cadastro e consulta de processos.
* Cadastro e gerenciamento de partes envolvidas (autor, réu, advogado).
* Registro de ações associadas aos processos (petições, audiências, sentenças).
* Atualização do status dos processos (ativo, suspenso ou arquivado).

## Objetivos
* Gerenciar Processos Jurídicos: Permitir criar, editar, consultar e arquivar processos.
* Gerenciar Partes Envolvidas: Registrar uma ou mais partes (autor, réu, advogado) para cada processo.
* Gerenciar Ações: Registrar ações associadas a cada processo, como petições, audiências e sentenças.
* Status do Processo: Indicar o estado atual do processo, como ativo, suspenso ou arquivado.

### Arquitetura
O projeto segue uma arquitetura de microsserviços, onde um único serviço é responsável por gerenciar todas as funcionalidades do sistema de processos jurídicos.

### Tecnologias Utilizadas
* Java 17 com Spring Boot: Base do microsserviço.
* Spring Data JPA: Interação com o banco de dados.
* Banco de Dados: PostgreSQL ou MySQL para a persistência dos dados.
* Jacoco: Ferramenta para cobertura de testes.
* SonarQube: Para análise de qualidade do código.

### Cobertura de Testes
Foi utilizado o Jacoco para realizar a análise de cobertura de testes do sistema. A cobertura dos testes pode ser visualizada ao executar o comando:


```
mvn test
```

Os relatórios serão gerados na pasta target/site/jacoco.

### Análise de Qualidade de Código
A análise de qualidade de código é feita utilizando o SonarQube. Para executar a análise localmente, utilize os seguintes comandos:

```
mvn sonar:sonar 
```

Verifique o relatório de análise no dashboard do SonarQube.

#### Instalação e Configuração

Pré-requisitos:
* Java 17
* Maven
* PostgreSQL ou MySQL

#### Passos para Executar o Projeto
Clone o repositório:
```
git clone https://github.com/usuario/projeto-juridico.git
```

#### Configure o banco de dados no arquivo application.properties:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/seu-banco
spring.datasource.username=usuario
spring.datasource.password=senha
```

### Execute o projeto:

```
mvn spring-boot:run
```

#### Testes
Para executar os testes unitários, utilize o comando:

```
mvn test
```
