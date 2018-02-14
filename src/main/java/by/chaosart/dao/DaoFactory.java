package by.chaosart.dao;

import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlRoleDao;
import by.chaosart.mysql.MySqlUserDao;

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
