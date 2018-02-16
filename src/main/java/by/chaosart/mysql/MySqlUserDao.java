package by.chaosart.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlUserDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectID;
	private PreparedStatement statementSelectLogin;
	private PreparedStatement statementDelete;
	
	protected MySqlUserDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectID = connection.prepareStatement(getSelectQuery()
					+ "WHERE ID = ?;");
			statementSelectLogin = connection.prepareStatement(getSelectQuery()
					+ "WHERE LOGIN = ?;");
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
		return "SELECT ID, ROLE_ID, FIRST_NAME, SECOND_NAME, LOGIN, USER_PASSWORD FROM USERS ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO USERS (ROLE_ID, FIRST_NAME, SECOND_NAME, LOGIN, USER_PASSWORD) \n"
				+ "VALUES (?, ?, ?, ?, ?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE USERS \n"
				+ "SET ROLE_ID = ?, FIRST_NAME = ?, SECOND_NAME = ?, LOGIN = ?, USER_PASSWORD = ? \n"
				+ "WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM USERS WHERE id= ?;";
	}

	public User create(User user) throws PersistException {
		User persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		try {
			prepareStatementForInsert(statementCreate, user);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectID.setInt(1, id);
			} 
			selectedById = statementSelectID.executeQuery();
			List<User> list = parseResultSet(selectedById);
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
		
	public User read(String id) throws PersistException {
		List<User> list;
		ResultSet selectedById = null;
		try {
			statementSelectID.setString(1, id);
			selectedById = statementSelectID.executeQuery();
			list = parseResultSet(selectedById);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} finally{
			try {
				if(selectedById!=null){
					selectedById.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list.iterator().next();
	}
	
	public User readByLogin(String login) throws PersistException {
		List<User> list;
		ResultSet selectedByLogin = null;
		try {
			statementSelectLogin.setString(1, login);
			selectedByLogin = statementSelectLogin.executeQuery();
			list = parseResultSet(selectedByLogin);
		} catch (Exception e) {
			throw new PersistException("Record with login = " + login
					+ " not found.", e);
		} finally{
			try {
				if(selectedByLogin!=null){
				selectedByLogin.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		if(list.isEmpty()){
			User user = new User();
			return user;
		} else {
			return list.iterator().next();
		}		
	}

	public void update(User user) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, user);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(User user) throws PersistException {
		try {
			statementDelete.setObject(1, user.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<User> getAll() throws PersistException {
		List<User> list;
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
	
	protected List<User> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<User> result = new LinkedList<User>();
		try {
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("ID"));
				user.setRoleId(rs.getString("ROLE_ID"));
				user.setName(rs.getString("FIRST_NAME"));
				user.setSurname(rs.getString("SECOND_NAME"));
				user.setLogin(rs.getString("LOGIN"));
				user.setPassword(rs.getString("USER_PASSWORD"));
				result.add(user);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			User object) throws PersistException {
		try {
			statement.setString(1, object.getRoleId());
			statement.setString(2, object.getName());
			statement.setString(3, object.getSurname());
			statement.setString(4, object.getLogin());
			statement.setString(5, object.getPassword());
			statement.setString(6, object.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			User object) throws PersistException {
		try {
			statement.setString(1, object.getRoleId());
			statement.setString(2, object.getName());
			statement.setString(3, object.getSurname());
			statement.setString(4, object.getLogin());
			statement.setString(5, object.getPassword());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
