package br.com.sharingan.orm.sql;

import org.junit.Assert;
import org.junit.Test;

import br.com.sharingan.ddd.orm.sql.GeradorSql;
import br.com.sharingan.noorm.model.Pessoa;

public class GeradorSqlImplTest {
    @Test
    public void testGerarInsert() throws IllegalArgumentException, IllegalAccessException {
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
    public void testGerarSelectById() {

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.gerarSelectFindById(1L);

        Assert.assertEquals("SELECT * FROM pessoa WHERE id = ?", sqlGerado);

    }

    @Test
    public void testGerarUpdate() throws IllegalArgumentException, IllegalAccessException {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSql.gerarUpdate(pessoa);

        Assert.assertEquals("UPDATE pessoa SET nome = ? WHERE id = 1", sqlGerado);
    }

    @Test
    public void testGerarDeleteAll() {
        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        String sqlGerado = geradorSql.deleteAll();

        Assert.assertEquals("DELETE FROM pessoa WHERE id > 0", sqlGerado);
    }
}
