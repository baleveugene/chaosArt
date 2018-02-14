package by.chaosart.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlCategoryDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectID;
	private PreparedStatement statementSelectName;
	private PreparedStatement statementDelete;
	
	protected MySqlCategoryDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectID = connection.prepareStatement(getSelectQuery()
					+ "WHERE ID = ?;");
			statementSelectName = connection.prepareStatement(getSelectQuery()
					+ "WHERE CATEGORY_NAME = ?;");
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
			statementSelectName.close();
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
		return "SELECT ID, CATEGORY_NAME FROM CATEGORY ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO CATEGORY (CATEGORY_NAME) \n"
				+ "VALUES (?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE CATEGORY \n"
				+ "SET CATEGORY_NAME = ? WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM CATEGORY WHERE id= ?;";
	}

	public Category create(Category category) throws PersistException {
		Category persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		// ��������� ������
		try {
			prepareStatementForInsert(statementCreate, category);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectID.setInt(1, id);
			} 
			selectedById = statementSelectID.executeQuery();
			List<Category> list = parseResultSet(selectedById);
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
		
	public Category read(String key) throws PersistException {
		List<Category> list;
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
				if(selectedById!=null){
				selectedById.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list.iterator().next();
	}
	
	public Category readByName(String categoryName) throws PersistException {
		List<Category> list;
		ResultSet selectedById = null;
		try {
			statementSelectName.setString(1, categoryName);
			selectedById = statementSelectName.executeQuery();
			list = parseResultSet(selectedById);
		} catch (Exception e) {
			throw new PersistException("Record with name = " + categoryName
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
		if(list.isEmpty()){
			return new Category();
		}
		return list.iterator().next();
	}

	public void update(Category category) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, category);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Category category) throws PersistException {
		try {
			statementDelete.setObject(1, category.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Category> getAll() throws PersistException {
		List<Category> list;
		ResultSet selectedAll = null;
		try {
			selectedAll = statementSelectAll.executeQuery();
			list = parseResultSet(selectedAll);
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}finally{
			try {
				if(selectedAll!=null){
				selectedAll.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list;
	}
	
	protected List<Category> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<Category> result = new LinkedList<Category>();
		try {
			while (rs.next()) {
				Category category = new Category();
				category.setId(rs.getString("ID"));
				category.setName(rs.getString("CATEGORY_NAME"));
				result.add(category);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			Category object) throws PersistException {
		try {
			statement.setString(1, object.getName());
			statement.setString(2, object.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			Category object) throws PersistException {
		try {
			statement.setString(1, object.getName());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
