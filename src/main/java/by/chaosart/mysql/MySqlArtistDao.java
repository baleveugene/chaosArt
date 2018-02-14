package by.chaosart.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlArtistDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectID;
	private PreparedStatement statementSelectName;
	private PreparedStatement statementDelete;

	protected MySqlArtistDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectID = connection.prepareStatement(getSelectQuery()
					+ "WHERE ID = ?;");
			statementSelectName = connection.prepareStatement(getSelectQuery()
					+ "WHERE ARTIST_NAME = ?;");
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
		return "SELECT ID, ARTIST_NAME FROM ARTIST ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO ARTIST (ARTIST_NAME) \n"
				+ "VALUES (?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE ARTIST \n"
				+ "SET ARTIST_NAME = ? WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM ARTIST WHERE id= ?;";
	}

	public Artist create(Artist artist) throws PersistException {
		Artist persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		// ��������� ������
		try {
			prepareStatementForInsert(statementCreate, artist);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectID.setInt(1, id);
			} 
			selectedById = statementSelectID.executeQuery();
			List<Artist> list = parseResultSet(selectedById);
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
		
	public Artist read(String key) throws PersistException {
		List<Artist> list;
		ResultSet selectedByID = null;
		try {
			statementSelectID.setString(1, key);
			selectedByID = statementSelectID.executeQuery();
			list = parseResultSet(selectedByID);
		} catch (Exception e) {
			throw new PersistException("Record with name = " + key
					+ " not found.", e);
		} finally{
			try {
			selectedByID.close();
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list.iterator().next();
	}
	
	public Artist readByName(String key) throws PersistException {
		List<Artist> list;
		ResultSet selectedByName = null;
		try {
			statementSelectName.setString(1, key);
			selectedByName = statementSelectName.executeQuery();
			list = parseResultSet(selectedByName);
		} catch (Exception e) {
			throw new PersistException("Record with name = " + key
					+ " not found.", e);
		} finally{
			try {
			selectedByName.close();
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		if(list.isEmpty()){
			return new Artist();
		}
		return list.iterator().next();
	}

	public void update(Artist artist) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, artist);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Artist artist) throws PersistException {
		try {
			statementDelete.setObject(1, artist.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Artist> getAll() throws PersistException {
		List<Artist> list;
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
	
	protected List<Artist> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<Artist> result = new LinkedList<Artist>();
		try {
			while (rs.next()) {
				Artist artist = new Artist();
				artist.setId(String.valueOf(rs.getInt("ID")));
				artist.setName(rs.getString("ARTIST_NAME"));
				result.add(artist);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			Artist object) throws PersistException {
		try {
			statement.setString(1, object.getName());
			statement.setString(2, object.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			Artist object) throws PersistException {
		try {
			statement.setString(1, object.getName());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
