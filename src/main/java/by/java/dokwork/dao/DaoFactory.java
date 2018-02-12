package by.java.dokwork.dao;

import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlArtistDao;
import by.java.dokwork.mysql.MySqlCategoryDao;
import by.java.dokwork.mysql.MySqlCommentDao;
import by.java.dokwork.mysql.MySqlRoleDao;
import by.java.dokwork.mysql.MySqlUserDao;

/** Фабрика объектов для работы с базой данных */
public interface DaoFactory {

	/**
	 * Возвращает объект для управления персистентным состоянием объекта Art
	 * 
	 * @throws PersistException
	 */
	public MySqlArtDao getMySqlArtDao()
			throws PersistException;

	/**
	 * Возвращает объект для управления персистентным состоянием объекта Artist
	 * 
	 * @throws PersistException
	 */
	public MySqlArtistDao getMySqlArtistDao()
			throws PersistException;

	/**
	 * Возвращает объект для управления персистентным состоянием объекта Comment
	 * 
	 * @throws PersistException
	 */
	public MySqlCommentDao getMySqlCommentDao()
			throws PersistException;
	
	/**
	 * Возвращает объект для управления персистентным состоянием объекта Role
	 * 
	 * @throws PersistException
	 */
	public MySqlRoleDao getMySqlRoleDao()
			throws PersistException;
	
	/**
	 * Возвращает объект для управления персистентным состоянием объекта Category
	 * 
	 * @throws PersistException
	 */
	public MySqlCategoryDao getMySqlCategoryDao()
			throws PersistException;
	
	/**
	 * Возвращает объект для управления персистентным состоянием объекта User
	 * 
	 * @throws PersistException
	 */
	public MySqlUserDao getMySqlUserDao()
			throws PersistException;
}
