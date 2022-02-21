package br.com.sharingan.orm.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import br.com.sharingan.ddd.orm.conexao.ConexaoFactory;

public class ConexaoFactoryImpl implements ConexaoFactory {

	private final Logger logger = Logger.getLogger(ConexaoFactoryImpl.class.getName());

	private String url;

	private String usuario;

	private String senha;

	public ConexaoFactoryImpl(String url, String usuario, String senha) {
		this.url = url;
		this.usuario = usuario;
		this.senha = senha;
	}

	public ConexaoFactoryImpl() {
		logger.info("Criando a classe ConexaoFactory");
		this.url = "jdbc:postgresql://localhost:5432/myorm";
		this.usuario = "postgres";
		this.senha = "root";
		logger.info("ConexaoFactory criada");
	}

	public Connection getConnection() {
		logger.info("Criando a conexao");
		try {
			return DriverManager.getConnection(url, usuario, senha);
		} catch (SQLException e) {
			logger.info("Erro ao criar a conexao: " + e.getLocalizedMessage());
		}
		return null;
	}
}
