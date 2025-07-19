package br.com.sharingan.domain.orm.conexao;

import java.sql.Connection;

public interface ConexaoFactory {
	
	public Connection getConnection();

}
