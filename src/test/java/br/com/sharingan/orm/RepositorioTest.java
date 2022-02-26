package br.com.sharingan.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sharingan.ddd.orm.Repositorio;
import br.com.sharingan.ddd.orm.conexao.ConexaoFactory;
import br.com.sharingan.ddd.orm.sql.GeradorSql;
import br.com.sharingan.noorm.model.Pessoa;
import br.com.sharingan.orm.conexao.ConexaoFactoryImpl;
import br.com.sharingan.orm.repositorio.RepositorioImpl;
import br.com.sharingan.orm.sql.GeradorSqlImpl;

public class RepositorioTest {

	@Before
	public void setUp() {
		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = null;

		new RepositorioImpl<Pessoa>(connection, geradorSql).deleteAll(Pessoa.class);
	}

	@Test
	public void testFindAll() throws SQLException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

		Repositorio<Pessoa> repositorio = new RepositorioImpl<>(connection, geradorSql);

		Pessoa pessoa1 = new Pessoa();
		Pessoa pessoa2 = new Pessoa();

		pessoa1.setNome("Nome1");
		pessoa2.setNome("Nome2");

		repositorio.create(Pessoa.class, pessoa1);
		repositorio.create(Pessoa.class, pessoa2);

		List<Pessoa> pessoas = repositorio.findAll(Pessoa.class);

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

		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

		Repositorio<Pessoa> repositorio = new RepositorioImpl<>(connection, geradorSql);

		Pessoa pessoa = new Pessoa();
		;
		pessoa.setNome("pessoa");

		repositorio.create(Pessoa.class, pessoa);

		Pessoa pessoaSave = repositorio.findById(Pessoa.class, pessoa.getId());

		Assert.assertEquals("pessoa", pessoaSave.getNome());
		Assert.assertNotNull(pessoaSave.getId());
	}

	@Test
	public void testCreate() throws IllegalArgumentException, IllegalAccessException {

		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

		Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Pessoa2");

		Pessoa save = query.create(Pessoa.class, pessoa);

		Assert.assertEquals("Pessoa2", save.getNome());
		Assert.assertNotNull(save.getId());
	}

	@Test
	public void deleteAllTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

		Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

		Boolean deletou = query.deleteAll(Pessoa.class);

		List<Pessoa> pessoas = query.findAll(Pessoa.class);

		Assert.assertNotNull(deletou);
		Assert.assertEquals(0, pessoas.size());
	}

	@Test
	public void updateTest() throws IllegalArgumentException, IllegalAccessException {
		ConexaoFactory connection = new ConexaoFactoryImpl();

		GeradorSql<Pessoa> geradorSql = new GeradorSqlImpl<>(Pessoa.class);

		Repositorio<Pessoa> query = new RepositorioImpl<>(connection, geradorSql);

		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Pessoa2");

		Pessoa save = query.create(Pessoa.class, pessoa);

		save.setNome("Outro nome");

		Pessoa update = query.update(Pessoa.class, save);

		Assert.assertEquals("Outro nome", update.getNome());
		Assert.assertEquals(save.getId(), update.getId());

	}

}
