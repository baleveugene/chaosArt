package by.java.dokwork.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import by.java.dokwork.dao.DaoFactory;
import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlArtistDao;
import by.java.dokwork.mysql.MySqlCategoryDao;
import by.java.dokwork.mysql.MySqlCommentDao;
import by.java.dokwork.mysql.MySqlDaoFactory;
import by.java.dokwork.mysql.MySqlUserDao;

public class SessionListener implements HttpSessionListener {

	MySqlArtDao artDao;
	MySqlArtistDao artistDao;
	MySqlCategoryDao categoryDao;
	MySqlCommentDao commentDao;
	MySqlUserDao userDao;
	
	@Override
    public void sessionCreated(HttpSessionEvent ev) {
    	try {   		
			DaoFactory factory = new MySqlDaoFactory();
			artDao = factory.getMySqlArtDao();
			categoryDao = factory.getMySqlCategoryDao();			
			artistDao = factory.getMySqlArtistDao();
			commentDao = factory.getMySqlCommentDao();
			userDao = factory.getMySqlUserDao();		
			HttpSession session = ev.getSession();
			session.setAttribute("artDao", artDao);
			session.setAttribute("artistDao", artistDao);
			session.setAttribute("commentDao", commentDao);
			session.setAttribute("userDao", userDao);
			session.setAttribute("categoryDao", categoryDao); 
    	} catch (Exception e) {
    		ev.getSession().setAttribute("errorPage", e);    		
		}
    }

	@Override
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
