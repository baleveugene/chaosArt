package by.java.dokwork.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import by.java.dokwork.dao.PersistException;
import by.java.dokwork.domain.*;

public class MySqlRoleDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectID;
	private PreparedStatement statementDelete;
	
	protected MySqlRoleDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectID = connection.prepareStatement(getSelectQuery()
					+ "WHERE ID = ?;");
			statementDelete = connection.prepareStatement(getDeleteQuery());
		} catch (Exception e) {
			throw new PersistException("Unable to create prepareStatement.", e);
		}
	}

	public void close() throws PersistException {
		Exception e = null;
		try {
			connection.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementCreate.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementUpdate.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementSelectAll.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementSelectID.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementDelete.close();
		} catch (Exception ex) {
			e = ex;
		}
		if (e != null) {
			throw new PersistException("Unable to close resourses. ", e);
		}
	}
	
	protected String getSelectQuery() {
		return "SELECT ID, ROLE_NAME FROM ChaosArt_DB.ROLE ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO ChaosArt_DB.ROLE (ROLE_NAME) \n"
				+ "VALUES (?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE ChaosArt_DB.ROLE \n"
				+ "SET ROLE_NAME = ? WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM ChaosArt_DB.ROLE WHERE id= ?;";
	}

	public Role create(Role role) throws PersistException {
		Role persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		// Добавляем запись
		try {
			prepareStatementForInsert(statementCreate, role);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectID.setInt(1, id);
			} 
			selectedById = statementSelectID.executeQuery();
			List<Role> list = parseResultSet(selectedById);
			persistInstance = list.iterator().next();
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} finally {
			Exception e = null; 
			try{
				if(generatedId != null){
				generatedId.close();
				}
			} catch (Exception ex){
				e = ex;
				}	
			try{
				if(selectedById != null){
				selectedById.close();
				}
			} catch (Exception ex){
				e = ex;
			}
			if (e != null) {
				throw new PersistException("Unable to close resourses. ", e);
			}
				}
		return persistInstance;
	}
		
	public Role read(String key) throws PersistException {
		List<Role> list;
		ResultSet selectedById = null;
		try {
			statementSelectID.setString(1, key);
			selectedById = statementSelectID.executeQuery();
			list = parseResultSet(selectedById);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + key
					+ " not found.", e);
		} finally{
			try {
			selectedById.close();
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list.iterator().next();
	}

	public void update(Role role) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, role);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Role role) throws PersistException {
		try {
			statementDelete.setObject(1, role.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Role> getAll() throws PersistException {
		List<Role> list;
		ResultSet selectedAll = null;
		try {
			selectedAll = statementSelectAll.executeQuery();
			list = parseResultSet(selectedAll);
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}finally{
			try {
				selectedAll.close();
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list;
	}
	
	protected List<Role> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<Role> result = new LinkedList<Role>();
		try {
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("ID"));
				role.setName(rs.getString("ROLE_NAME"));
				result.add(role);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			Role object) throws PersistException {
		try {
			statement.setString(1, object.getName());
			statement.setInt(2, object.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			Role object) throws PersistException {
		try {
			statement.setString(1, object.getName());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
