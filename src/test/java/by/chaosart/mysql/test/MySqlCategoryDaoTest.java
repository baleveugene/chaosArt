package by.chaosart.mysql.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Category;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlDaoFactory;

public class MySqlCategoryDaoTest {
	
	private static MySqlCategoryDao categoryDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		categoryDao = factory.getMySqlCategoryDao();
	}
	
	/* categoryId = 1 - для чтения/изменения(изменить после) 
	 * categoryName = "CategoryTest2" - для создания(удалить после)  
	 * categoryName = "CategoryTest3" - для удаления(создать после)
	 * */
	
	public Category createCategory(int categoryId){
		/* Входные параметры */
		String[] name = {"CategoryTest1", "CategoryTest2", "CategoryTest3"};
		Category expectedCategory = new Category();
		expectedCategory.setName(name[categoryId]);
		return expectedCategory;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Category expectedCategory = createCategory(1);
		/* Проверяем метод create(Category category) */
		Category actualCategory = categoryDao.create(expectedCategory);	
		assertEquals(expectedCategory.getName(), actualCategory.getName());
	}
		
	@Test
	public void readTest() throws PersistException {	
		Category expectedCategory = createCategory(0);
		/* Проверяем метод read(String id) */
		Category actualCategory = categoryDao.read("1");
		assertEquals(expectedCategory.getName(), actualCategory.getName());
	}

	@Test
	public void readByNameTest() throws PersistException {	
		Category expectedCategory = createCategory(0);
		/* Проверяем метод readByName(String name) */
		Category actualCategory = categoryDao.readByName("CategoryTest1");
		assertEquals(expectedCategory.getName(), actualCategory.getName());
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(Art art) */
		Category expectedCategory = categoryDao.readByName("CategoryTest3");
		categoryDao.delete(expectedCategory);
		assertNull(categoryDao.readByName("CategoryTest3").getName());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		Category expectedCategory = createCategory(1);
		/* Проверяем метод update(Category cat) */	 
		expectedCategory.setId("1");
		categoryDao.update(expectedCategory);
		Category actualCategory = categoryDao.read("1");
		assertEquals(expectedCategory.getName(), actualCategory.getName());		
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Category> categoryList = categoryDao.getAll();
		Integer actualLengh = categoryList.size();
		assertEquals(expectedLengh, actualLengh);
	}
	
	@After
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!categoryDao.read("1").getName().equals("CategoryTest1")){
				Category expectedCategory = createCategory(0); 
				expectedCategory.setId("1");
				categoryDao.update(expectedCategory);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(categoryDao.readByName("CategoryTest2").getName()!=null){
				Category expectedCategory = categoryDao.readByName("CategoryTest2"); 
				categoryDao.delete(expectedCategory);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(categoryDao.readByName("categoryTest3").getName()==null){
				Category expectedCategory = createCategory(2); 
				categoryDao.create(expectedCategory);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (categoryDao != null) {
			categoryDao.close();
		}
	}	
}	
