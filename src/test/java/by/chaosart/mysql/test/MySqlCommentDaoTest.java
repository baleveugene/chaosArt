package by.chaosart.mysql.test;

import static org.testng.Assert.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;

import java.util.List;

import by.chaosart.dao.PersistException;
import by.chaosart.domain.Comment;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlDaoFactory;

public class MySqlCommentDaoTest {
	
	private static MySqlCommentDao commentDao;
	
	@BeforeClass
    public static void setUp() throws PersistException {
		MySqlDaoFactory factory = new MySqlDaoFactory();
		commentDao = factory.getMySqlCommentDao();
	}
	
	/* commentId = 1 - для чтения/изменения(изменить после) 
	 * commentName = "CommentTest2" - для создания(удалить после)  
	 * commentName = "CommentTest3" - для удаления(создать после)
	 * */
	
	public Comment createComment(int commentId){
		/* Входные параметры */
		String[] userId = {"1", "1", "1"};
		String[] artId = {"1", "1", "1"};
		String[] text = {"CommentTest1", "CommentTest2", "CommentTest3"};
		Comment expectedComment = new Comment();
		expectedComment.setUserId(userId[commentId]);
		expectedComment.setArtId(artId[commentId]);
		expectedComment.setText(text[commentId]);
		return expectedComment;	
	}
	
	@Test
	public void createTest() throws PersistException {					
		Comment expectedComment = createComment(1);
		String[] expectedArray = {expectedComment.getUserId(), expectedComment.getArtId(), expectedComment.getText()};
		/* Проверяем метод create(Comment Comment) */
		Comment actualComment = commentDao.create(expectedComment);	
		String[] actualArray = {actualComment.getUserId(), actualComment.getArtId(), actualComment.getText()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
	}
		
	@Test
	public void readTest() throws PersistException {	
		Comment expectedComment = createComment(0);
		String[] expectedArray = {expectedComment.getUserId(), expectedComment.getArtId(), expectedComment.getText()};
		/* Проверяем метод read(String id) */
		Comment actualComment = commentDao.read("1");
		String[] actualArray = {actualComment.getUserId(), actualComment.getArtId(), actualComment.getText()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void readByTextTest() throws PersistException {	
		Comment expectedComment = createComment(0);
		String[] expectedArray = {expectedComment.getUserId(), expectedComment.getArtId(), expectedComment.getText()};
		/* Проверяем метод readByText(String name) */
		Comment actualComment = commentDao.readByText("CommentTest1");
		String[] actualArray = {actualComment.getUserId(), actualComment.getArtId(), actualComment.getText()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);
	}
	
	@Test
	public void deleteTest() throws PersistException {
		/* Проверяем метод delete(Comment Comment) */
		Comment expectedComment = commentDao.readByText("CommentTest3");
		commentDao.delete(expectedComment);
		assertNull(commentDao.readByText("CommentTest3").getText());		
	}
	
	@Test
	public void updateTest() throws PersistException {
		Comment expectedComment = createComment(1);
		String[] expectedArray = {expectedComment.getUserId(), expectedComment.getArtId(), expectedComment.getText()};
		/* Проверяем метод update(Comment Comment) */	 
		expectedComment.setId("1");
		commentDao.update(expectedComment);
		Comment actualComment = commentDao.read("1");
		String[] actualArray = {actualComment.getUserId(), actualComment.getArtId(), actualComment.getText()};
		AssertJUnit.assertArrayEquals(expectedArray, actualArray);		
	}
	
	@Test
	public void getAllTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Comment> commentList = commentDao.getAll();
		Integer actualLengh = commentList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@Test
	public void getAllByArtIdTest() throws PersistException {
		Integer expectedLengh = 2;
		List<Comment> commentList = commentDao.getAll("1");
		Integer actualLengh = commentList.size();
		AssertJUnit.assertEquals(expectedLengh, actualLengh);
	}
	
	@AfterMethod
	public void restore() throws PersistException {
			/* Возвращаем в исходное положение запись в БД с id=1, 
			 * в случае ее изменения в процессе тестирования метода update()*/
			if(!commentDao.read("1").getText().equals("CommentTest1")){
				Comment expectedComment = createComment(0); 
				expectedComment.setId("1");
				commentDao.update(expectedComment);
			}
			/* Возвращаем в исходное положение запись в БД (удаляем запись), 
			 * в случае ее создания в процессе тестирования метода create()*/
			if(commentDao.readByText("CommentTest2").getText()!=null){
				Comment expectedComment = commentDao.readByText("CommentTest2"); 
				commentDao.delete(expectedComment);
			}			
			/* Возвращаем в исходное положение запись в БД (создаем запись), 
			 * в случае ее удаления в процессе тестирования метода delete()*/
			if(commentDao.readByText("CommentTest3").getText()==null){
				Comment expectedComment = createComment(2); 
				commentDao.create(expectedComment);
			}			
		}
	
	@AfterClass
		public static void close() throws PersistException{
		if (commentDao != null) {
			commentDao.close();
		}
	}	
}	
