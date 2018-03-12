package by.chaosart.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlDaoFactory;
import by.chaosart.mysql.MySqlUserDao;

public class SessionListener implements HttpSessionListener {

	MySqlArtDao artDao;
	MySqlArtistDao artistDao;
	MySqlCategoryDao categoryDao;
	MySqlCommentDao commentDao;
	MySqlUserDao userDao;
	
	
    public void sessionCreated(HttpSessionEvent ev) {
    	try {   		
    		MySqlDaoFactory factory = new MySqlDaoFactory();
			artDao = factory.getMySqlArtDao();
			categoryDao = factory.getMySqlCategoryDao();			
			artistDao = factory.getMySqlArtistDao();
			commentDao = factory.getMySqlCommentDao();
			userDao = factory.getMySqlUserDao();		
    	} catch (Exception e) {
    		ev.getSession().setAttribute("errorPage", e);    		
		}
    }

	
    public void sessionDestroyed(HttpSessionEvent ev) {
    	Exception e = null;    	
		try {
			if (artDao != null) {
				artDao.close();
			}
		} catch (Exception ex) {
			e = ex;
		}
		try {
			if (artistDao != null) {
				artistDao.close();
			}
		} catch (Exception ex) {
			e = ex;
		}
		try {
			if (commentDao != null) {
				commentDao.close();
			}
		} catch (Exception ex) {
			e = ex;
		} 
		try {
			if (userDao != null) {
				userDao.close();
			}
		} catch (Exception ex) {
			e = ex;
		} 
		try {
			if (categoryDao != null) {
				categoryDao.close();
			}
		} catch (Exception ex) {
			e = ex;
		} 
		if (e != null) {
			ev.getSession().setAttribute("errorPage", e);
		}	
    }
}
