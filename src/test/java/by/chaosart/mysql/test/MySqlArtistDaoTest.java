package by.chaosart.mysql.test;

import static org.testng.Assert.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Art;
import by.chaosart.domain.Artist;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlDaoFactory;

public class MySqlArtistDaoTest {
	
	private static MySqlArtistDao artistDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		artistDao = factory.getMySqlArtistDao();
	}
	
	/* artistId = 1 - для чтения/изменения(изменить после) 
	 * artistName = "ArtistTest2" - для создания(удалить после)  
	 * artistName = "ArtistTest3" - для удаления(создать после)
	 * */
	
	public Artist createArtist(int artistId){
		/* Входные параметры */
		String[] name = {"ArtistTest1", "ArtistTest2", "ArtistTest3"};
		Artist expectedArtist = new Artist();
		expectedArtist.setName(name[artistId]);
		return expectedArtist;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Artist expectedArtist = createArtist(1);
		/* Проверяем метод create(Artist artist) */
		Artist actualArtist = artistDao.create(expectedArtist);	
		System.out.println(expectedArtist.getName()+actualArtist.getName());
		AssertJUnit.assertEquals(expectedArtist.getName(), actualArtist.getName());
	}
		
	@Test
	public void readTest() throws PersistException {	
		Artist expectedArtist = createArtist(0);
		/* Проверяем метод read(String id) */
		Artist actualArtist = artistDao.read("1");
		System.out.println(expectedArtist.getName()+actualArtist.getName());
		AssertJUnit.assertEquals(expectedArtist.getName(), actualArtist.getName());
	}

	@Test
	public void readByNameTest() throws PersistException {	
		Artist expectedArtist = createArtist(0);
		/* Проверяем метод readByName(String name) */
		Artist actualArtist = artistDao.readByName("ArtistTest1");
		System.out.println(expectedArtist.getName()+actualArtist.getName());
		AssertJUnit.assertEquals(expectedArtist.getName(), actualArtist.getName());
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(Artist artist) */
		Artist expectedArtist = artistDao.readByName("ArtistTest3");
		artistDao.delete(expectedArtist);
		assertNull(artistDao.readByName("ArtistTest3").getName());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		Artist expectedArtist = artistDao.read("1");
		expectedArtist.setName("ArtistTest2");
		/* Проверяем метод update(Artist artist) */	 
		artistDao.update(expectedArtist);
		Artist actualArtist = artistDao.read("1");
		AssertJUnit.assertEquals(expectedArtist.getName(), actualArtist.getName());		
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Artist> artistList = artistDao.getAll();
		System.out.println(artistList.size());
		Integer actualLengh = artistList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@AfterMethod
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!artistDao.read("1").getName().equals("ArtistTest1")) {
				Artist expectedArtist = artistDao.read("1");
				expectedArtist.setName("ArtistTest1");
				artistDao.update(expectedArtist);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(artistDao.readByName("ArtistTest2").getName()!=null) {
				Artist expectedArtist = artistDao.readByName("ArtistTest2"); 
				artistDao.delete(expectedArtist);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(artistDao.readByName("ArtistTest3").getName()==null) {
				Artist expectedArtist = createArtist(2); 
				artistDao.create(expectedArtist);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (artistDao != null) {
			artistDao.close();
		}
	}	
}	
