package br.com.sharingan.orm.sql;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import br.com.sharingan.ddd.orm.entidade.Entidade;
import br.com.sharingan.ddd.orm.sql.GeradorSql;

public class GeradorSqlImpl<T extends Entidade> implements GeradorSql<T> {

    private final Logger logger = Logger.getLogger(GeradorSql.class.getName());

    private final Class<T> clazz;

    public GeradorSqlImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String gerarSelect(Class<T> clazz, T t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String gerarInsert(Class<T> clazz, T t) {
        // TODO Auto-generated method stub
        return null;
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

}
