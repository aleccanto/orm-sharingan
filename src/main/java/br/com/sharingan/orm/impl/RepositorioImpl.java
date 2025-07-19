package br.com.sharingan.orm.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;

import br.com.sharingan.ORMSharinganApp;
import br.com.sharingan.orm.ConexaoFactory;
import br.com.sharingan.orm.Entidade;
import br.com.sharingan.orm.GeradorSql;
import br.com.sharingan.orm.Repositorio;

public class RepositorioImpl<T extends Entidade> implements Repositorio<T> {

    private static final Logger LOGGER = ORMSharinganApp.LOGGER;

    private final Connection conexao;

    private final GeradorSql<T> geradorSql;

    private static final String ERRO_AO_ACESSAR_O_OBJETO = "Erro ao acessar o objeto ";

    private static final String ERRO_AO_CRIAR_O_PREPAREDSTATEMENT = "Erro ao criar o PreparedStatement: ";

    public RepositorioImpl(ConexaoFactory connection, GeradorSql<T> geradorSql) {
        this.conexao = connection.getConnection();
        this.geradorSql = geradorSql;
    }

    @Override
    public List<T> buscarTodos() {
        LOGGER.info("Executando a consulta buscarTodos");
        String sql = geradorSql.gerarSelectTodos();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            LOGGER.info(ERRO_AO_CRIAR_O_PREPAREDSTATEMENT + e.getLocalizedMessage());
        }
        List<T> lista = new ArrayList<>();
        if (Objects.nonNull(stmt) && Objects.nonNull(rs)) {
            lista.addAll(mapearEntidade(geradorSql.obterClasse(), stmt, rs));
        }
        LOGGER.info("finalizando a consulta buscarTodos");
        return lista;
    }

    @Override
    public T buscarPorId(Long id) {
        LOGGER.info("Executando a consulta buscarPorId");
        String sql = geradorSql.gerarSelectPorId(id);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            LOGGER.info(ERRO_AO_CRIAR_O_PREPAREDSTATEMENT + e.getLocalizedMessage());
        }
        List<T> entidade = new ArrayList<>();
        if (Objects.nonNull(stmt) && Objects.nonNull(rs)) {
            entidade = mapearEntidade(geradorSql.obterClasse(), stmt, rs);
        } else {
            return null;
        }
        LOGGER.info("finalizando a consulta buscarPorId");
        return entidade.isEmpty() ? null : entidade.get(0);
    }

    @Override
    public T criar(T t) {
        LOGGER.info("Executando a consulta criar");
        String sqlFinal = "";
        try {
            sqlFinal = geradorSql.gerarInsert(t);
            PreparedStatement ps = null;
            try {
                ps = conexao.prepareStatement(sqlFinal, PreparedStatement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Field field : t.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if ("id".equals(field.getName())) {
                        continue;
                    }
                    if (field.get(t) != null) {
                        ps.setObject(i, field.get(t));
                        i++;
                    }
                }

                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    Field field = geradorSql.obterClasse().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(t, id);
                }
                return t;

            } catch (SQLException | NoSuchFieldException | SecurityException e) {
                LOGGER.info(ERRO_AO_CRIAR_O_PREPAREDSTATEMENT + e);
            } finally {
                closePreparedStatement(ps);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.info("Erro ao acessar o objeto: " + e.getLocalizedMessage());
        } finally {

        }
        return null;
    }

    @Override
    public T atualizar(T t) {
        String sqlFInal = "";
        PreparedStatement ps = null;
        try {
            sqlFInal = geradorSql.gerarUpdate(t);
            if ("".equals(sqlFInal)) {
                return this.criar(t);
            } else {
                int i = 1;
                ps = conexao.prepareStatement(sqlFInal);
                for (Field field : t.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if ("id".equals(field.getName())) {
                        continue;
                    }
                    if (field.get(t) != null) {
                        ps.setObject(i, field.get(t));
                        i++;
                    }
                }
                ps.executeUpdate();
                return t;
            }
        } catch (Exception e) {
            LOGGER.info("Ocorreu um erro ao gerar o sql: " + e);
        } finally {
            closePreparedStatement(ps);
        }
        return null;
    }

    @Override
    public Boolean excluirTodos() {
        String sqlFinal = geradorSql.gerarDeleteTodos();
        LOGGER.info("SQL: \n\t" + sqlFinal);

        try (PreparedStatement ps = conexao.prepareStatement(sqlFinal);) {
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.info("Erro: " + e);
        }
        return false;
    }

    private void closePreparedStatement(PreparedStatement ps) {
        try {
            if (Objects.nonNull(ps)) {
                ps.close();
            }
        } catch (SQLException e) {
            LOGGER.info("Falha ao fechar a conexão: " + e);
        }
    }

    private List<T> mapearEntidade(Class<T> t, PreparedStatement stmt, ResultSet rs) {
        LOGGER.info("Iniciando o mapeamento da entidade");
        List<T> lista = new ArrayList<>();
        try {
            lista = percorrerCamposSelect(t, rs);
        } catch (InstantiationException e) {
            LOGGER.info("Erro ao criar o objeto: " + e.getLocalizedMessage());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | SQLException e) {
            LOGGER.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.info("Erro ao fechar o ResultSet: " + e.getLocalizedMessage());
            }
            try {
                stmt.close();
            } catch (SQLException e) {
                LOGGER.info("Erro ao fechar o PreparedStatement: " + e.getLocalizedMessage());
            }
        }
        LOGGER.info("finalizando o mapeamento da entidade");
        return lista;
    }

    private List<T> percorrerCamposSelect(Class<T> t, ResultSet rs) throws SQLException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        LOGGER.info("Iniciando o percorrerCamposSelect");
        List<T> listaAux = new ArrayList<>();
        while (rs.next()) {
            T obj = t.getConstructor().newInstance();
            for (Field field : t.getDeclaredFields()) {
                field.setAccessible(true);
                LOGGER.info("Iniciando o switch");
                switch (field.getType().getSimpleName()) {
                    case "String":
                        field.set(obj, rs.getString(field.getName()));
                        break;
                    case "Long":
                        field.set(obj, rs.getLong(field.getName()));
                        break;
                    case "Integer":
                        field.set(obj, rs.getInt(field.getName()));
                        break;
                    case "Double":
                        field.set(obj, rs.getDouble(field.getName()));
                        break;
                    case "Float":
                        field.set(obj, rs.getFloat(field.getName()));
                        break;
                    case "Boolean":
                        field.set(obj, rs.getBoolean(field.getName()));
                        break;
                    case "Date":
                        field.set(obj, rs.getDate(field.getName()));
                        break;
                    default:
                        break;
                }
                LOGGER.info("finalizando o switch");
            }
            listaAux.add(obj);
        }
        LOGGER.info("finalizando o percorrerCamposSelect");
        return listaAux;
    }

}
