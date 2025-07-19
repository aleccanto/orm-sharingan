package br.com.sharingan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sharingan.orm.impl.ConexaoFactoryImpl;

public class ORMSharinganApp {

    public static final Logger LOGGER = LoggerFactory.getLogger(ORMSharinganApp.class);

    public static void main(String[] args) {
        ConexaoFactoryImpl conexaoFactory = new ConexaoFactoryImpl();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            conexaoFactory.fechar();
            LOGGER.info("Pool de conexões HikariCP fechado.");
        }));

        LOGGER.info("ORMSharinganApp iniciado. Pool de conexões ativo.");

        try {
            Thread.sleep(5000); // Mantém ativo por 5 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOGGER.info("ORMSharinganApp finalizado.");
    }
}
