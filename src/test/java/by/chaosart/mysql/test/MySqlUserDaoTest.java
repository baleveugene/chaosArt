package by.chaosart.mysql.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.User;
import by.chaosart.mysql.MySqlUserDao;
import by.chaosart.mysql.MySqlDaoFactory;

public class MySqlUserDaoTest {
	
	private static MySqlUserDao userDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		userDao = factory.getMySqlUserDao();
	}
	
	/* userId = 1 - для чтения/изменения(изменить после) 
	 * userName = "UserTest2" - для создания(удалить после)  
	 * userName = "UserTest3" - для удаления(создать после)
	 * */
	
	public User createUser(int userId){
		/* Входные параметры */	
		String[] roleId = {"1", "1", "1"};
		String[] name = {"UserTest1", "UserTest2", "UserTest3"};
		String[] login = {"LoginTest1", "LoginTest2", "LoginTest3"};
		String[] password = {"PasswordTest1", "PasswordTest2", "PasswordTest3"};
		User expectedUser = new User();
		expectedUser.setRoleId(roleId[userId]);
		expectedUser.setName(name[userId]);
		expectedUser.setLogin(login[userId]);
		expectedUser.setPassword(password[userId]);
		return expectedUser;
	}
	
	@Test
	public void createTest() throws PersistException {					
		User expectedUser = createUser(1);
		String[] expectedArray = {expectedUser.getRoleId(), expectedUser.getName(), 
				expectedUser.getLogin(), expectedUser.getPassword()};
		/* Проверяем метод create(User User) */
		User actualUser = userDao.create(expectedUser);
		String[] actualArray = {actualUser.getRoleId(), actualUser.getName(), 
				actualUser.getLogin(), actualUser.getPassword()};
		assertArrayEquals(expectedArray, actualArray);
	}
		
	@Test
	public void readTest() throws PersistException {	
		User expectedUser = createUser(0);
		String[] expectedArray = {expectedUser.getRoleId(), expectedUser.getName(), 
				expectedUser.getLogin(), expectedUser.getPassword()};
		/* Проверяем метод read(String id) */
		User actualUser = userDao.read("1");
		String[] actualArray = {actualUser.getRoleId(), actualUser.getName(), 
				actualUser.getLogin(), actualUser.getPassword()};
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void readByLoginTest() throws PersistException {	
		User expectedUser = createUser(0);
		String[] expectedArray = {expectedUser.getRoleId(), expectedUser.getName(), 
				expectedUser.getLogin(), expectedUser.getPassword()};
		/* Проверяем метод readByName(String name) */
		User actualUser = userDao.readByLogin("LoginTest1");
		String[] actualArray = {actualUser.getRoleId(), actualUser.getName(), 
				actualUser.getLogin(), actualUser.getPassword()};
		assertArrayEquals(expectedArray, actualArray);
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(User User) */
		User expectedUser = userDao.readByLogin("LoginTest3");
		userDao.delete(expectedUser);
		assertNull(userDao.readByLogin("LoginTest3").getName());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		User expectedUser = createUser(1);
		String[] expectedArray = {expectedUser.getRoleId(), expectedUser.getName(), 
				expectedUser.getLogin(), expectedUser.getPassword()};
		/* Проверяем метод update(User User) */	 
		expectedUser.setId("1");
		userDao.update(expectedUser);
		User actualUser = userDao.read("1");
		String[] actualArray = {actualUser.getRoleId(), actualUser.getName(), 
				actualUser.getLogin(), actualUser.getPassword()};
		assertArrayEquals(expectedArray, actualArray);	
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<User> userList = userDao.getAll();
		Integer actualLengh = userList.size();
		assertEquals(expectedLengh, actualLengh);
	}
	
	@After
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!userDao.read("1").getName().equals("UserTest1")){
				User expectedUser = createUser(0); 
				expectedUser.setId("1");
				userDao.update(expectedUser);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(userDao.readByLogin("LoginTest2").getName()!=null){
				User expectedUser = userDao.readByLogin("LoginTest2"); 
				userDao.delete(expectedUser);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(userDao.readByLogin("LoginTest3").getName()==null){
				User expectedUser = createUser(2); 
				userDao.create(expectedUser);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (userDao != null) {
			userDao.close();
		}
	}	
}	
