package br.com.sharingan.orm.sql;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import br.com.sharingan.ddd.orm.entidade.Entidade;
import br.com.sharingan.ddd.orm.sql.GeradorSql;

public class GeradorSqlImpl<T extends Entidade> implements GeradorSql<T> {

    private final Logger logger = Logger.getLogger(GeradorSqlImpl.class.getName());

    private final Class<T> clazz;

    public GeradorSqlImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String gerarSelectTodos() {
        String classeName = clazz.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + classeName;
        logger.info("SQL: \n\t" + sql);
        return sql;
    }

    @Override
    public String gerarSelectFindById(Long id) {
        String classeName = clazz.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + classeName + " WHERE id = ?";
        logger.info("SQL: \n\t" + sql);
        return sql;
    }

    @Override
    public String gerarInsert(T t) throws IllegalArgumentException, IllegalAccessException {
        String nomeClasse = clazz.getSimpleName().toLowerCase();
        String sql = "INSERT INTO " + nomeClasse;
        String sqlDeclaredValues = " (";
        String values = ") VALUES (";
        String sqlFinal = "";

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if ("id".equals(field.getName())) {
                logger.info("id ignorado");
                continue;
            }
            if (field.get(t) != null) {
                sqlDeclaredValues += field.getName().toLowerCase() + ", ";
                values += "?, ";
            }
        }
        sqlFinal = sql + sqlDeclaredValues.substring(0, sqlDeclaredValues.length() - 2)
                + values.substring(0, values.length() - 2) + ")";

        logger.info("SQL: \n\t" + sqlFinal);
        return sqlFinal;
    }

    @Override
    public String gerarUpdate(T t) throws IllegalArgumentException, IllegalAccessException {
        String className = clazz.getSimpleName().toLowerCase();
        String sqlUpdate = "UPDATE " + className + " SET ";
        String sqlWhere = "WHERE id = " + t.getId();
        String sqlFInal = "";
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (t.getId() == null) {
                logger.info("Id nulo, criando o objeto: " + className);
                return "";
            }
            if (field.get(t) != null && !"id".equals(field.getName())) {
                sqlUpdate += field.getName() + " = ?, ";
            }
        }
        sqlFInal = sqlUpdate.substring(0, sqlUpdate.length() - 2) + " " + sqlWhere;
        logger.info("SQLFINAL: " + sqlFInal);

        return sqlFInal;
    }

    public String deleteAll() {
        String sqlDelete = "DELETE FROM " + clazz.getSimpleName().toLowerCase();
        String sqlWhere = "WHERE id > 0";
        String sqlFinal = sqlDelete + " " + sqlWhere;
        logger.info("SQL: \n\t" + sqlFinal);
        return sqlFinal;
    }

    @Override
    public Class<T> classeUsada() {
        return clazz;
    }

}
