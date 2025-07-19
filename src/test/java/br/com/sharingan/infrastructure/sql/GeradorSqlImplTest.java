package br.com.sharingan.infrastructure.sql;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import br.com.sharingan.infrastructure.orm.sql.GeradorSqlImpl;
import br.com.sharingan.domain.orm.sql.GeradorSql;
import br.com.sharingan.infrastructure.model.Carro;
import br.com.sharingan.infrastructure.model.Pessoa;

public class GeradorSqlImplTest {
    @Test
    public void testGerarInsertPessoa() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSql.gerarInsert(pessoa);

        Assert.assertEquals("INSERT INTO pessoa (nome) VALUES (?)", sqlGerado);
    }

    @Test
    public void testGerarSelectAll() {

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarSelectTodos();

        Assert.assertEquals("SELECT * FROM pessoa", sqlGerado);
    }

    @Test
    public void testGerarSelectPorIdPessoa() {

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarSelectPorId(1L);

        Assert.assertEquals("SELECT * FROM pessoa WHERE id = ?", sqlGerado);

    }

    @Test
    public void testGerarUpdatePessoa() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSql.gerarUpdate(pessoa);

        Assert.assertEquals("UPDATE pessoa SET nome = ? WHERE id = 1", sqlGerado);
    }

    @Test
    public void testGerarDeleteTodosPessoa() {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarDeleteTodos();

        Assert.assertEquals("DELETE FROM pessoa WHERE id > 0", sqlGerado);
    }

    @Test
    public void testGerarSelectAllCarro() {

        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        String sqlGerado = geradorSql.gerarSelectTodos();

        Assert.assertEquals("SELECT * FROM carro", sqlGerado);

    }

    @Test
    public void testGerarInsertCarro() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        Carro carro = new Carro();
        carro.setNome("Carro");
        carro.setPreco(BigDecimal.valueOf(40000));
        carro.setPeso(500000D);

        String sqlGerado = geradorSql.gerarInsert(carro);

        Assert.assertEquals("INSERT INTO carro (nome, preco, peso) VALUES (?, ?, ?)", sqlGerado);
    }

    @Test
    public void testGerarUpdateCarro() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Carro> geradorSql = new GeradorSqlImpl<>(Carro.class);

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setNome("Carro");
        carro.setPreco(BigDecimal.valueOf(40000));
        carro.setPeso(500000D);

        String sqlGerado = geradorSql.gerarUpdate(carro);

        Assert.assertEquals("UPDATE carro SET nome = ?, preco = ?, peso = ? WHERE id = 1", sqlGerado);
    }
}
