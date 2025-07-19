package br.com.sharingan.orm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sharingan.orm.impl.ConexaoFactoryImpl;
import br.com.sharingan.orm.impl.GeradorSqlImpl;
import br.com.sharingan.orm.impl.RepositorioImpl;
import br.com.sharingan.orm.model.Pessoa;

public class RepositorioTest {

    static {
        System.setProperty("db.test", "h2");
    }

    @Before
    public void setUp() throws SQLException, IOException {
        ConexaoFactory connection = new ConexaoFactoryImpl();
        try (Connection c = connection.getConnection()) {
            try (Statement statement = c.createStatement()) {
                statement.execute(getSql());
            }
        }

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        new RepositorioImpl<Pessoa>(connection, geradorSql).excluirTodos();
    }

    private String getSql() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("script.sql");
        StringBuilder stringBuilder = new StringBuilder();
        try (Reader reader = new InputStreamReader(inputStream)) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        }
        return stringBuilder.toString();
    }

    @Test
    public void testBuscarTodos() throws SQLException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        ConexaoFactory connection = new ConexaoFactoryImpl();

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Repositorio<Pessoa> repositorio = new RepositorioImpl<>(connection, geradorSql);

        Pessoa pessoa1 = new Pessoa();
        Pessoa pessoa2 = new Pessoa();

        pessoa1.setNome("Nome1");
        pessoa2.setNome("Nome2");

        repositorio.criar(pessoa1);
        repositorio.criar(pessoa2);

        List<Pessoa> pessoas = repositorio.buscarTodos();

        pessoas.forEach(pessoa -> {
            System.out.println(pessoa.getNome());
            System.out.println(pessoa.getId());
        });

        Assert.assertEquals(ArrayList.class, pessoas.getClass());
        Assert.assertEquals(2, pessoas.size());
    }

    @Test
    public void testBuscarPorId() throws SQLException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        ConexaoFactory connection = new ConexaoFactoryImpl();

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Repositorio<Pessoa> repositorio = new RepositorioImpl<>(connection, geradorSql);

        Pessoa pessoa = new Pessoa();

        pessoa.setNome("pessoa");

        repositorio.criar(pessoa);

        Pessoa pessoaSave = repositorio.buscarPorId(pessoa.getId());

        Assert.assertEquals("pessoa", pessoaSave.getNome());
        Assert.assertNotNull(pessoaSave.getId());
    }

    @Test
    public void testCriar() throws IllegalArgumentException, IllegalAccessException {

        ConexaoFactory connection = new ConexaoFactoryImpl();

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Pessoa2");

        Pessoa save = query.criar(pessoa);

        Assert.assertEquals("Pessoa2", save.getNome());
        Assert.assertNotNull(save.getId());
    }

    @Test
    public void testExcluirTodos() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
        ConexaoFactory connection = new ConexaoFactoryImpl();

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

        Boolean deletou = query.excluirTodos();

        List<Pessoa> pessoas = query.buscarTodos();

        Assert.assertNotNull(deletou);
        Assert.assertEquals(0, pessoas.size());
    }

    @Test
    public void testAtualizar() throws IllegalArgumentException, IllegalAccessException {
        ConexaoFactory connection = new ConexaoFactoryImpl();

        GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

        Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Pessoa2");

        Pessoa save = query.criar(pessoa);

        save.setNome("Outro nome");

        Pessoa update = query.atualizar(save);

        Assert.assertEquals("Outro nome", update.getNome());
        Assert.assertEquals(save.getId(), update.getId());

    }

}
