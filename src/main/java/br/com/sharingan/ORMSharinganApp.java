package br.com.sharingan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sharingan.orm.impl.ConexaoFactoryImpl;

public class ORMSharinganApp {

    public static final Logger LOGGER = LoggerFactory.getLogger(ORMSharinganApp.class);

    public static void main(String[] args) {
        ConexaoFactoryImpl conexaoFactory = new ConexaoFactoryImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            conexaoFactory.close();
            LOGGER.info("HikariCP connection pool closed.");
        }));

        LOGGER.info("ORMSharinganApp started. Connection pool is active.");

        try {
            Thread.sleep(5000); // Keep alive for 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOGGER.info("ORMSharinganApp finished.");
    }
}
