package br.com.sharingan.orm;

import java.sql.Connection;

public interface ConexaoFactory {
	
	public Connection getConnection();

}
