package br.com.sharingan.orm.repositorio;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import br.com.sharingan.ddd.orm.Repositorio;
import br.com.sharingan.ddd.orm.conexao.ConexaoFactory;
import br.com.sharingan.ddd.orm.entidade.Entidade;

public class RepositorioImpl<T extends Entidade> implements Repositorio<T> {

	private final Logger logger = Logger.getLogger(RepositorioImpl.class.getName());

	private final Connection conexao;

	private final String ERRO_AO_ACESSAR_O_OBJETO = "Erro ao acessar o objeto ";

	public RepositorioImpl(ConexaoFactory connection) {
		this.conexao = connection.getConnection();
	}

	@Override
	public List<T> findAll(Class<T> t) {
		logger.info("Executando a consulta findAll");
		String classeName = t.getSimpleName();
		String sql = "SELECT * FROM " + classeName;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		logger.info("SQL: \n\t" + sql);
		try {
			stmt = conexao.prepareStatement(sql);
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			logger.info("Erro ao criar o PreparedStatement: " + e.getLocalizedMessage());
		}
		List<T> lista = new ArrayList<>();
		if (Objects.nonNull(stmt) && Objects.nonNull(rs)) {
			lista.addAll(mapearEntidade(t, stmt, rs));
		}
		logger.info("finalizando a consulta findAll");
		return lista;
	}

	@Override
	public T findById(Class<T> t, Long id) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SecurityException {
		logger.info("Executando a consulta findById");
		String classeName = t.getSimpleName();
		String sql = "SELECT * FROM " + classeName + " WHERE id = ?";
		logger.info("SQL: \n\t" + sql);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			logger.info("Erro ao criar o PreparedStatement: " + e.getLocalizedMessage());
		}
		List<T> entidade = null;
		if (Objects.nonNull(stmt) && Objects.nonNull(rs)) {
			entidade = mapearEntidade(t, stmt, rs);
		} else {
			return null;
		}
		logger.info("finalizando a consulta findById");
		return entidade.isEmpty() ? null : entidade.get(0);
	}

	private List<T> mapearEntidade(Class<T> t, PreparedStatement stmt, ResultSet rs) {
		logger.info("Iniciando o mapeamento da entidade");
		List<T> lista = new ArrayList<>();
		try {
			lista = percorrerCamposSelect(t, rs);
		} catch (InstantiationException e) {
			logger.info("Erro ao criar o objeto: " + e.getLocalizedMessage());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | SQLException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.info("Erro ao fechar o ResultSet: " + e.getLocalizedMessage());
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.info("Erro ao fechar o PreparedStatement: " + e.getLocalizedMessage());
			}
		}
		logger.info("finalizando o mapeamento da entidade");
		return lista;
	}

	private List<T> percorrerCamposSelect(Class<T> t, ResultSet rs) throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		logger.info("Iniciando o percorrerCamposSelect");
		List<T> listaAux = new ArrayList<>();
		while (rs.next()) {
			T obj = t.getConstructor().newInstance();
			for (Field field : t.getDeclaredFields()) {
				field.setAccessible(true);
				logger.info("Iniciando o switch");
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
				logger.info("finalizando o switch");
			}
			listaAux.add(obj);
		}
		logger.info("finalizando o percorrerCamposSelect");
		return listaAux;
	}

	@Override
	public T create(Class<T> clazz, T t) {
		logger.info("Executando a consulta create");

		String nomeClasse = clazz.getSimpleName().toLowerCase();
		String sql = "INSERT INTO " + nomeClasse;
		String sqlDeclaredValues = " (";
		String values = ") VALUES (";
		String sqlFinal = "";
		try {
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
				Long id = null;
				while (rs.next()) {
					id = rs.getLong("id");
				}
				Field field = clazz.getDeclaredField("id");
				field.setAccessible(true);
				field.set(t, id);
				return t;

			} catch (SQLException | NoSuchFieldException | SecurityException e) {
				logger.info("Erro ao criar o PreparedStatement: " + e);
			} finally {
				closePreparedStatement(ps);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.info("Erro ao acessar o objeto: " + e.getLocalizedMessage());
		} finally {

		}
		return null;
	}

	private void closePreparedStatement(PreparedStatement ps) {
		try {
			ps.close();
		} catch (SQLException e) {
			logger.info("Falha ao fechar a conexão: " + e);
		}
	}

	@Override
	public Boolean deleteAll(Class<T> clazz) {

		String sqlDelete = "DELETE FROM " + clazz.getSimpleName().toLowerCase();
		String sqlWhere = "WHERE id > 0";
		String sqlFinal = sqlDelete + " " + sqlWhere;
		logger.info("SQL: \n\t" + sqlFinal);

		try (PreparedStatement ps = conexao.prepareStatement(sqlFinal);) {
			ps.executeQuery();
			return true;
		} catch (Exception e) {
			logger.info("Erro: " + e);
		}
		return false;

	}

	@Override
	public T update(Class<T> clazz, T t) throws IllegalArgumentException, IllegalAccessException {
		logger.info("Executando update na classe " + clazz.getSimpleName());
		String className = clazz.getSimpleName().toLowerCase();
		String sqlUpdate = "UPDATE " + className + " SET ";
		String sqlWhere = "WHERE id = " + t.getId();
		String sqlFInal = "";
		try {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (t.getId() == null) {
					logger.info("Id nulo, criando o objeto: " + className);
					return this.create(clazz, t);
				}
				if (field.get(t) != null && !"id".equals(field.getName())) {
					sqlUpdate += field.getName() + " = ?, ";
				}
			}
			sqlFInal = sqlUpdate.substring(0, sqlUpdate.length() - 2) + " " + sqlWhere;
			logger.info("SQLFINAL: " + sqlFInal);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
