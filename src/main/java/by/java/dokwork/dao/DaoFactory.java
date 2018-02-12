package by.java.dokwork.dao;

import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlArtistDao;
import by.java.dokwork.mysql.MySqlCategoryDao;
import by.java.dokwork.mysql.MySqlCommentDao;
import by.java.dokwork.mysql.MySqlRoleDao;
import by.java.dokwork.mysql.MySqlUserDao;

/** ������� �������� ��� ������ � ����� ������ */
public interface DaoFactory {

	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� Art
	 * 
	 * @throws PersistException
	 */
	public MySqlArtDao getMySqlArtDao()
			throws PersistException;

	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� Artist
	 * 
	 * @throws PersistException
	 */
	public MySqlArtistDao getMySqlArtistDao()
			throws PersistException;

	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� Comment
	 * 
	 * @throws PersistException
	 */
	public MySqlCommentDao getMySqlCommentDao()
			throws PersistException;
	
	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� Role
	 * 
	 * @throws PersistException
	 */
	public MySqlRoleDao getMySqlRoleDao()
			throws PersistException;
	
	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� Category
	 * 
	 * @throws PersistException
	 */
	public MySqlCategoryDao getMySqlCategoryDao()
			throws PersistException;
	
	/**
	 * ���������� ������ ��� ���������� ������������� ���������� ������� User
	 * 
	 * @throws PersistException
	 */
	public MySqlUserDao getMySqlUserDao()
			throws PersistException;
}
