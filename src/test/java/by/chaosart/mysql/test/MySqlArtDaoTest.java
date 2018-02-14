package by.chaosart.mysql.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Art;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlDaoFactoryTest;

public class MySqlArtDaoTest {
	
	MySqlArtDao artDao;
	String[] expectedArray0;
	String[] expectedArray1;
	String[] expectedArray2;
	ArrayList<Art> expectedArtList;
	
	@Before
    public void setUp() throws PersistException {
		MySqlDaoFactoryTest factory = new MySqlDaoFactoryTest();
		artDao = factory.getMySqlArtDao();
		System.out.println(5);
		/* Входные параметры */
		String[] artistId = {"1", "2", "3"};
		String[] categoryId = {"1", "2", "3"};
		String[] image = {"1.jpg", "2.jpg", "3.jpj"};
		String[] name = {"ArtTest1", "ArtTest2", "ArtTest3"};
		String[] originalUrl = {"https://www.deviantart.com", "https://www.dev.by", "https://www.d.ru"};
		/* Массивы входных параметров */
		expectedArray0 = new String[] {artistId[0], categoryId[0], 
				image[0], name[0], originalUrl[0]};
		expectedArray1 = new String[] {artistId[1], categoryId[1], 
				image[1], name[1], originalUrl[1]};
		expectedArray2 = new String[] {artistId[2], categoryId[2], 
				image[2], name[2], originalUrl[2]};
		/* Устанавливаем входные параметры */
		ArrayList<Art> expectedArtListLocal = new ArrayList<Art>();
		for(int i=0;i<3;i++){
			Art expectedArt = new Art();
			expectedArt.setArtistId(artistId[i]);
			expectedArt.setCategoryId(categoryId[i]);
			expectedArt.setImage(image[i]);
			expectedArt.setName(name[i]);
			expectedArt.setOriginalUrl(originalUrl[i]);
			expectedArtListLocal.add(expectedArt);
		}
		expectedArtList = expectedArtListLocal;
	}
	
	/* artId = 1 - для чтения/изменения(изменить после) 
	 * artName = "ArtTest2" - для создания(удалить после)  
	 * artName = "ArtTest3" - для удаления(создать после)
	 * */
	
	@Test
	public void createTest() throws PersistException {					
		/* Проверяем метод create(Art art) */
		Art actualArt = artDao.create(expectedArtList.get(1));	
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray1, actualArray);
	}
		
	@Test
	public void readTest() throws PersistException {	
		/* Проверяем метод read(String id) */
		Art actualArt = artDao.read("1");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray0, actualArray);
	}

	@Test
	public void readByNameTest() throws PersistException {	
		/* Проверяем метод readByName(String name) */
		Art actualArt = artDao.readByName("ArtTest1");
		String[] actualArray = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray0, actualArray);
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
		/* Проверяем метод update(Art art) */
		Art expectedArt = expectedArtList.get(1); 
		expectedArt.setId("1");
		artDao.update(expectedArt);
		Art actualArt = artDao.read("1");
		String[] actualArrayUpdate = {actualArt.getArtistId(), actualArt.getCategoryId(), 
				actualArt.getImage(), actualArt.getName(), actualArt.getOriginalUrl()};
		assertArrayEquals(expectedArray1, actualArrayUpdate);		
	}
	

		
	@After
	public void restore() throws PersistException {
		try {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!artDao.read("1").getName().equals("ArtTest1")){
				Art expectedArt = expectedArtList.get(0); 
				expectedArt.setId("1");
				artDao.update(expectedArt);
				System.out.println(1);
			}
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(artDao.readByName("ArtTest3").getName()==null){
				Art expectedArt = expectedArtList.get(2); 
				artDao.create(expectedArt);
				System.out.println(3);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(artDao.readByName("ArtTest2")!=null){
				Art expectedArt = artDao.readByName("ArtTest2"); 
				artDao.delete(expectedArt);
				System.out.println(2);
			}		
		} catch (Exception e) {
				e.getLocalizedMessage();
		} finally{
			if (artDao != null) {
				artDao.close();
				System.out.println(4);
				}
		}
	}	
}
