package by.java.dokwork.mysql.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import by.java.dokwork.dao.PersistException;
import by.java.dokwork.domain.Art;
import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlDaoFactoryTest;

public class MySqlArtDaoTest {
	
	MySqlArtDao artDao;
	
	public MySqlArtDaoTest() throws PersistException {
		MySqlDaoFactoryTest factory = new MySqlDaoFactoryTest();
		artDao = factory.getMySqlArtDao();
	}
	
	@Test
	public void createTest() throws PersistException {					
		/* Входные параметры */
		String artistId = "1";
		String categoryId = "1";
		String image = "1.jpg";
		String name = "Dog";
		String originalUrl = "http://dog.com";
		/* Устанавливаем входные параметры */
		Art expectedArt = new Art();
		expectedArt.setArtistId(artistId);
		expectedArt.setCategoryId(categoryId);
		expectedArt.setImage(image);
		expectedArt.setName(name);
		expectedArt.setOriginalUrl(originalUrl);
		String expectedArray[] = {artistId, categoryId, image, name, originalUrl};
		/* Проверяем метод create(Art art) */
		Art actualArt = artDao.create(expectedArt);	
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
		artDao.delete(actualArt);
	}
		
	@Test
	public void readTest() throws PersistException {	
		/* Входные параметры */
		String artistId = "1";
		String categoryId = "1";
		String image = "img/1.jpg";
		String name = "ArtTest";
		String originalUrl = "https://www.deviantart.com/art/Okami-433205381";	
		String expectedArray[] = {artistId, categoryId, image, name, originalUrl};
		/* Проверяем метод read(String id) */
		Art actualArt = artDao.read("1");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void readByNameTest() throws PersistException {	
		/* Входные параметры */
		String artistId = "1";
		String categoryId = "1";
		String image = "img/1.jpg";
		String name = "ArtTest";
		String originalUrl = "https://www.deviantart.com/art/Okami-433205381";	
		String expectedArray[] = {artistId, categoryId, image, name, originalUrl};
		/* Проверяем метод read(String id) */
		Art actualArt = artDao.readByName("ArtTest");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
	}
	
	@Test
	public void updateTest() throws PersistException {
		/* Проверяем метод update(Art art) */
		String artistId = "1";
		String categoryId = "1";
		String image = "2.jpg";
		String name = "Cat";
		String originalUrl = "http://cat.com";
		String[] expectedArray = {artistId, categoryId, image, name, originalUrl};
		Art actualArt = artDao.readByName("ArtTest"); 
		actualArt.setArtistId(artistId);
		actualArt.setCategoryId(categoryId);
		actualArt.setImage(image);
		actualArt.setName(name);
		actualArt.setOriginalUrl(originalUrl);
		artDao.update(actualArt);
		String[] actualArrayUpdate = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArrayUpdate);
		/*Возвращаем в исходное состояние тестовую запись в БД */
		actualArt.setArtistId("1");
		actualArt.setCategoryId("1");
		actualArt.setImage("img/1.jpg");
		actualArt.setName("ArtTest");
		actualArt.setOriginalUrl("https://www.deviantart.com/art/Okami-433205381");
		artDao.update(actualArt);	
	}
		
	@After
	public void close() {
		try {
			if (artDao != null) {
			artDao.close();
			}
		} catch (Exception e) {
		e.getLocalizedMessage();
		}	
	}	
}
