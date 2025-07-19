# ORM Sharingan: Onde seus dados ganham vida com a visão mais aguçada! 👁️‍🗨️

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2%20Database-4479A1?style=for-the-badge&logo=h2database&logoColor=white)

## Visão Geral

O **ORM Sharingan** é um projeto de Object-Relational Mapping (ORM) desenvolvido em Java, projetado para simplificar a interação com bancos de dados relacionais. Assim como o Sharingan concede ao usuário a capacidade de ver e prever movimentos, nosso ORM oferece a você a *visão* para mapear seus objetos Java diretamente para o banco de dados, *prevendo* e automatizando as complexidades do SQL. Diga adeus ao SQL boilerplate e deixe o Sharingan fazer o trabalho pesado!

## Funcionalidades

*   **Mapeamento Objeto-Relacional:** Mapeia automaticamente objetos Java para tabelas de banco de dados e vice-versa.
*   **Operações CRUD:** Suporte completo para operações de **Criação (Create)**, **Leitura (Read)**, **Atualização (Update)** e **Exclusão (Delete)**.
*   **Geração de SQL:** Gera automaticamente comandos SQL com base nos objetos Java e suas anotações.
*   **Conexão com Banco de Dados:** Gerenciamento de conexão com banco de dados utilizando [HikariCP](https://github.com/brettwooldridge/HikariCP) para pooling de conexões de alta performance.
*   **Suporte a H2 (para Testes):** Configuração para uso de banco de dados em memória [H2 Database](http://www.h2database.com/html/main.html) para facilitar a execução de testes, garantindo um ambiente isolado e rápido.

## Utilizando Anotações

O ORM Sharingan utiliza anotações para mapear suas classes Java para as tabelas do banco de dados, oferecendo flexibilidade e clareza no mapeamento.

### `@Entidade`

**Obrigatória.** Marca uma classe como uma entidade gerenciada pelo ORM. Classes anotadas com `@Entidade` são elegíveis para mapeamento e operações de persistência.

### `@Tabela`

Utilizada para especificar o nome da tabela no banco de dados à qual a classe está mapeada. Se não for especificada, o nome da classe (em minúsculas e **pluralizado automaticamente**) será usado como nome da tabela.

```java
@Tabela("minha_tabela_personalizada")
public class MeuObjeto {
    // ...
}
```

### `@Id`

**Obrigatória.** Marca um campo como a chave primária da tabela. O ORM utilizará este campo para operações de busca por ID e atualizações. Se o `value` não for especificado, o nome do campo será usado como o nome da coluna da chave primária.

```java
public class MeuObjeto {
    @Id("id_personalizado")
    private Long id;
    // ...
}
```

### `@Coluna`

Utilizada para especificar o nome da coluna no banco de dados à qual um campo da classe está mapeado. Se não for especificada, o nome do campo (em minúsculas) será usado como o nome da coluna.

```java
public class MeuObjeto {
    @Coluna("nome_completo")
    private String nome;
    // ...
}
```

## Cobertura de Testes

O projeto possui uma suíte de testes abrangente, cobrindo:

*   **Testes de Geração de SQL:** Verificam a correção dos comandos SQL gerados para operações CRUD, incluindo cenários com campos nulos e uso explícito de anotações `@Tabela`, `@Id` e `@Coluna`.
*   **Testes de Repositório:** Validam as operações de persistência e recuperação de dados (CRUD) no banco de dados H2 em memória, incluindo casos positivos e negativos (e.g., busca por ID inexistente, exclusão de registros em tabelas vazias).

## Tecnologias Utilizadas

*   [Java 11+](https://www.java.com/): Linguagem de programação orientada a objetos.
*   [Apache Maven](https://maven.apache.org/): Gerenciador de pacotes.
*   [SLF4J](https://www.slf4j.org/): Fachada de logging.
*   [PostgreSQL](https://www.postgresql.org/): Banco de dados relacional (configuração padrão).
*   [H2 Database](http://www.h2database.com/html/main.html): Banco de dados em memória (para testes).
*   [HikariCP](https://github.com/brettwooldridge/HikariCP): Framework de pooling de conexões.
*   [JUnit 4](https://junit.org/junit4/): Framework de testes unitários.

## Primeiros Passos

### Pré-requisitos

Certifique-se de ter o seguinte instalado em sua máquina:

*   Java Development Kit (JDK) 11 ou superior
*   Apache Maven 3.6.0 ou superior

### Construindo o Projeto

Para compilar o projeto e baixar todas as dependências, execute o seguinte comando Maven:

```bash
mvn clean install
```

## Configuração do Banco de Dados

Por padrão, o ORM Sharingan tenta se conectar a um banco de dados PostgreSQL em `localhost:5432` com o banco de dados `myorm`, usuário `postgres` e senha `root`.

Você pode alterar essas configurações no construtor `ConexaoFactoryImpl()` em `src/main/java/br/com/sharingan/orm/impl/ConexaoFactoryImpl.java`.

Para testes, o projeto utiliza um banco de dados H2 em memória. A tabela `pessoa` é criada automaticamente antes da execução dos testes.

## Executando os Testes

Para executar a suíte de testes unitários, que utiliza o banco de dados H2 em memória, execute:

```bash
mvn test
```

## Exemplo de Uso (Básico)

```java
// Exemplo de Entidade
@Tabela("pessoas")
public class Pessoa {
    @Id("id")
    private Long id;

    @Coluna("nome")
    private String nome;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

// No seu código principal
public class Main {
    public static void main(String[] args) {
        ConexaoFactory conexaoFactory = new ConexaoFactoryImpl();
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);
        Repositorio<Pessoa> repositorioPessoa = new RepositorioImpl<>(conexaoFactory, geradorSql);

        // Criar uma nova pessoa
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("João Silva");
        repositorioPessoa.criar(novaPessoa);
        System.out.println("Pessoa criada com ID: " + novaPessoa.getId());

        // Buscar todas as pessoas
        List<Pessoa> pessoas = repositorioPessoa.buscarTodos();
        pessoas.forEach(p -> System.out.println("ID: " + p.getId() + ", Nome: " + p.getNome()));

        // Atualizar uma pessoa
        if (!pessoas.isEmpty()) {
            Pessoa primeiraPessoa = pessoas.get(0);
            primeiraPessoa.setNome("João Atualizado");
            repositorioPessoa.atualizar(primeiraPessoa);
            System.out.println("Pessoa atualizada: " + primeiraPessoa.getNome());
        }

        // Excluir todas as pessoas
        repositorioPessoa.excluirTodos();
        System.out.println("Todas as pessoas foram excluídas.");

        conexaoFactory.fechar(); // Fechar o pool de conexões
    }
}
```
