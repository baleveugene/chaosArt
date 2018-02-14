package by.chaosart.mysql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import by.chaosart.dao.*;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlRoleDao;
import by.chaosart.mysql.MySqlUserDao;

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

	
	public MySqlArtDao getMySqlArtDao() throws PersistException {
		return new MySqlArtDao(connection);
	}

	
	public MySqlArtistDao getMySqlArtistDao() throws PersistException {
		return new MySqlArtistDao(connection);
	}

	
	public MySqlCommentDao getMySqlCommentDao() throws PersistException {
		return new MySqlCommentDao(connection);
	}

	
	public MySqlRoleDao getMySqlRoleDao() throws PersistException {
		return new MySqlRoleDao(connection);
	}

	
	public MySqlCategoryDao getMySqlCategoryDao() throws PersistException {
		return new MySqlCategoryDao(connection);
	}


	public MySqlUserDao getMySqlUserDao() throws PersistException {
		return new MySqlUserDao(connection);
	}	
}
