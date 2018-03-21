package by.chaosart.mysql.test;

import static org.testng.Assert.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Category;
import by.chaosart.domain.Role;
import by.chaosart.mysql.MySqlDaoFactory;
import by.chaosart.mysql.MySqlRoleDao;

public class MySqlRoleDaoTest {
	
	private static MySqlRoleDao roleDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		roleDao = factory.getMySqlRoleDao();
	}
	
	/* roleId = 1 - для чтения/изменения(изменить после) 
	 * roleName = "RoleTest2" - для создания(удалить после)  
	 * roleName = "RoleTest3" - для удаления(создать после)
	 * */
	
	public Role createRole(int roleId){
		/* Входные параметры */
		String[] name = {"RoleTest1", "RoleTest2", "RoleTest3"};
		Role expectedRole = new Role();
		expectedRole.setName(name[roleId]);
		return expectedRole;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Role expectedRole = createRole(1);
		/* Проверяем метод create(Role Role) */
		Role actualRole = roleDao.create(expectedRole);	
		AssertJUnit.assertEquals(expectedRole.getName(), actualRole.getName());
	}
		
	@Test
	public void readTest() throws PersistException {	
		Role expectedRole = createRole(0);
		/* Проверяем метод read(String id) */
		Role actualRole = roleDao.read("1");
		AssertJUnit.assertEquals(expectedRole.getName(), actualRole.getName());
	}

	@Test
	public void readByNameTest() throws PersistException {	
		Role expectedRole = createRole(0);
		/* Проверяем метод readByName(String name) */
		Role actualRole = roleDao.readByName("RoleTest1");
		AssertJUnit.assertEquals(expectedRole.getName(), actualRole.getName());
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(Role Role) */
		Role expectedRole = roleDao.readByName("RoleTest3");
		roleDao.delete(expectedRole);
		assertNull(roleDao.readByName("RoleTest3").getName());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		Role expectedRole = roleDao.read("1");
		expectedRole.setName("RoleTest2");
		/* Проверяем метод update(Role Role) */	 
		roleDao.update(expectedRole);
		Role actualRole = roleDao.read("1");
		AssertJUnit.assertEquals(expectedRole.getName(), actualRole.getName());		
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Role> roleList = roleDao.getAll();
		Integer actualLengh = roleList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@AfterMethod
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!roleDao.read("1").getName().equals("RoleTest1")){
				Role expectedRole = roleDao.read("1");
				expectedRole.setName("RoleTest1");
				roleDao.update(expectedRole);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(roleDao.readByName("RoleTest2").getName()!=null){
				Role expectedRole = roleDao.readByName("RoleTest2"); 
				roleDao.delete(expectedRole);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(roleDao.readByName("RoleTest3").getName()==null){
				Role expectedRole = createRole(2); 
				roleDao.create(expectedRole);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (roleDao != null) {
			roleDao.close();
		}
	}	
}	
