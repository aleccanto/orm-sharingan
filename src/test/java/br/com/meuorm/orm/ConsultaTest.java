package br.com.meuorm.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.sharingan.ddd.orm.Consulta;
import br.com.sharingan.noorm.Model.Pessoa;
import br.com.sharingan.orm.ConsultaImpl;
import br.com.sharingan.orm.conexao.ConexaoFactory;

public class ConsultaTest {

    @Test
    public void testFindAll() throws SQLException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        ConexaoFactory connection = new ConexaoFactory();

        Consulta<Pessoa> consulta = new ConsultaImpl<>(connection);

        List<Pessoa> pessoas = consulta.findAll(Pessoa.class);

        pessoas.forEach(pessoa -> {
            System.out.println(pessoa.getNome());
            System.out.println(pessoa.getId());
        });

        Assert.assertEquals(ArrayList.class, pessoas.getClass());
        Assert.assertEquals(2, pessoas.size());
    }

    @Test
    public void testFindById() throws SQLException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        ConexaoFactory connection = new ConexaoFactory();

        Consulta<Pessoa> consulta = new ConsultaImpl<Pessoa>(connection);

        Pessoa pessoa = consulta.findById(Pessoa.class, 1L);

        Assert.assertEquals("pessoa", pessoa.getNome());
        Assert.assertEquals(Long.valueOf(1), pessoa.getId());
    }

}
