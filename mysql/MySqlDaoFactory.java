package by.java.dokwork.mysql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import by.java.dokwork.dao.*;

public class MySqlDaoFactory implements DaoFactory{

	private Connection connection;
	private static final String PATH_TO_PROPERTIES = "config.properties";

	public MySqlDaoFactory() throws PersistException {

		
		InputStream fis = null;

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			fis = classLoader.getResourceAsStream(PATH_TO_PROPERTIES);
			Properties prop = new Properties();
			prop.load(fis);
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String url = prop.getProperty("url");
			String driver = prop.getProperty("driver");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			throw new PersistException("Fail " + PATH_TO_PROPERTIES
					+ " not found.", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				throw new PersistException(
						"Unable to close resource. ", e);
			}
		}
	}

	@Override
	public MySqlArtDao getMySqlArtDao() throws PersistException {
		return new MySqlArtDao(connection);
	}

	@Override
	public MySqlArtistDao getMySqlArtistDao() throws PersistException {
		return new MySqlArtistDao(connection);
	}

	@Override
	public MySqlCommentDao getMySqlCommentDao() throws PersistException {
		return new MySqlCommentDao(connection);
	}

	@Override
	public MySqlRoleDao getMySqlRoleDao() throws PersistException {
		return new MySqlRoleDao(connection);
	}

	@Override
	public MySqlCategoryDao getMySqlCategoryDao() throws PersistException {
		return new MySqlCategoryDao(connection);
	}

	@Override
	public MySqlUserDao getMySqlUserDao() throws PersistException {
		return new MySqlUserDao(connection);
	}	
}
