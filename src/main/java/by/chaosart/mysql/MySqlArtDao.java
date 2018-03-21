package by.chaosart.mysql;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlArtDao {

	private Session session;
	
	public MySqlArtDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}

	public Art create(Art art) throws PersistException {
		Art persistInstance;
		try {
			Transaction trans = session.beginTransaction();	
			Serializable generatedId = session.save(art);
			persistInstance = session.get(Art.class, generatedId);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public Art read(String id) throws PersistException {
		Art persistInstance;
		try {
			persistInstance = session.get(Art.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}

	public Art readByName(String name) throws PersistException {
		Art persistInstance = new Art();
		try {
			Criteria criteria = session.createCriteria(Art.class)
                    .add(Restrictions.eq("name", name));
			if(!criteria.list().isEmpty()){
				persistInstance = (Art) criteria.list().listIterator().next();
			}	
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + name
					+ " not found.", e);
		}
		return persistInstance;
	}
	
	public void update(Art art) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(art);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Art art) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();					
			art.getArtist().getArts().remove(art);	
			art.getCategory().getArts().remove(art);
			session.delete(art);				
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Art> getAll() throws PersistException {
		List<Art> list;
		try {
			list = session.createCriteria(Art.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
	
	public List<Art> getAll(Artist artist) throws PersistException {
		List<Art> list;
		try {
			Criteria criteria = session.createCriteria(Art.class)
                    .add(Restrictions.eq("artist", artist));
			list = criteria.list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
	
	public List<Art> getAllOfCat(Category category) throws PersistException {
		List<Art> list;
		try {
			Criteria criteria = session.createCriteria(Art.class)
                    .add(Restrictions.eq("category", category));
			list = criteria.list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
