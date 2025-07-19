package br.com.sharingan.orm.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import br.com.sharingan.ORMSharinganApp;
import br.com.sharingan.orm.ConexaoFactory;

public class ConexaoFactoryImpl implements ConexaoFactory {

    private static final Logger LOGGER = ORMSharinganApp.LOGGER;
    private HikariDataSource dataSource;

    public ConexaoFactoryImpl(String url, String usuario, String senha) {
        setupDataSource(url, usuario, senha);
    }

    public ConexaoFactoryImpl() {
        LOGGER.info("Criando a classe ConexaoFactory");
        if ("h2".equalsIgnoreCase(System.getProperty("db.test"))) {
            LOGGER.info("Usando o banco de dados H2 em memória para testes.");
            setupDataSource("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        } else {
            String url = "jdbc:postgresql://localhost:5432/myorm";
            String usuario = "postgres";
            String senha = "root";
            setupDataSource(url, usuario, senha);
        }
        LOGGER.info("ConexaoFactory criada");
    }

    private void setupDataSource(String url, String usuario, String senha) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(usuario);
        config.setPassword(senha);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection obterConexao() {
        LOGGER.info("Obtendo conexao do pool");
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Erro ao obter conexao do pool: " + e.getLocalizedMessage());
            throw new RuntimeException("Erro ao obter conexao do pool", e);
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("Pool de conexoes fechado.");
        }
    }

    public Logger getLogger() {
        return LOGGER;
    }
}
