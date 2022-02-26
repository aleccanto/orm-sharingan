package br.com.sharingan.orm.sql;

import org.junit.Assert;
import org.junit.Test;

import br.com.sharingan.noorm.model.Pessoa;

public class GeradorSqlImplTest {
    @Test
    public void testGerarInsert() {

    }

    @Test
    public void testGerarSelect() {

    }

    @Test
    public void testGerarUpdate() throws IllegalArgumentException, IllegalAccessException {
        GeradorSqlImpl<Pessoa> geradorSqlImpl = new GeradorSqlImpl<>(Pessoa.class);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("nome da pessoa");

        String sqlGerado = geradorSqlImpl.gerarUpdate(pessoa);

        Assert.assertEquals("UPDATE pessoa SET nome = ? WHERE id = 1", sqlGerado);
    }
}
