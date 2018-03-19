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
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);	
	}

	public void processing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		String controlParam = req.getParameter("controlParam");
		try {
			/*Если в сессии возникло исключение, переходим на страницу исключения*/
			if (req.getSession().getAttribute("errorPage")!= null) {
				exceptionPageProcessing(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/errorPage.jsp");
				requestDispatcher.forward(req, resp);		
			} else if(controlParam==null){			
				/*Проверяем контрольный параметр запроса и обрабатываем его соответствующим методом*/
				mainPageProcessing(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/main.jsp");
				requestDispatcher.forward(req, resp);
			} else if(controlParam.equals("art")){
				artPageProcessing(req, resp);							
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/art.jsp");
				requestDispatcher.forward(req, resp);
			} else if(controlParam.equals("newAccount")&&req.getParameter("newAccount")!=null&&req.getParameter("newAccount").equals("Регистрация")){				
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/registration.jsp");
				requestDispatcher.forward(req, resp);
			} else if(controlParam.equals("newAccount")&&req.getParameter("newAccount")!=null&&req.getParameter("newAccount").equals("Отмена")){
				resp.sendRedirect("/Chaos/ControllerServlet");	
			} else if (controlParam.equals("newAccount")&&req.getParameter("newAccount")!=null&&req.getParameter("newAccount").equals("Создать")){
				String url = regFormProcessing(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);			
			
			} else if(controlParam.equals("logIn")&&req.getParameter("logIn")!=null&&req.getParameter("logIn").equals("Вход")){
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/login.jsp");
				requestDispatcher.forward(req, resp);			
			} else if (controlParam.equals("logIn")&&req.getParameter("logIn")!=null&&req.getParameter("logIn").equals("Отмена")) {
				resp.sendRedirect("/Chaos/ControllerServlet");			
			} else if (controlParam.equals("logIn")&&req.getParameter("logIn")!=null&&req.getParameter("logIn").equals("Выйти")) {
				HttpSession session = req.getSession();
				session.removeAttribute("login");
				session.removeAttribute("password");
				session.removeAttribute("roleId");
				// переходим на главную страницу (mainWithOutReg)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (controlParam.equals("logIn")&&req.getParameter("logIn")!=null&&req.getParameter("logIn").equals("Войти")) {
				String url = loginFormProcessing(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);	
				
			} else if (controlParam.equals("addCategory")&&req.getParameter("addCategory")!=null&&req.getParameter("addCategory").equals("Добавить категорию")) {
				// addCategory
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addCategory.jsp");
				requestDispatcher.forward(req, resp);
			} else if (controlParam.equals("addCategory")&&req.getParameter("addCategory")!=null&&req.getParameter("addCategory").equals("Отмена")) {
				// переходим на главную страницу (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (controlParam.equals("addCategory")&&req.getParameter("addCategory")!=null&&req.getParameter("addCategory").equals("Создать")) {
				String url = addNewCategory(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);					
				
			} else if (controlParam.equals("addArt")&&req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("Добавить Арт")) {
				// addArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/addArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (controlParam.equals("addArt")&&req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("Отмена")) {
				// переходим на главную страницу (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (controlParam.equals("addArt")&&req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("Создать")) {
				String url = addNewArt(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);
	
			} else if (controlParam.equals("updateArt")&&req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("Изменить")) {
				String url = updateArt(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);
			} else if (controlParam.equals("updateArt")&&req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("Отмена")) {
				// возвращаемся на страницу арта (artAdmin)
				String artId = (String) req.getSession().getAttribute("artId");
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId="+artId+"&controlParam=art");
				requestDispatcher.forward(req, resp);
			} else if (controlParam.equals("updateArt")&&req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("Изменить Арт")) {			
				String url = updateArt(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);
				
			} else if (controlParam.equals("deleteArt")&&req.getParameter("deleteArt")!=null&&req.getParameter("deleteArt").equals("Удалить")) {
				// deleteArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("jsp/deleteArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (controlParam.equals("deleteArt")&&req.getParameter("deleteArt")!=null&&req.getParameter("deleteArt").equals("Удалить арт")) {
				String url = deleteArt(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher(url);
				requestDispatcher.forward(req, resp);

			} else if(controlParam.equals("newComment")){
				String artId = (String) req.getSession().getAttribute("artId");
				addComment(req, resp);
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId="+artId+"&controlParam=art");
				requestDispatcher.forward(req, resp);
			}		
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);		
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
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
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
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
	}

	// Форма Регистрации
	public String regFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			String url = null;
			try {
				String name = req.getParameter("name");
				String surname = req.getParameter("surname");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String password2 = req.getParameter("password2");
				/* Проверка валидности введенных данных */
				/* Проверка заполнения обязательных полей */
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("Имя", name);
				paramMap.put("Логин", login);
				paramMap.put("Пароль", password);
				paramMap.put("Повторите пароль", password2);
				Map<String, String> messageMap = new HashMap<String, String>();
				for(Iterator<String> i = paramMap.keySet().iterator(); i.hasNext();){
					String param = i.next();
					String value = paramMap.get(param);
					if(value.isEmpty()){
						messageMap.put(param, "Поле \""+param+"\" <br>является обязательным к заполнению!");
					}
				}
				/* Проверка корректности введенных данных поля Имя*/
				if (!name.isEmpty() && !name.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					messageMap.put("Имя", "Проверьте правильность заполнения поля Имя.<br>"
							+ "(формат: Иван либо Ivan)");
				}
				/* Проверка корректности введенных данных поля Логин */
				if (!login.isEmpty() && !login.matches("[\\w]*")) {
					messageMap.put("Логин", "Проверьте правильность заполнения поля Логин.<br>"
							+ "(допустимы лишь цифры и буквы)");
				}
				if (!password.isEmpty() && !password.matches("\\w*")) {
					messageMap.put("Пароль", "Проверьте правильность заполнения поля Пароль.<br>"
							+ "(допустимы лишь цифры и буквы)");
				}
				/* Проверка совпадения паролей */
				if (!password.equals(password2)) {
					messageMap.put("Повторите пароль", "Пароли должны совпадать!");
				} 
				/* Проверка уникальности логина */
				String hashCode = String.valueOf(password.hashCode());
				String adminPassword = "Admin";
				String adminHashCode = String.valueOf(adminPassword.hashCode());
				User user = userDao.readByLogin(login);
				if (user.getLogin() != null) {
					messageMap.put("Логин", "Пользователь с таким логином уже существует.");
				}
				/* В случае наличия невалидных введенных данных пользователь возвращается 
				 * на страницу регистрации с сообщениями о допущенных ошибках */
				if (!messageMap.isEmpty()){
					req.setAttribute("messageMap", messageMap);
					url =  "jsp/registration.jsp";
				} else {
					/* В случае успешной валидации введенных данных 
					 * устанавливаем параметры нового пользователя и создаем запись в БД */				  					 
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
					mainPageProcessing(req,resp);
					url = "jsp/main.jsp";
				}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
		return url;
	}

	// Форма Входа
	public String loginFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			String url = null;	
			try {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String hashCode = String.valueOf(password.hashCode());
				Map<String, String> messageMap = new HashMap<String, String>(); 
				User user = userDao.readByLogin(login);
				if (user.getLogin() == null) {
					String message = "Проверьте правильность написания логина.";
					messageMap.put("messageLogin", message);
					}					 
				if (user.getPassword()== null || !hashCode.equals(user.getPassword())) {
					String message = "Проверьте правильность написания пароля.";
					messageMap.put("messagePassword", message);	
					}
				if(!messageMap.isEmpty()){
					req.setAttribute("messageMap", messageMap);
					url = "jsp/login.jsp";
				} else {
					String userId = user.getId();
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					HttpSession session = req.getSession(true);
					session.setAttribute("userId", userId);
					if (hashCode.equals(adminHashCode)) {
						session.setAttribute("roleId", new String("1"));
					} else {
						session.setAttribute("roleId", new String("2"));
					}
					mainPageProcessing(req,resp);
					url = "jsp/main.jsp";
				}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
		return url;
	}

	// Форма Добавления новой категории
	public String addNewCategory(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
			String url = null;
			try {				
				String categoryName = req.getParameter("category");
				// Валидация введенных данных
				if (categoryName.isEmpty()) {
					String message = "Название категории не может быть пустым.";
					req.setAttribute("message", message);
					url = "jsp/addCategory.jsp";
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {				
					String message = "Проверьте данные в поле Название категории.<br>"
							+ "(формат: Красивые либо Beautiful)";
					req.setAttribute("message", message);
					url = "jsp/addCategory.jsp";
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						String message = "Данная категория уже существует.";
						req.setAttribute("message", message);
						url = "jsp/addCategory.jsp";
					} else {
						// устанавливаем параметры категории, записываем ее в БД
						category.setName(categoryName);
						categoryDao.create(category);
						mainPageProcessing(req, resp);
						url = "jsp/main.jsp";
					}
				}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
			return url;
	}

	// Форма Добавления нового арта
	public String addNewArt(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
			String url = null;
			try {
				String artName = req.getParameter("artName");
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalURL = req.getParameter("originalURL");
				/* Проверка валидности введенных данных */
				/* Проверка заполнения обязательных полей */
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("Название арта", artName);
				paramMap.put("Имя художника", artistName);
				paramMap.put("Название категории", categoryName);
				paramMap.put("Ссылка на оригинал", originalURL);
				Map<String, String> messageMap = new HashMap<String, String>();
				for(Iterator<String> i = paramMap.keySet().iterator(); i.hasNext();){
					String param = i.next();
					String value = paramMap.get(param);
					if(value.isEmpty()){
						messageMap.put(param, "Поле \""+param+"\" <br>является обязательным к заполнению!");
					}
				}
				/* Проверка поля Название арта */
				if (!artName.isEmpty() && !artName.matches(".+\\.(jpg|png|jpeg|bmp|tif|gif)")) {
					messageMap.put("Название арта", "Проверьте данные в поле Название арта.<br>"
							+ "(Формат: название файла. Пример: Арт1.jpg)");
				}
				/* Проверка поля Название категории */
				if (!categoryName.isEmpty() && !categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")) {
					messageMap.put("Название категории", "Проверьте данные в поле Название категории.<br>"
							+ "(формат: Красивые либо Beautiful)");
				}
				/* Проверка поля Ссылка на оригинал */
				if (!originalURL.isEmpty() && !originalURL.matches("^(?i)http[s]{0,1}://.*$")) {				
					messageMap.put("Ссылка на оригинал", "Проверьте данные в поле Ссылка на оригинал.<br>"
							+ "(формат: http://... либо https://...)");			
				}
				/* Проверка на уникальность добавляемого арта */
				Art art = artDao.readByName(artName);
				if (art.getName() != null) {										
					messageMap.put("Название арта", "Арт с таким названием уже существует.");
				}		
				/* В случае наличия невалидных введенных данных пользователь возвращается 
				 * на страницу добавления арта с сообщениями о допущенных ошибках */
				if (!messageMap.isEmpty()){
					req.setAttribute("messageMap", messageMap);
					url = "jsp/addArt.jsp";
				} else {
					/* В случае успешной валидации введенных данных 
					 * получаем и устанавливаем id автора арта, если такого автора еще нет, создаем его */												
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
					url = "/ControllerServlet?artId="+art.getId()+"&controlParam=art";
				}	
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
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
			if(req.getParameter("updateArt").equals("Изменить")){
				req.getSession().setAttribute("art", art);
				req.getSession().setAttribute("artist", artist);
				req.getSession().setAttribute("category", category);
				url = "jsp/updateArt.jsp";
			} else if(req.getParameter("updateArt").equals("Изменить Арт")){	
				String artistName = req.getParameter("artistName");			
				String categoryName = req.getParameter("categoryName");
				String originalUrl = req.getParameter("originalURL");
				Map<String, String> messageMap = new HashMap<String, String>();
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
				if (!categoryName.isEmpty() && 
					categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)")){
					/* Получаем и устанавливаем id категории арта, если такой категории еще нет,
					*создаем ее */
					Category cat = categoryDao.readByName(categoryName);
					if (cat.getName() == null) {
						cat.setName(categoryName);
						cat = categoryDao.create(cat);
						art.setCategory(cat);
					} else {
						art.setCategory(cat);
					}
				} else {
					messageMap.put("Название категории", "Проверьте данные в поле Название категории.<br>"
						+ "(формат: Красивые либо Beautiful)");
				}
				
				if (!originalUrl.isEmpty() && originalUrl.matches("^(?i)http[s]{0,1}://\\.*$")){
					art.setOriginalUrl(originalUrl);
				} else {				
					messageMap.put("Ссылка на оригинал", "Проверьте данные в поле Ссылка на оригинал.<br>"
							+ "(формат: http://... либо https://...)");								
				}
				if (!messageMap.isEmpty()){
					req.setAttribute("messageMap", messageMap);
					url = "jsp/updateArt.jsp";
				} else {
					artDao.update(art);
					artPageProcessing(req, resp);
					url = "jsp/art.jsp";		
				}
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
			return url;
	}

	// Форма Удаления арта
	public String deleteArt(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String url = null;	
		String artId = (String) req.getSession().getAttribute("artId");	
			try {
				String yes = req.getParameter("yes");
				if (yes != null) {
				Art art = artDao.read(artId);				
				artDao.delete(art);
				req.getSession().removeAttribute("artId");
				req.removeAttribute("artId");
				req.removeAttribute("deleteArt");
				url = "jsp/main.jsp";
				} else {
					url = "/ControllerServlet?artId="+artId+"&controlParam=art";
				}
			} catch (Exception e) {
				req.getSession().setAttribute("errorPage", e);
			}
			return url;
		}

	// Форма Добавления комментария
	public void addComment(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
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
			} catch (Exception e) {
				req.getSession().setAttribute("errorPage", e);
			}
		}

	public void exceptionPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		try {
			Exception e = (Exception) req.getSession().getAttribute("errorPage"); // получаем исключение из сессии
			req.getSession().removeAttribute("errorPage"); // обнуляем исключение в сессии
			e.printStackTrace();
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
		}
	}
}
