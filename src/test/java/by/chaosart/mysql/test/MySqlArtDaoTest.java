package by.chaosart.mysql.test;

import static org.testng.Assert.assertNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Art;
import by.chaosart.domain.Artist;
import by.chaosart.domain.Category;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlDaoFactory;

public class MySqlArtDaoTest {
	
	static MySqlArtDao artDao;
	static MySqlArtistDao artistDao;
	static MySqlCategoryDao categoryDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		artDao = factory.getMySqlArtDao();
		artistDao = factory.getMySqlArtistDao();
		categoryDao = factory.getMySqlCategoryDao();
	}
	
	/* artId = 1 - для чтения/изменения(изменить после) 
	 * artName = "ArtTest2" - для создания(удалить после)  
	 * artName = "ArtTest3" - для удаления(создать после)
	 * */
	
	public Art createArt(int artId) throws PersistException{
		/* Входные параметры */
		String[] artistId = {"1", "1", "1"};
		String[] categoryId = {"1", "1", "1"};
		String[] image = {"1.jpg", "2.jpg", "3.jpj"};
		String[] name = {"ArtTest1", "ArtTest2", "ArtTest3"};
		String[] originalUrl = {"https://ArtTest1.com", "https://ArtTest2.com", "https://ArtTest3.com"};
		Art expectedArt = new Art();
		expectedArt.setArtist(artistDao.read(artistId[artId]));
		expectedArt.setCategory(categoryDao.read(categoryId[artId]));
		expectedArt.setImage(image[artId]);
		expectedArt.setName(name[artId]);
		expectedArt.setOriginalUrl(originalUrl[artId]);
		return expectedArt;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Art expectedArt = createArt(1);
		String[] expectedArray = {expectedArt.getArtist().getId(), expectedArt.getCategory().getId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод create(Art art) */
		Art actualArt = artDao.create(expectedArt);	
		String[] actualArray = {actualArt.getArtist().getId(), actualArt.getCategory().getId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
	}
		
	@Test
	public void readTest() throws PersistException {	
		Art expectedArt = createArt(0);
		String[] expectedArray = {expectedArt.getArtist().getId(), expectedArt.getCategory().getId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод read(String id) */
		Art actualArt = artDao.read("1");
		String[] actualArray = {actualArt.getArtist().getId(), actualArt.getCategory().getId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void readByNameTest() throws PersistException {	
		Art expectedArt = createArt(0);
		String[] expectedArray = {expectedArt.getArtist().getId(), expectedArt.getCategory().getId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод readByName(String name) */
		Art actualArt = artDao.readByName("ArtTest1");
		String[] actualArray = {actualArt.getArtist().getId(), actualArt.getCategory().getId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
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
		Art expectedArt = artDao.read("1");
		expectedArt.setImage("2.jpg");
		expectedArt.setName("ArtTest2");
		String[] expectedArray = {expectedArt.getArtist().getId(), expectedArt.getCategory().getId(), 
				expectedArt.getImage(), expectedArt.getName(), expectedArt.getOriginalUrl()};
		/* Проверяем метод update(Art art) */	 
		artDao.update(expectedArt);
		Art actualArt = artDao.read("1");
		String[] actualArray = {actualArt.getArtist().getId(), actualArt.getCategory().getId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);		
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Art> artList = artDao.getAll();
		Integer actualLengh = artList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@Test
	public void getAllByArtistIdTest() throws PersistException {
		Integer expectedLengh = 2;
		Artist artist = artistDao.read("1");
		List<Art> artList = artDao.getAll(artist);
		Integer actualLengh = artList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@Test
	public void getAllOfCatTest() throws PersistException {
		Integer expectedLengh = 2;
		Category category = categoryDao.read("1");
		List<Art> artList = artDao.getAllOfCat(category);
		Integer actualLengh = artList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@AfterMethod
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!artDao.read("1").getName().equals("ArtTest1")){
				Art expectedArt = artDao.read("1");
				expectedArt.setImage("1.jpg");
				expectedArt.setName("ArtTest1");
				artDao.update(expectedArt);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(artDao.readByName("ArtTest2").getName()!=null){
				Art expectedArt = artDao.readByName("ArtTest2"); 
				artDao.delete(expectedArt);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(artDao.readByName("ArtTest3").getName()==null){
				Art expectedArt = createArt(2); 
				artDao.create(expectedArt);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (artDao != null) {
			artDao.close();
		}
	}	
}	
