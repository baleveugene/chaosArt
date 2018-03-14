package by.chaosart.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void processing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		Map<String, String[]> reqParam = req.getParameterMap();	
		try {
			/*Если в сессии возникло исключение, переходим на страницу исключения*/
			if (req.getSession().getAttribute("errorPage") != null) {
				exceptionPageProcessing(req, resp);
			/*Если в запросе нет параметров, переходим на главную страницу*/
			} else if(reqParam.keySet().isEmpty()){
				mainPageProcessing(req, resp);
			} else {			
				/*Проверяем параметры запроса и обрабатываем его соответствующим методом*/
				for(Iterator<String> i = reqParam.keySet().iterator(); i.hasNext();){
					String param = i.next();
					String[] value = reqParam.get(param);
					if(value!=null){
						switch(param){
							case("Chaos"):
								mainPageProcessing(req, resp);
								break;
							case("artId"):
								artPageProcessing(req, resp);
								break;
							case("categoryId"):
								mainPageProcessing(req, resp);
								break;
							case("newAccount"):
								regFormProcessing(req, resp);
								break;
							case("logIn"):
								loginFormProcessing(req, resp);
								break;
							case("addCategory"):
								addNewCategory(req, resp);
								break;
							case("addArt"):
								addNewArt(req, resp);
								break;
							case("updateArt"):
								updateArt(req, resp);
								break;
							case("deleteArt"):
								deleteArt(req, resp);
								break;
							case("newComment"):
								addComment(req, resp);
								break;
							default:
								continue;
							}		
					} return;
				}
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
				artList = artDao.getAllOfCat(categoryDao.read(categoryId));
				req.getSession().setAttribute("artList", artList);
			}
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/main.jsp");
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
			Artist artist = art.getArtist();
			req.getSession().setAttribute("artist", artist);
			List<Comment> commentList = commentDao.getAll(art);
			req.getSession().setAttribute("commentList", commentList);
			List<User> userList = new ArrayList<User>();
			if(commentList!=null) {			
				for (Comment c : commentList) {
					User u = c.getUser();
					userList.add(u);			
				}
			}
			req.getSession().setAttribute("userList", userList);
			List<Art> artList = artist.getArts();
			req.getSession().setAttribute("artList", artList);
			// Переходим на страницу art.jsp
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/art.jsp");
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
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
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
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!name.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					String message = "Проверьте правильность заполнения полей Имя и Фамилия."
							+ "Введенные параметры должны состоять из букв латинского или русского алфавита,"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
					requestDispatcher.forward(req, resp);					
				} else if (login.matches("^[*№;%:#&\'\"!)(\\.,]+") || password.matches("^[*№;%:#&\'\"!)(\\.,]+")) {
					String message = "Проверьте правильность заполнения полей Логин и Пароль."
							+ "(поля не должны содержать символов *, №, ?, %, ;, |, /, \\, (, ), &, !)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (!password.equals(password2)) {
					String message = "Пароли должны совпадать!";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					String hashCode = String.valueOf(password.hashCode());
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					User user = userDao.readByLogin(login);
					if (user.getLogin() != null) {
						String message = "Пользователь с таким логином уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
						requestDispatcher.forward(req, resp);					
					} else {
						// устанавливаем параметры нового пользователя и создаем запись в БД
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
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
				requestDispatcher.forward(req, resp);
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
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (user.getPassword() != null && !hashCode.equals(user.getPassword())) {
					String message = "Проверьте правильность написания пароля.";				
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
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
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addCategory.jsp");
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
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {				
					String message = "Проверьте правильность заполнения поля Название категории."+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						String message = "Данная категория уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addCategory.jsp");
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
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
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
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!artName.matches(".+\\.(jpg|png|jpeg|bmp|tif|gif)")) {
					String message = "Проверьте правильность заполнения поля Название арта.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					String message = "Проверьте правильность заполнения поля Название категории."+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!originalURL.matches("^(?i)http[s ]://.*$")) {				
					String message = "Проверьте правильность заполнения поля Ссылка на оригинал.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Art art = artDao.readByName(artName);
					if (art.getName() != null) {										
						String message = "Арт с таким названием уже существует.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
						requestDispatcher.forward(req, resp);
					} else {
						// Получаем и устанавливаем id автора арта, если такого автора еще нет, создаем его	
						Artist artist = artistDao.readByName(artistName);
						if (artist.getName() == null) {
							artist.setName(artistName);
							artist = artistDao.create(artist);
							art.setArtist(artist);
						} else {
							art.setArtist(artist);
						}
						// Получаем и устанавливаем id категории арта, если такой категории еще нет, создаем ее
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
			Artist artist = art.getArtist();
			Category category = art.getCategory();
			req.getSession().setAttribute("art", art);
			req.getSession().setAttribute("artist", artist);
			req.getSession().setAttribute("category", category);
			if (req.getParameter("updateArt").equals("Изменить")) {			
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/updateArt.jsp");
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
						art.setArtist(artist);
					} else {
						art.setArtist(artist);
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
						art.setCategory(cat);
					} else {
						art.setCategory(cat);
					}
				} else {
					String message = "Проверьте правильность заполнения поля Название категории"+"\n"
							+ "(название должно состоять из букв латинского или русского алфавита,"+"\n"
							+ "начинаться с заглавной буквы и содержать количество символов в диапазоне от 1 до 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/updateArt.jsp");
					requestDispatcher.forward(req, resp);
				}
				}
				if (!originalUrl.isEmpty()){
					if(originalUrl.matches("^(?i)http://\\.*$")){
						art.setOriginalUrl(originalUrl);
				} else {				
					String message = "Проверьте правильность заполнения поля Ссылка на оригинал.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/updateArt.jsp");
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
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/deleteArt.jsp");
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
					com.setUser(userDao.read(userId));
					com.setArt(artDao.read(artId));
					commentDao.create(com);
				}
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
				requestDispatcher.forward(req, resp);
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
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/errorPage.jsp");
			requestDispatcher.forward(req, resp);
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}
}
