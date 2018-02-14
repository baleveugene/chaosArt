package by.chaosart.mysql.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Art;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlDaoFactoryTest;

public class MySqlArtDaoTest {
	
	static MySqlArtDao artDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactoryTest factory = new MySqlDaoFactoryTest();
		artDao = factory.getMySqlArtDao();
	}
	
	/* artId = 1 - для чтения/изменения(изменить после) 
	 * artName = "ArtTest2" - для создания(удалить после)  
	 * artName = "ArtTest3" - для удаления(создать после)
	 * */
	
	public Art createArt(int artId){
		/* Входные параметры */
		String[] artistId = {"1", "2", "3"};
		String[] categoryId = {"1", "2", "3"};
		String[] image = {"1.jpg", "2.jpg", "3.jpj"};
		String[] name = {"ArtTest1", "ArtTest2", "ArtTest3"};
		String[] originalUrl = {"https://www.deviantart.com", "https://www.dev.by", "https://www.d.ru"};
		Art expectedArt = new Art();
		expectedArt.setArtistId(artistId[artId]);
		expectedArt.setCategoryId(categoryId[artId]);
		expectedArt.setImage(image[artId]);
		expectedArt.setName(name[artId]);
		expectedArt.setOriginalUrl(originalUrl[artId]);
		return expectedArt;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Art expectedArt = createArt(1);
		String[] expectedArray = {expectedArt.getArtistId(), expectedArt.getCategoryId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод create(Art art) */
		Art actualArt = artDao.create(expectedArt);	
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
	}
		
	@Test
	public void readTest() throws PersistException {	
		Art expectedArt = createArt(0);
		String[] expectedArray = {expectedArt.getArtistId(), expectedArt.getCategoryId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод read(String id) */
		Art actualArt = artDao.read("1");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void readByNameTest() throws PersistException {	
		Art expectedArt = createArt(0);
		String[] expectedArray = {expectedArt.getArtistId(), expectedArt.getCategoryId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод readByName(String name) */
		Art actualArt = artDao.readByName("ArtTest1");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArray);
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(Art art) */
		Art expectedArt = artDao.readByName("ArtTest3");
		artDao.delete(expectedArt);
		assertNull(artDao.readByName("ArtTest3").getName());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		Art expectedArt = createArt(1);
		String[] expectedArray = {expectedArt.getArtistId(), expectedArt.getCategoryId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод update(Art art) */	 
		expectedArt.setId("1");
		artDao.update(expectedArt);
		Art actualArt = artDao.read("1");
		String[] actualArrayUpdate = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray, actualArrayUpdate);		
	}
	
	@After
	public void restore() throws PersistException {
		try {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!artDao.read("1").getName().equals("ArtTest1")){
				Art expectedArt = createArt(0); 
				expectedArt.setId("1");
				artDao.update(expectedArt);
			}
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(artDao.readByName("ArtTest3").getName()==null){
				Art expectedArt = createArt(2); 
				artDao.create(expectedArt);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(artDao.readByName("ArtTest2")!=null){
				Art expectedArt = artDao.readByName("ArtTest2"); 
				artDao.delete(expectedArt);
			}		
		} catch (Exception e) {
				e.getLocalizedMessage();
			} 
	}
	
	@AfterClass
		public static void close() throws PersistException{
		if (artDao != null) {
			artDao.close();
		}
	}	
}	
