package by.chaosart.mysql;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.*;

public class MySqlArtistDao {

	private Session session;
	
	public MySqlArtistDao(Session session) throws PersistException {
		this.session = session;
	}

	public void close() throws PersistException {
		try {
			session.close();
		} catch (Exception e) {
			throw new PersistException("Unable to close resourses. ", e);
		}	
	}
	
	public Artist create(Artist artist) throws PersistException {
		Artist persistInstance;
		try {
			Serializable generatedId = session.save(artist);
			persistInstance = session.get(Artist.class, generatedId);
		} catch (Exception e) {
			throw new PersistException("Unable to record new data to DB.", e);
		} 
		return persistInstance;
	}
		
	public Artist read(String id) throws PersistException {
		Artist persistInstance;
		try {
			persistInstance = session.get(Artist.class, id);
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + id
					+ " not found.", e);
		} 	
		return persistInstance;
	}
	
	public Artist readByName(String name) throws PersistException {
		Artist persistInstance = new Artist();
		try {
			Criteria criteria = session.createCriteria(Artist.class)
                    .add(Restrictions.eq("name", name));
			if(!criteria.list().isEmpty()){
				persistInstance = (Artist) criteria.list().listIterator().next();
			}	
		} catch (Exception e) {
			throw new PersistException("Record with PK = " + name
					+ " not found.", e);
		}
		return persistInstance;
	}

	public void update(Artist artist) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.update(artist);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to update record.", e);
		}
	}
	

	public void delete(Artist artist) throws PersistException {
		try {
			Transaction trans = session.beginTransaction();
			session.delete(artist);
			trans.commit();
		} catch (Exception e) {
			throw new PersistException("Unable to delete record.", e);
		}
	}


	public List<Artist> getAll() throws PersistException {
		List<Artist> list;
		try {
			list = session.createCriteria(Artist.class).list();
		} catch (Exception e) {
			throw new PersistException("Unable to read data from DB.", e);
		}
		return list;
	}
}
