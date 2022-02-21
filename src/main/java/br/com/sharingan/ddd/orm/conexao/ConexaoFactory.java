package br.com.sharingan.ddd.orm.conexao;

import java.sql.Connection;

public interface ConexaoFactory {
	
	public Connection getConnection();

}
