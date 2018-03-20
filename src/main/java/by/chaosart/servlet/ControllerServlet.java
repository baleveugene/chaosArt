package by.chaosart.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import by.chaosart.domain.Art;
import by.chaosart.domain.Artist;
import by.chaosart.domain.Category;
import by.chaosart.domain.Comment;
import by.chaosart.domain.Role;
import by.chaosart.domain.User;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlRoleDao;
import by.chaosart.mysql.MySqlUserDao;

public class ControllerServlet extends HttpServlet {

	@Autowired
	private MySqlArtDao artDao;
	@Autowired
	private MySqlArtistDao artistDao;
	@Autowired
	private MySqlCategoryDao categoryDao;
	@Autowired
	private MySqlCommentDao commentDao;
	@Autowired
	private MySqlUserDao userDao;
	@Autowired
	private MySqlRoleDao roleDao;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
				config.getServletContext());
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processing(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processing(req, resp);
	}

	public void processing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		String controlParam = req.getParameter("controlParam");
		String url = null;
		try {
			/*
			 * Проверяем контрольный параметр запроса и обрабатываем его
			 * соответствующим методом
			 */
			if (controlParam == null) {
				url = mainPageProcess(req, resp);
			} else if (controlParam.equals("art")) {
				url = artPageProcess(req, resp);
			} else if (controlParam.equals("newAccount")) {
				url = regFormProcess(req, resp);
			} else if (controlParam.equals("logIn")) {
				url = loginFormProcess(req, resp);
			} else if (controlParam.equals("addCategory")) {
				url = addNewCategory(req, resp);
			} else if (controlParam.equals("addArt")) {
				url = addNewArt(req, resp);
			} else if (controlParam.equals("updateArt")) {
				url = updateArt(req, resp);
			} else if (controlParam.equals("deleteArt")) {
				url = deleteArt(req, resp);
			} else if (controlParam.equals("newComment")) {
				url = addComment(req, resp);
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
		requestDispatcher.forward(req, resp);
	}

	// Переход на главную страницу, исходя из роли пользователя
	public String mainPageProcess(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		try {
			List<Category> categoryList = categoryDao.getAll();
			req.getSession().setAttribute("categoryList", categoryList);
			List<Art> artList = artDao.getAll();
			req.getSession().setAttribute("artList", artList);
			if (req.getParameter("categoryId") != null) {
				String categoryId = req.getParameter("categoryId");
				String categoryName = categoryDao.read(categoryId).getName();
				req.getSession().setAttribute("categoryName", categoryName);
				artList = artDao.getAllOfCat(categoryDao.read(categoryId));
				req.getSession().setAttribute("artList", artList);
			}
			url = "jsp/main.jsp";
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Переход на страницу конкретного арта, исходя из роли пользователя
	public String artPageProcess(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		String artId = req.getParameter("artId");
		req.getSession().setAttribute("artId", artId);
		try {
			Art art = artDao.read(artId);
			req.getSession().setAttribute("art", art);
			Artist artist = art.getArtist();
			req.getSession().setAttribute("artist", artist);
			List<Comment> commentList = commentDao.getAll(art);
			req.getSession().setAttribute("commentList", commentList);
			List<User> userList = new ArrayList<User>();
			if (commentList != null) {
				for (Comment c : commentList) {
					User u = c.getUser();
					userList.add(u);
				}
			}
			req.getSession().setAttribute("userList", userList);
			List<Art> artList = artist.getArts();
			req.getSession().setAttribute("artList", artList);
			url = "jsp/art.jsp";
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Регистрации
	public String regFormProcess(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		try {
			if (req.getParameter("newAccount") != null
					&& req.getParameter("newAccount").equals("Регистрация")) {
				url = "jsp/registration.jsp";
			} else if (req.getParameter("newAccount") != null
					&& req.getParameter("newAccount").equals("Отмена")) {
				mainPageProcess(req, resp);
				url = "jsp/main.jsp";
			} else if (req.getParameter("newAccount") != null
					&& req.getParameter("newAccount").equals("Создать")) {
				String name = req.getParameter("name");
				String surname = req.getParameter("surname");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String password2 = req.getParameter("password2");
				/* Проверка валидности введенных данных */
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("Имя", name);
				paramMap.put("Логин", login);
				paramMap.put("Пароль", password);
				paramMap.put("Повторите пароль", password2);
				Validator validator = new Validator();				
				/* Так как поля не могут быть пустыми, то устанавливаем параметр
				 * true */				 
				Map<String, String> messageMap = validator
						.validate(paramMap,true);			
				/* Так как поле не является обязательным для заполнения, то
				 * устанавливаем параметр false */			 
				String message = validator.validate("Фамилия", surname, false);
				if (message != null) {
					messageMap.put("Фамилия", message);
				}
				/* Проверка совпадения паролей */
				if (!password.equals(password2)) {
					messageMap.put("Повторите пароль",
							"Пароли должны совпадать!");
				}
				/* Проверка уникальности логина */
				String hashCode = String.valueOf(password.hashCode());
				String adminPassword = "Admin";
				String adminHashCode = String.valueOf(adminPassword.hashCode());
				User user = userDao.readByLogin(login);
				if (user.getLogin() != null) {
					messageMap.put("Логин",
							"Пользователь с таким логином уже существует.");
				}		
				/* В случае наличия невалидных введенных данных пользователь
				 * возвращается на страницу регистрации с сообщениями о
				 * допущенных ошибках */			 
				if (!messageMap.isEmpty()) {
					req.setAttribute("messageMap", messageMap);
					url = "jsp/registration.jsp";
				} else {				
					/* В случае успешной валидации введенных данных
					 * устанавливаем параметры нового пользователя и создаем
					 * запись в БД */			 
					Role role = new Role();
					if (hashCode.equals(adminHashCode)) {
						role = roleDao.read("1");
						user.setRole(role);
					} else {
						role = roleDao.read("2");
						user.setRole(role);
					}
					user.setName(name);
					user.setSurname(surname);
					user.setLogin(login);
					user.setPassword(hashCode);
					user = userDao.create(user);
					// Записываем в сессию параметры пользователя
					HttpSession session = req.getSession();
					session.setAttribute("userId", user.getId());
					session.setAttribute("roleId", user.getRole().getId());
					mainPageProcess(req, resp);
					url = "jsp/main.jsp";
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Входа
	public String loginFormProcess(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		try {
			if (req.getParameter("logIn") != null
					&& req.getParameter("logIn").equals("Вход")) {
				url = "jsp/login.jsp";
			} else if (req.getParameter("logIn") != null
					&& req.getParameter("logIn").equals("Отмена")) {
				mainPageProcess(req, resp);
				url = "jsp/main.jsp";
			} else if (req.getParameter("logIn") != null
					&& req.getParameter("logIn").equals("Выйти")) {
				HttpSession session = req.getSession();
				session.removeAttribute("login");
				session.removeAttribute("password");
				session.removeAttribute("roleId");
				mainPageProcess(req, resp);
				url = "jsp/main.jsp";
			} else if (req.getParameter("logIn") != null
					&& req.getParameter("logIn").equals("Войти")) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String hashCode = String.valueOf(password.hashCode());
				Map<String, String> messageMap = new HashMap<String, String>();
				User user = userDao.readByLogin(login);
				if (user.getLogin() == null) {
					String message = "Проверьте правильность написания логина.";
					messageMap.put("messageLogin", message);
				}
				if (user.getPassword() == null
						|| !hashCode.equals(user.getPassword())) {
					String message = "Проверьте правильность написания пароля.";
					messageMap.put("messagePassword", message);
				}
				if (!messageMap.isEmpty()) {
					req.setAttribute("messageMap", messageMap);
					url = "jsp/login.jsp";
				} else {
					String userId = user.getId();
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword
							.hashCode());
					HttpSession session = req.getSession(true);
					session.setAttribute("userId", userId);
					if (hashCode.equals(adminHashCode)) {
						session.setAttribute("roleId", new String("1"));
					} else {
						session.setAttribute("roleId", new String("2"));
					}
					mainPageProcess(req, resp);
					url = "jsp/main.jsp";
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Добавления новой категории
	public String addNewCategory(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		try {
			if (req.getParameter("addCategory") != null
					&& req.getParameter("addCategory").equals(
							"Добавить категорию")) {
				url = "jsp/addCategory.jsp";
			} else if (req.getParameter("addCategory") != null
					&& req.getParameter("addCategory").equals("Отмена")) {
				mainPageProcess(req, resp);
				url = "jsp/main.jsp";
			} else if (req.getParameter("addCategory") != null
					&& req.getParameter("addCategory").equals("Создать")) {
				String categoryName = req.getParameter("category");
				// Валидация введенных данных
				Validator validator = new Validator();
				String message = validator.validate("Название категории",
						categoryName, true);
				if (message != null) {
					req.setAttribute("message", message);
					url = "jsp/addCategory.jsp";
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						message = "Данная категория уже существует.";
						req.setAttribute("message", message);
						url = "jsp/addCategory.jsp";
					} else {
						// устанавливаем параметры категории, записываем ее в БД
						category.setName(categoryName);
						categoryDao.create(category);
						mainPageProcess(req, resp);
						url = "jsp/main.jsp";
					}
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Добавления нового арта
	public String addNewArt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = null;
		try {
			if (req.getParameter("addArt") != null
					&& req.getParameter("addArt").equals("Добавить Арт")) {
				url = "jsp/addArt.jsp";
			} else if (req.getParameter("addArt") != null
					&& req.getParameter("addArt").equals("Отмена")) {
				mainPageProcess(req, resp);
				url = "jsp/main.jsp";
			} else if (req.getParameter("addArt") != null
					&& req.getParameter("addArt").equals("Создать")) {
				String artName = req.getParameter("artName");
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalURL = req.getParameter("originalURL");
				/* Проверка валидности введенных данных */
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("Название арта", artName);
				paramMap.put("Имя художника", artistName);
				paramMap.put("Название категории", categoryName);
				paramMap.put("Ссылка на оригинал", originalURL);
				System.out.println(artName);
				Validator validator = new Validator();
				/* Так как поля не могут быть пустыми, то устанавливаем параметр
				 * true */
				Map<String, String> messageMap = validator.validate(paramMap,
						true);
				/* Проверка на уникальность добавляемого арта */
				Art art = artDao.readByName(artName);
				if (art.getName() != null) {
					messageMap.put("Название арта",
							"Арт с таким названием уже существует.");
				}				
				/* В случае наличия невалидных введенных данных пользователь
				 * возвращается на страницу добавления арта с сообщениями о
				 * допущенных ошибках */			 
				if (!messageMap.isEmpty()) {
					req.setAttribute("messageMap", messageMap);
					url = "jsp/addArt.jsp";
				} else {		
					/* В случае успешной валидации введенных данных получаем и
					 * устанавливаем id автора арта, если такого автора еще нет,
					 * создаем его */				 
					Artist artist = artistDao.readByName(artistName);
					if (artist.getName() == null) {
						artist.setName(artistName);
						artist = artistDao.create(artist);
						art.setArtist(artist);
					} else {
						art.setArtist(artist);
					}
					// Получаем и устанавливаем id категории арта, если такой
					// категории еще нет, создаем ее
					Category cat = categoryDao.readByName(categoryName);
					if (cat.getName() == null) {
						cat.setName(categoryName);
						cat = categoryDao.create(cat);
						art.setCategory(cat);
					} else {
						art.setCategory(cat);
					}
					// Устанавливаем параметры арта, записываем его в БД
					art.setName(artName);
					art.setImage("img/content/" + artName);
					art.setOriginalUrl(originalURL);
					art = artDao.create(art);
					url = "/ControllerServlet?artId=" + art.getId()
							+ "&controlParam=art";
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Изменения арта
	public String updateArt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = null;
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			Art art = artDao.read(artId);
			Artist artist = art.getArtist();
			Category category = art.getCategory();
			if (req.getParameter("updateArt") != null
					&& req.getParameter("updateArt").equals("Изменить")) {
				req.getSession().setAttribute("art", art);
				req.getSession().setAttribute("artist", artist);
				req.getSession().setAttribute("category", category);
				url = "jsp/updateArt.jsp";
			} else if (req.getParameter("updateArt") != null
					&& req.getParameter("updateArt").equals("Отмена")) {
				url = "/ControllerServlet?artId=" + artId + "&controlParam=art";
			} else if (req.getParameter("updateArt") != null
					&& req.getParameter("updateArt").equals("Изменить Арт")) {
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("categoryName");
				String originalUrl = req.getParameter("originalURL");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("Имя художника", artistName);
				paramMap.put("Название категории", categoryName);
				paramMap.put("Ссылка на оригинал", originalUrl);
				Validator validator = new Validator();			
				/* Так как поля по умолчанию могут быть пустыми, то
				 * устанавливаем параметр false */				 
				Map<String, String> messageMap = validator.validate(paramMap,
						false);
				if (!messageMap.isEmpty()) {
					req.setAttribute("messageMap", messageMap);
					url = "jsp/updateArt.jsp";
				} else {
					boolean notEmpty = false;
					if (!artistName.isEmpty()) {
						notEmpty = true;
						/* Получаем и устанавливаем id автора арта, если такого
						 * автора еще нет, создаем его */
						artist = artistDao.readByName(artistName);
						if (artist.getName() == null) {
							artist.setName(artistName);
							artist = artistDao.create(artist);
							art.setArtist(artist);
						} else {
							art.setArtist(artist);
						}
					}
					if (!categoryName.isEmpty()) {
						notEmpty = true;					
						/* Получаем и устанавливаем id категории арта, если
						 * такой категории еще нет, создаем ее */					 
						Category cat = categoryDao.readByName(categoryName);
						if (cat.getName() == null) {
							cat.setName(categoryName);
							cat = categoryDao.create(cat);
							art.setCategory(cat);
						} else {
							art.setCategory(cat);
						}
					}
					if (!originalUrl.isEmpty()) {
						notEmpty = true;
						art.setOriginalUrl(originalUrl);
					}
					if (notEmpty) {
						artDao.update(art);
					}
					url = "/ControllerServlet?artId=" + artId
							+ "&controlParam=art";
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Удаления арта
	public String deleteArt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = null;
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			if (req.getParameter("deleteArt") != null
					&& req.getParameter("deleteArt").equals("Удалить")) {
				url = "jsp/deleteArt.jsp";
			} else if (req.getParameter("deleteArt") != null
					&& req.getParameter("deleteArt").equals("Удалить арт")) {
				String yes = req.getParameter("yes");
				if (yes != null) {
					Art art = artDao.read(artId);
					artDao.delete(art);
					req.getSession().removeAttribute("artId");
					req.removeAttribute("artId");
					req.removeAttribute("deleteArt");
					mainPageProcess(req, resp);
					url = "jsp/main.jsp";
				} else {
					url = "/ControllerServlet?artId=" + artId
							+ "&controlParam=art";
				}
			}
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	// Форма Добавления комментария
	public String addComment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = null;
		try {
			String userId = (String) req.getSession().getAttribute("userId");
			String comment = req.getParameter("comment");
			String artId = (String) req.getSession().getAttribute("artId");
			Comment com = new Comment();
			if (!comment.isEmpty()) {
				com.setText(comment);
				com.setUser(userDao.read(userId));
				com.setArt(artDao.read(artId));
				commentDao.create(com);
			}
			url = "/ControllerServlet?artId=" + artId + "&controlParam=art";
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}

	public String exceptionPageProcess(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String url = null;
		try {
			// получаем исключение из запроса
			Exception e = (Exception) req.getAttribute("errorPage"); 																	
			e.printStackTrace();
			// удаляем исключение в запроса
			req.removeAttribute("errorPage");  
			url = "jsp/errorPage.jsp";
		} catch (Exception e) {
			req.setAttribute("errorPage", e);
			url = exceptionPageProcess(req, resp);
		}
		return url;
	}
}
