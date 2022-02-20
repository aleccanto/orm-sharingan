package br.com.meuorm.orm;

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

import br.com.meuorm.ddd.orm.Consulta;
import br.com.meuorm.orm.conexao.ConexaoFactory;

public class ConsultaImpl<T> implements Consulta<T> {

	private final Logger logger = Logger.getLogger(ConexaoFactory.class.getName());

	private final Connection conexao;

	private final String ERRO_AO_ACESSAR_O_OBJETO = "Erro ao acessar o objeto ";

	public ConsultaImpl(ConexaoFactory connection) {
		this.conexao = connection.getConnection();
	}

	public List<T> findAll(Class<T> t) {
		logger.info("Executando a consulta findAll");
		String classeName = t.getSimpleName();
		String sql = "SELECT * FROM " + classeName;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conexao.prepareStatement(sql);
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			logger.info("Erro ao criar o PreparedStatement: " + e.getLocalizedMessage());
		}
		List<T> lista = new ArrayList<T>();
		if (Objects.nonNull(stmt) && Objects.nonNull(rs)) {
			lista.addAll(mapearEntidade(t, stmt, rs));
		}
		logger.info("finalizando a consulta findAll");
		return lista;
	}

	public T findById(Class<T> t, Long id) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SecurityException {
		logger.info("Executando a consulta findById");
		String classeName = t.getSimpleName();
		String sql = "SELECT * FROM " + classeName + " WHERE id = ?";
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
		}
		logger.info("finalizando a consulta findById");
		return entidade.isEmpty() ? null : (T) t.getConstructors()[0].newInstance();
	}

	private List<T> mapearEntidade(Class<T> t, PreparedStatement stmt, ResultSet rs) {
		logger.info("Iniciando o mapeamento da entidade");
		List<T> lista = new ArrayList<T>();
		try {
			lista = percorrerCamposSelect(t, rs);
		} catch (InstantiationException e) {
			logger.info("Erro ao criar o objeto: " + e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} catch (SecurityException e) {
			logger.info(ERRO_AO_ACESSAR_O_OBJETO + e.getLocalizedMessage());
		} catch (SQLException e) {
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

	private List<T> percorrerCamposSelect(Class<T> t, ResultSet rs)
			throws SQLException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		logger.info("Iniciando o percorrerCamposSelect");
		List<T> listaAux = new ArrayList<T>();
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
}
