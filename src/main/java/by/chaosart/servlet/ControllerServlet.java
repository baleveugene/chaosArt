package by.chaosart.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import by.chaosart.dao.DaoFactory;
import by.chaosart.domain.Art;
import by.chaosart.domain.Artist;
import by.chaosart.domain.Category;
import by.chaosart.domain.Comment;
import by.chaosart.domain.User;
import by.chaosart.mysql.MySqlArtDao;
import by.chaosart.mysql.MySqlArtistDao;
import by.chaosart.mysql.MySqlCategoryDao;
import by.chaosart.mysql.MySqlCommentDao;
import by.chaosart.mysql.MySqlDaoFactory;
import by.chaosart.mysql.MySqlUserDao;

public class ControllerServlet extends HttpServlet {

	@Autowired	
	MySqlArtDao artDao;
	@Autowired
	MySqlArtistDao artistDao;
	@Autowired
	MySqlCategoryDao categoryDao;
	@Autowired
	MySqlCommentDao commentDao;
	@Autowired
	MySqlUserDao userDao;
	
	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	      config.getServletContext());
	  }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void processing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		Enumeration<String> en = req.getParameterNames();
		try {
			if (req.getSession().getAttribute("errorPage") != null) {
				exceptionPageProcessing(req, resp);
				// Главная страница
			} else if (!en.hasMoreElements() || req.getParameter("Chaos") != null) {
				mainPageProcessing(req, resp);
				// Страница конкретного арта
			} else if (req.getParameter("artId") != null) {
				artPageProcessing(req, resp);
				// Главная страница с артами конкретной категории
			} else if (req.getParameter("categoryId") != null) {
				mainPageProcessing(req, resp);
				// Форма Регистрации
			} else if (req.getParameter("newAccount") != null) {
				regFormProcessing(req, resp);
				// Форма Login
			} else if (req.getParameter("logIn") != null) {
				loginFormProcessing(req, resp);
				// Обработка формы добавления новой категории
			} else if (req.getParameter("addCategory") != null) {
				addNewCategory(req, resp);
				// Обработка формы добавления нового арта
			} else if (req.getParameter("addArt") != null) {
				addNewArt(req, resp);
				// Обработка формы изменения арта
			} else if (req.getParameter("updateArt") != null) {
				updateArt(req, resp);
				// Обработка формы удаления арта
			} else if (req.getParameter("deleteArt") != null) {
				deleteArt(req, resp);
				// Обработка формы добавления нового комментария
			} else if (req.getParameter("newComment") != null) {
				addComment(req, resp);
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			exceptionPageProcessing(req, resp); // страница исключений
		}
	}

	// Переход на главную страницу, исходя из роли пользователя
	public void mainPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			List<Category> categoryList = categoryDao.getAll();
			req.getSession().setAttribute("categoryList", categoryList);			
			List<Art> artList = artDao.getAll();
			req.getSession().setAttribute("artList", artList);
			if (req.getParameter("categoryId") != null) {
				String categoryId = req.getParameter("categoryId");
				String categoryName = categoryDao.read(categoryId).getName();
				req.getSession().setAttribute("categoryName", categoryName);
				artList = artDao.getAllOfCat(categoryId);
				req.getSession().setAttribute("artList", artList);
			}
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("main.jsp");
			requestDispatcher.forward(req, resp);
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Переход на страницу конкретного арта, исходя из роли пользователя
	public void artPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String artId = req.getParameter("artId");
		req.getSession().setAttribute("artId", artId);
		try {
			Art art = artDao.read(artId);
			req.getSession().setAttribute("art", art);
			String artistId = art.getArtistId();
			Artist artist = artistDao.read(artistId);
			req.getSession().setAttribute("artist", artist);
			List<Comment> commentList = commentDao.getAll(artId);
			req.getSession().setAttribute("commentList", commentList);
			List<User> userList = new ArrayList<User>();
			for (Comment c : commentList) {
				User u = userDao.read(c.getUserId());
				userList.add(u);			
			}
			req.getSession().setAttribute("userList", userList);
			List<Art> artList = artDao.getAll(artistId);
			req.getSession().setAttribute("artList", artList);
			// Переходим на страницу art.jsp
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("art.jsp");
			requestDispatcher.forward(req, resp);
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Регистрации
	public void regFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (req.getParameter("newAccount").equals("Регистрация")) {
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
				requestDispatcher.forward(req, resp);	
			} else if (req.getParameter("newAccount").equals("Отмена")) {
				resp.sendRedirect("/Chaos/ControllerServlet");		
			} else if (req.getParameter("newAccount").equals("Создать")) {
				String name = req.getParameter("name");
				String surname = req.getParameter("surname");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String password2 = req.getParameter("password2");
				// Проверка валидности введенных данных
				if (name.isEmpty() || login.isEmpty() || password.isEmpty() || password2.isEmpty()) {
					String message = "Необходимо заполнить все обязательные поля";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!name.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					String message = "Проверьте правильность заполнения полей Имя и Фамилия."
							+ "Введенные параметры должны состоять из букв латинского или русского алфавита,"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);					
				} else if (login.matches("^[*№;%:#&\'\"!)(\\.,]+") || password.matches("^[*№;%:#&\'\"!)(\\.,]+")) {
					String message = "Проверьте правильность заполнения полей Логин и Пароль."
							+ "(поля не должны содержать символов *, №, ?, %, ;, |, /, \\, (, ), &, !)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (!password.equals(password2)) {
					String message = "Пароли должны совпадать!";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					String hashCode = String.valueOf(password.hashCode());
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					User user = userDao.readByLogin(login);
					if (user.getLogin() != null) {
						String message = "Пользователь с таким логином уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
						requestDispatcher.forward(req, resp);					
					} else {
						// устанавливаем параметры нового пользователя и создаем запись в БД
						if (hashCode.equals(adminHashCode)) {
							user.setRoleId("1");
						} else {
							user.setRoleId("2");
						}
						user.setName(name);
						user.setSurname(surname);
						user.setLogin(login);
						user.setPassword(hashCode);
						user = userDao.create(user);
						// Создаем сессию и записываем в нее параметры пользователя
						HttpSession session = req.getSession();
						session.setAttribute("userId", user.getId());
						session.setAttribute("roleId", user.getRoleId());
						// переходим на главную страницу (mainAdmin или mainUser в зависимости от роли)
						resp.sendRedirect("/Chaos/ControllerServlet");
					}
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Входа
	public void loginFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (req.getParameter("logIn").equals("Вход")) {
				resp.sendRedirect("login.jsp");
			} else if (req.getParameter("logIn").equals("Отмена")) {
				resp.sendRedirect("/Chaos/ControllerServlet");			
			} else if (req.getParameter("logIn").equals("Выйти")) {
				HttpSession session = req.getSession();
				session.removeAttribute("login");
				session.removeAttribute("password");
				session.removeAttribute("roleId");
				// переходим на главную страницу (mainWithOutReg)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("logIn").equals("Войти")) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String hashCode = String.valueOf(password.hashCode());
				User user = userDao.readByLogin(login);
				if (user.getLogin() == null) {
					String message = "Проверьте правильность написания логина.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("login.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (user.getPassword() != null && !hashCode.equals(user.getPassword())) {
					String message = "Проверьте правильность написания пароля.";				
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("login.jsp");
					requestDispatcher.forward(req, resp);				
				} else {
					String userId = user.getId();
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					HttpSession session = req.getSession(true);
					session.setAttribute("userId", userId);
					if (hashCode.equals(adminHashCode)) {
						session.setAttribute("roleId", new String("1"));
						// переходим на главную страницу (mainAdmin)
						resp.sendRedirect("/Chaos/ControllerServlet");
					} else {
						session.setAttribute("roleId", new String("2"));
						// переходим на главную страницу (mainUser)
						resp.sendRedirect("/Chaos/ControllerServlet");
					}
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Добавления новой категории
	public void addNewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {		
			if (req.getParameter("addCategory").equals("Добавить категорию")) {
				// addCategory
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("addCategory").equals("Отмена")) {
				// переходим на главную страницу (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("addCategory").equals("Создать")) {
				String categoryName = req.getParameter("category");
				// Валидация введенных данных
				if (categoryName.isEmpty()) {
					String message = "Название категории не может быть пустым.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {				
					String message = "Проверьте правильность заполнения поля Название категории."+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						String message = "Данная категория уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
						requestDispatcher.forward(req, resp);
					} else {
						// устанавливаем параметры категории, записываем ее в БД
						category.setName(categoryName);
						categoryDao.create(category);
						// переходим на главную страницу (mainAdmin)
						resp.sendRedirect("/Chaos/ControllerServlet");
					}
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Добавления нового арта
	public void addNewArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (req.getParameter("addArt").equals("Добавить Арт")) {
				// addArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("addArt").equals("Отмена")) {
				// переходим на главную страницу (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("addArt").equals("Создать")) {
				String artName = req.getParameter("artName");
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalURL = req.getParameter("originalURL");
				// Валидация пользовательского ввода
				if (artName.isEmpty() || artistName.isEmpty() || categoryName.isEmpty() || originalURL.isEmpty()) {				
					String message = "Все поля обязательны к заполнению.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!artName.matches(".+\\.(jpg|png|jpeg|bmp|tif|gif)")) {
					String message = "Проверьте правильность заполнения поля Название арта.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					String message = "Проверьте правильность заполнения поля Название категории."+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!originalURL.matches("^(?i)http://.*$")) {				
					String message = "Проверьте правильность заполнения поля Ссылка на оригинал.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Art art = artDao.readByName(artName);
					if (art.getName() != null) {										
						String message = "Арт с таким названием уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
						requestDispatcher.forward(req, resp);
					} else {
						// Получаем и устанавливаем id автора арта, если такого автора еще нет, создаем его	
						Artist artist = artistDao.readByName(artistName);
						if (artist.getName() == null) {
							artist.setName(artistName);
							artist = artistDao.create(artist);
							art.setArtistId(artist.getId());
						} else {
							art.setArtistId(artist.getId());
						}
						// Получаем и устанавливаем id категории арта, если такой категории еще нет, создаем ее
						Category cat = categoryDao.readByName(categoryName);
						if (cat.getName() == null) {
							cat.setName(categoryName);
							cat = categoryDao.create(cat);
							art.setCategoryId(cat.getId());
						} else {
							art.setCategoryId(cat.getId());
						}
						// Устанавливаем параметры арта, записываем его в БД
						art.setName(artName);
						art.setImage("img/content/" + artName);
						art.setOriginalUrl(originalURL);
						art = artDao.create(art);
						// переходим на страницу созданного арта
						RequestDispatcher requestDispatcher = req
								.getRequestDispatcher("/ControllerServlet?artId=" + art.getId());
						requestDispatcher.forward(req, resp);
					}
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Изменения арта
	public void updateArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			Art art = artDao.read(artId);
			Artist artist = artistDao.read(art.getArtistId());
			Category category = categoryDao.read(art.getCategoryId());
			req.getSession().setAttribute("art", art);
			req.getSession().setAttribute("artist", artist);
			req.getSession().setAttribute("category", category);
			if (req.getParameter("updateArt").equals("Изменить")) {			
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("updateArt").equals("Отмена")) {
				// возвращаемся на страницу арта (artAdmin)
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("updateArt").equals("Изменить Арт")) {
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalUrl = req.getParameter("originalURL");
				
				if (!artistName.isEmpty()) {
					// Получаем и устанавливаем id автора арта, если такого автора еще нет, создаем его
					artist = artistDao.readByName(artistName);
					if (artist.getName() == null) {
						artist.setName(artistName);
						artist = artistDao.create(artist);
						art.setArtistId(artist.getId());
					} else {
						art.setArtistId(artist.getId());
					}
				}
				if (!categoryName.isEmpty()){
					if (categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
						// Получаем и устанавливаем id категории арта, если такой категории еще нет,
						// создаем ее
					Category cat = categoryDao.readByName(categoryName);
					if (cat.getName() == null) {
						cat.setName(categoryName);
						cat = categoryDao.create(cat);
						art.setCategoryId(cat.getId());
					} else {
						art.setCategoryId(cat.getId());
					}
				} else {
					String message = "Проверьте правильность заполнения поля Название категории"+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
					requestDispatcher.forward(req, resp);
				}
				}
				if (!originalUrl.isEmpty()){
				if(originalUrl.matches("^(?i)http://\\w+\\.(com|ru|by|ua|edu|gov|net|org).*$")){
					art.setOriginalUrl(originalUrl);
				} else {				
					String message = "Проверьте правильность заполнения поля Ссылка на оригинал.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
					requestDispatcher.forward(req, resp);
				}
				}
				artDao.update(art);
				// переходим на обновленную страницу арта (artAdmin)
				RequestDispatcher requestDispatcher = req
						.getRequestDispatcher("/ControllerServlet?artId=" + art.getId());
				requestDispatcher.forward(req, resp);
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Удаления арта
	public void deleteArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			if (req.getParameter("deleteArt").equals("Удалить")) {
				// deleteArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("deleteArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("deleteArt").equals("Удалить арт")) {
				String yes = req.getParameter("yes");
				if (yes != null) {
					Art art = artDao.read(artId);
					artDao.delete(art);
					req.getSession().removeAttribute("artId");
					req.removeAttribute("artId");
					req.removeAttribute("deleteArt");				
					// переходим на главную страницу
					resp.sendRedirect("/Chaos/ControllerServlet");				
				} else {
					// возвращаемся на страницу арта (artAdmin)
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
					requestDispatcher.forward(req, resp);
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// Форма Добавления комментария
	public void addComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if (req.getParameter("newComment").equals("Добавить комментарий")) {
				String userId = (String) req.getSession().getAttribute("userId");
				String comment = req.getParameter("comment");
				String artId = (String) req.getSession().getAttribute("artId");
				Comment com = new Comment();
				if (!comment.isEmpty()) {
					com.setText(comment);
					com.setUserId(userId);
					com.setArtId(artId);
					commentDao.create(com);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
					requestDispatcher.forward(req, resp);
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	public void exceptionPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		try {
			Exception e = (Exception) req.getSession().getAttribute("errorPage"); // получаем исключение из сессии
			req.getSession().removeAttribute("errorPage"); // обнуляем исключение в сессии
			e.printStackTrace();
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("errorPage.jsp");
			requestDispatcher.forward(req, resp);
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}
}
