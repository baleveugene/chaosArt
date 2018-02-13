package by.java.dokwork.mysql;

import static org.junit.Assert.*;
import java.sql.SQLException;

import org.junit.Test;

import by.java.dokwork.dao.PersistException;
import by.java.dokwork.domain.Art;

public class MySqlArtDaoTest {

	@Test
	public void test() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		MySqlArtDao artDao = factory.getMySqlArtDao();
		
		String artistId = "1";
		String categoryId = "1";
		String image = "1.jpg";
		String name = "Dog";
		String originalUrl = "http://dog.com";
		String expectedArray[] = {artistId, categoryId, image, name, originalUrl};
		
		Art expectedArt = new Art();
		expectedArt.setArtistId(artistId);
		expectedArt.setCategoryId(categoryId);
		expectedArt.setImage(image);
		expectedArt.setName(name);
		expectedArt.setOriginalUrl(originalUrl);
		/* Test for metod create(Art art) */
		Art actualArt = artDao.create(expectedArt);
		String actualArtId = actualArt.getId();
		String[] actualArrayCreate = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArrayCreate);
		
		/* Test for metod read(String id) */
		actualArt = artDao.read(actualArtId);
		String[] actualArrayRead = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArrayRead);
		
		/* Test for metod readByName(String name) */
		actualArt = artDao.readByName(actualArt.getName());
		String[] actualArrayReadByName = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArrayReadByName);
		
		/* Test for metod update(Art art) */
		artistId = "2";
		categoryId = "2";
		image = "2.jpg";
		name = "Cat";
		originalUrl = "http://cat.com";
		String[] expectedArray2 = {artistId, categoryId, image, name, originalUrl};
		actualArt.setArtistId(artistId);
		actualArt.setCategoryId(categoryId);
		actualArt.setImage(image);
		actualArt.setName(name);
		actualArt.setOriginalUrl(originalUrl);
		artDao.update(actualArt);
		String[] actualArrayUpdate = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray2, actualArrayUpdate);			
	}
}
