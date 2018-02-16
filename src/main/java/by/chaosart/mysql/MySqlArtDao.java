package by.chaosart.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlArtDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectArtistId;
	private PreparedStatement statementSelectCategoryId;
	private PreparedStatement statementSelectName;
	private PreparedStatement statementSelectId;
	private PreparedStatement statementDelete;
	
	protected MySqlArtDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectArtistId = connection.prepareStatement(getSelectQuery()
					+ "WHERE ARTIST_ID = ?;");
			statementSelectCategoryId = connection.prepareStatement(getSelectQuery()
					+ "WHERE CATEGORY_ID = ?;");
			statementSelectName = connection.prepareStatement(getSelectQuery()
					+ "WHERE ART_NAME = ?;");
			statementSelectId = connection.prepareStatement(getSelectQuery()
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
			statementSelectArtistId.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementSelectCategoryId.close();
		} catch (Exception ex) {
			e = ex;
		}
		try {
			statementSelectId.close();
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
		return "SELECT ID, ART_NAME, IMAGE, ARTIST_ID, CATEGORY_ID, DATE_CREATE, ORIGINAL_URL FROM ART ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO ART (ART_NAME, IMAGE, ARTIST_ID, CATEGORY_ID, DATE_CREATE, ORIGINAL_URL) \n"
				+ "VALUES (?, ?, ?, ?, ?, ?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE ART \n"
				+ "SET ART_NAME = ?, IMAGE = ?, ARTIST_ID = ?, CATEGORY_ID = ?, DATE_CREATE = ?, ORIGINAL_URL = ? \n"
				+ "WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM ART WHERE id= ?;";
	}

	public Art create(Art art) throws PersistException {
		Art persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		try {
			prepareStatementForInsert(statementCreate, art);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectId.setInt(1, id);
			} 
			selectedById = statementSelectId.executeQuery();
			List<Art> list = parseResultSet(selectedById);
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
		
	public Art read(String key) throws PersistException {
		List<Art> list;
		ResultSet selectedById = null;
		try {
			statementSelectId.setString(1, key);
			selectedById = statementSelectId.executeQuery();
			list = parseResultSet(selectedById);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + key
					+ " not found.", e);
		} finally{
			try {
				if(selectedById != null){
				selectedById.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		if(list.isEmpty()){
			throw new PersistException("Record with PK = " + key
					+ " not found.");
		}
		return list.iterator().next();
	}

	public Art readByName(String key) throws PersistException {
		List<Art> list;
		ResultSet selectedByName = null;
		try {
			statementSelectName.setString(1, key);
			selectedByName = statementSelectName.executeQuery();
			list = parseResultSet(selectedByName);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + key
					+ " not found.", e);
		} finally{
			try {
				if(selectedByName != null){
				selectedByName.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		if(list.isEmpty()){
			return new Art();
		}
		return list.iterator().next();
	}
	
	public void update(Art art) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, art);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Art art) throws PersistException {
		try {
			statementDelete.setObject(1, art.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Art> getAll() throws PersistException {
		List<Art> list;
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
	
	public List<Art> getAll(String artistId) throws PersistException {
		List<Art> list;
		ResultSet selectedAll = null;
		try {
			statementSelectArtistId.setString(1, artistId);
			selectedAll = statementSelectArtistId.executeQuery();
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
	
	public List<Art> getAllOfCat(String categoryId) throws PersistException {
		List<Art> list;
		ResultSet selectedAll = null;
		try {
			statementSelectCategoryId.setString(1, categoryId);
			selectedAll = statementSelectCategoryId.executeQuery();
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
	
	protected List<Art> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<Art> result = new LinkedList<Art>();
		try {
			while (rs.next()) {
				Art art = new Art();
				art.setId(rs.getString("ID"));
				art.setName(rs.getString("ART_NAME"));
				art.setImage(rs.getString("IMAGE"));
				art.setArtistId(rs.getString("ARTIST_ID"));
				art.setCategoryId(rs.getString("CATEGORY_ID"));
				art.setDate(rs.getString("DATE_CREATE"));
				art.setOriginalUrl(rs.getString("ORIGINAL_URL"));
				result.add(art);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			Art art) throws PersistException {
		try {
			statement.setString(1, art.getName());
			statement.setString(2, art.getImage());
			statement.setString(3, art.getArtistId());
			statement.setString(4, art.getCategoryId());
			statement.setString(5, art.getDate());
			statement.setString(6, art.getOriginalUrl());
			statement.setString(7, art.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			Art art) throws PersistException {
		try {
			statement.setString(1, art.getName());
			statement.setString(2, art.getImage());
			statement.setString(3, art.getArtistId());
			statement.setString(4, art.getCategoryId());
			statement.setString(5, art.getDate());
			statement.setString(6, art.getOriginalUrl());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
