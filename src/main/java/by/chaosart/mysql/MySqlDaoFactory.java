package by.chaosart.mysql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import by.chaosart.dao.DaoFactory;
import by.chaosart.dao.PersistException;

public class MySqlDaoFactory implements DaoFactory{

	private Session session;

	public MySqlDaoFactory() throws PersistException {
		try {
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
			session = sessionFactory.openSession();
		} catch (Exception e) {
			throw new PersistException("Плохо дело", e);
		}
	}

	
	public MySqlArtDao getMySqlArtDao() throws PersistException {
		return new MySqlArtDao(session);
	}

	
	public MySqlArtistDao getMySqlArtistDao() throws PersistException {
		return new MySqlArtistDao(session);
	}

	
	public MySqlCommentDao getMySqlCommentDao() throws PersistException {
		return new MySqlCommentDao(session);
	}

	
	public MySqlRoleDao getMySqlRoleDao() throws PersistException {
		return new MySqlRoleDao(session);
	}

	
	public MySqlCategoryDao getMySqlCategoryDao() throws PersistException {
		return new MySqlCategoryDao(session);
	}


	public MySqlUserDao getMySqlUserDao() throws PersistException {
		return new MySqlUserDao(session);
	}	
}
