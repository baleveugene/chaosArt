package by.chaosart.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlCommentDao {

	private Connection connection;
	private PreparedStatement statementCreate;
	private PreparedStatement statementUpdate;
	private PreparedStatement statementSelectAll;
	private PreparedStatement statementSelectArtId;
	private PreparedStatement statementSelectId;
	private PreparedStatement statementDelete;
	
	protected MySqlCommentDao(Connection connection) throws PersistException {
		this.connection = connection;
		try {
			statementCreate = connection.prepareStatement(getCreateQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
			statementUpdate = connection.prepareStatement(getUpdateQuery());
			statementSelectAll = connection.prepareStatement(getSelectQuery());
			statementSelectArtId = connection.prepareStatement(getSelectQuery()
					+ "WHERE ART_ID = ?;");
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
			statementSelectArtId.close();
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
		return "SELECT ID, USER_ID, ART_ID, COMMENT_TEXT FROM COMMENTS ";
	}

	
	protected String getCreateQuery() {
		return "INSERT INTO COMMENTS (USER_ID, ART_ID, COMMENT_TEXT) \n"
				+ "VALUES (?, ?, ?);";
	}

	protected String getUpdateQuery() {
		return "UPDATE COMMENTS \n"
				+ "SET USER_ID = ?, ART_ID  = ?, COMMENT_TEXT = ? \n"
				+ "WHERE id = ?;";
	}

	protected String getDeleteQuery() {
		return "DELETE FROM COMMENTS WHERE id= ?;";
	}

	public Comment create(Comment comment) throws PersistException {
		Comment persistInstance;
		ResultSet generatedId = null;
		ResultSet selectedById = null;
		// ��������� ������
		try {
			prepareStatementForInsert(statementCreate, comment);
			statementCreate.executeUpdate();
			generatedId = statementCreate.getGeneratedKeys();
			if(generatedId.next()){
				int id = generatedId.getInt(1);
				statementSelectId.setInt(1, id);
			} 
			selectedById = statementSelectId.executeQuery();
			List<Comment> list = parseResultSet(selectedById);
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
		
	public Comment read(String key) throws PersistException {
		List<Comment> list;
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
			selectedById.close();
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list.iterator().next();
	}

	public void update(Comment comment) throws PersistException {
		try {
			prepareStatementForUpdate(statementUpdate, comment);
			int count = statementUpdate.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On update modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Comment comment) throws PersistException {
		try {
			statementDelete.setObject(1, comment.getId());
			int count = statementDelete.executeUpdate();
			if (count != 1) {
				throw new PersistException(
						"On delete modify more then 1 record: " + count);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Comment> getAll() throws PersistException {
		List<Comment> list;
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
	
	public List<Comment> getAll(String artId) throws PersistException {
		List<Comment> list;
		ResultSet selectedAll = null;
		try {
			statementSelectArtId.setString(1, artId);
			selectedAll = statementSelectArtId.executeQuery();
			list = parseResultSet(selectedAll);
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}finally{
			try {
				if(selectedAll != null){
				selectedAll.close();
				}
		} catch (Exception e){
			throw new PersistException("Unable to close resourses. ", e);
		}
		}
		return list;
	}
	
	protected List<Comment> parseResultSet(ResultSet rs)
			throws PersistException {
		LinkedList<Comment> result = new LinkedList<Comment>();
		try {
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setId(rs.getString("ID"));
				comment.setUserId(rs.getInt("USER_ID"));
				comment.setArtId(rs.getString("ART_ID"));
				comment.setText(rs.getString("COMMENT_TEXT"));
				result.add(comment);
			}
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
		return result;
	}

	protected void prepareStatementForUpdate(PreparedStatement statement,
			Comment object) throws PersistException {
		try {
			statement.setInt(1, object.getUserId());
			statement.setString(2, object.getArtId());
			statement.setString(3, object.getText());
			statement.setString(4, object.getId());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}

	protected void prepareStatementForInsert(PreparedStatement statement,
			Comment object) throws PersistException {
		try {
			statement.setInt(1, object.getUserId());
			statement.setString(2, object.getArtId());
			statement.setString(3, object.getText());
		} catch (Exception e) {
			throw new PersistException("Unable to set values to object", e);
		}
	}
}
