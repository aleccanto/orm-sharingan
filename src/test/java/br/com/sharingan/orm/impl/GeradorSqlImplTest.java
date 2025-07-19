package br.com.sharingan.orm.impl;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import br.com.sharingan.orm.GeradorSql;
import br.com.sharingan.orm.modelo.Carro;
import br.com.sharingan.orm.modelo.Pessoa;

public class GeradorSqlImplTest {

    @Test
    public void testarGerarInsertPessoa() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSql.gerarInsert(pessoa);

        Assert.assertEquals("INSERT INTO pessoas (nome) VALUES (?)", sqlGerado);
    }

    @Test
    public void testarGerarSelectTodos() {

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarSelectTodos();

        Assert.assertEquals("SELECT * FROM pessoas", sqlGerado);
    }

    @Test
    public void testarGerarSelectPorIdPessoa() {

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarSelectPorId(1L);

        Assert.assertEquals("SELECT * FROM pessoas WHERE id = ?", sqlGerado);

    }

    @Test
    public void testarGerarUpdatePessoa() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSql.gerarUpdate(pessoa);

        Assert.assertEquals("UPDATE pessoas SET nome = ? WHERE id = 1", sqlGerado);
    }

    @Test
    public void testarGerarDeleteTodosPessoa() {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarDeleteTodos();

        Assert.assertEquals("DELETE FROM pessoas WHERE id > 0", sqlGerado);
    }

    @Test
    public void testarGerarSelectTodosCarro() {

        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        String sqlGerado = geradorSql.gerarSelectTodos();

        Assert.assertEquals("SELECT * FROM carros", sqlGerado);

    }

    @Test
    public void testarGerarInsertCarro() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        Carro carro = new Carro();
        carro.setNome("Carro");
        carro.setPreco(BigDecimal.valueOf(40000));
        carro.setPeso(500000D);

        String sqlGerado = geradorSql.gerarInsert(carro);

        Assert.assertEquals("INSERT INTO carros (nome, preco, peso) VALUES (?, ?, ?)", sqlGerado);
    }

    @Test
    public void testarGerarUpdateCarro() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setNome("Carro");
        carro.setPreco(BigDecimal.valueOf(40000));
        carro.setPeso(500000D);

        String sqlGerado = geradorSql.gerarUpdate(carro);

        Assert.assertEquals("UPDATE carros SET nome = ?, preco = ?, peso = ? WHERE id = 1", sqlGerado);
    }
}
