package by.chaosart.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

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
				// ������� ��������
			} else if (!en.hasMoreElements() || req.getParameter("Chaos") != null) {
				mainPageProcessing(req, resp);
				// �������� ����������� ����
			} else if (req.getParameter("artId") != null) {
				artPageProcessing(req, resp);
				// ������� �������� � ������ ���������� ���������
			} else if (req.getParameter("categoryId") != null) {
				mainPageProcessing(req, resp);
				// ����� �����������
			} else if (req.getParameter("newAccount") != null) {
				regFormProcessing(req, resp);
				// ����� Login
			} else if (req.getParameter("logIn") != null) {
				loginFormProcessing(req, resp);
				// ��������� ����� ���������� ����� ���������
			} else if (req.getParameter("addCategory") != null) {
				addNewCategory(req, resp);
				// ��������� ����� ���������� ������ ����
			} else if (req.getParameter("addArt") != null) {
				addNewArt(req, resp);
				// ��������� ����� ��������� ����
			} else if (req.getParameter("updateArt") != null) {
				updateArt(req, resp);
				// ��������� ����� �������� ����
			} else if (req.getParameter("deleteArt") != null) {
				deleteArt(req, resp);
				// ��������� ����� ���������� ������ �����������
			} else if (req.getParameter("newComment") != null) {
				addComment(req, resp);
			}
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			exceptionPageProcessing(req, resp); // �������� ����������
		}
	}

	// ������� �� ������� ��������, ������ �� ���� ������������
	public void mainPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		try {
			if(categoryDao==null||artDao==null){
				DaoFactory factory = new MySqlDaoFactory();
				artDao = factory.getMySqlArtDao();
				categoryDao = factory.getMySqlCategoryDao();
				HttpSession session = req.getSession();
				session.setAttribute("artDao", artDao);
				session.setAttribute("categoryDao", categoryDao);
			}	
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

	// ������� �� �������� ����������� ����, ������ �� ���� ������������
	public void artPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		MySqlArtistDao artistDao = (MySqlArtistDao) req.getSession().getAttribute("artistDao");
		MySqlCommentDao commentDao = (MySqlCommentDao) req.getSession().getAttribute("commentDao");
		MySqlUserDao userDao = (MySqlUserDao) req.getSession().getAttribute("userDao");
		String artId = req.getParameter("artId");
		req.getSession().setAttribute("artId", artId);
		try {
			if(artDao==null||artistDao==null||commentDao==null||userDao==null){
				DaoFactory factory = new MySqlDaoFactory();
				artDao = factory.getMySqlArtDao();		
				artistDao = factory.getMySqlArtistDao();
				commentDao = factory.getMySqlCommentDao();
				userDao = factory.getMySqlUserDao();
				HttpSession session = req.getSession();
				session.setAttribute("artDao", artDao);
				session.setAttribute("artistDao", artistDao);
				session.setAttribute("commentDao", commentDao);
				session.setAttribute("userDao", userDao);
			}	
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
			// ��������� �� �������� art.jsp
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("art.jsp");
			requestDispatcher.forward(req, resp);
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	// ����� �����������
	public void regFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlUserDao userDao = (MySqlUserDao) req.getSession().getAttribute("userDao");
		try {
			if (req.getParameter("newAccount").equals("�����������")) {
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
				requestDispatcher.forward(req, resp);			
			} else if (req.getParameter("newAccount").equals("������")) {
				resp.sendRedirect("/Chaos/ControllerServlet");		
			} else if (req.getParameter("newAccount").equals("�������")) {
				String name = req.getParameter("name");
				String surname = req.getParameter("surname");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String password2 = req.getParameter("password2");
				// �������� ���������� ��������� ������
				if (name.isEmpty() || login.isEmpty() || password.isEmpty() || password2.isEmpty()) {
					String message = "���������� ��������� ��� ������������ ����";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!name.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					String message = "��������� ������������ ���������� ����� ��� � �������."
							+ "��������� ��������� ������ �������� �� ���� ���������� ��� �������� ��������,"
							+ "���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);					
				} else if (login.matches("^[*�;%:#&\'\"!)(\\.,]+") || password.matches("^[*�;%:#&\'\"!)(\\.,]+")) {
					String message = "��������� ������������ ���������� ����� ����� � ������."
							+ "(���� �� ������ ��������� �������� *, �, ?, %, ;, |, /, \\, (, ), &, !)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (!password.equals(password2)) {
					String message = "������ ������ ���������!";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					String hashCode = String.valueOf(password.hashCode());
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					User user = userDao.readByLogin(login);
					if (user.getLogin() != null) {
						String message = "������������ � ����� ������� ��� ����������.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("registration.jsp");
						requestDispatcher.forward(req, resp);					
					} else {
						// ������������� ��������� ������ ������������ � ������� ������ � ��
						if (hashCode.equals(adminHashCode)) {
							user.setRoleId(1);
						} else {
							user.setRoleId(2);
						}
						user.setName(name);
						user.setSurname(surname);
						user.setLogin(login);
						user.setPassword(hashCode);
						user = userDao.create(user);
						// ������� ������ � ���������� � ��� ��������� ������������
						HttpSession session = req.getSession();
						session.setAttribute("userId", user.getId());
						session.setAttribute("roleId", user.getRoleId());
						// ��������� �� ������� �������� (mainAdmin ��� mainUser � ����������� �� ����)
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

	// ����� �����
	public void loginFormProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlUserDao userDao = (MySqlUserDao) req.getSession().getAttribute("userDao");
		try {
			if (req.getParameter("logIn").equals("����")) {
				resp.sendRedirect("login.jsp");
			} else if (req.getParameter("logIn").equals("������")) {
				resp.sendRedirect("/Chaos/ControllerServlet");			
			} else if (req.getParameter("logIn").equals("�����")) {
				HttpSession session = req.getSession();
				session.removeAttribute("login");
				session.removeAttribute("password");
				session.removeAttribute("roleId");
				// ��������� �� ������� �������� (mainWithOutReg)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("logIn").equals("�����")) {
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String hashCode = String.valueOf(password.hashCode());
				User user = userDao.readByLogin(login);
				if (user.getLogin() == null) {
					String message = "��������� ������������ ��������� ������.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("login.jsp");
					requestDispatcher.forward(req, resp);				
				} else if (user.getPassword() != null && !hashCode.equals(user.getPassword())) {
					String message = "��������� ������������ ��������� ������.";				
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("login.jsp");
					requestDispatcher.forward(req, resp);				
				} else {
					Integer userId = user.getId();
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					HttpSession session = req.getSession(true);
					session.setAttribute("userId", userId);
					if (hashCode.equals(adminHashCode)) {
						session.setAttribute("roleId", new Integer(1));
						// ��������� �� ������� �������� (mainAdmin)
						resp.sendRedirect("/Chaos/ControllerServlet");
					} else {
						session.setAttribute("roleId", new Integer(2));
						// ��������� �� ������� �������� (mainUser)
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

	// ����� ���������� ����� ���������
	public void addNewCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		try {		
			if (req.getParameter("addCategory").equals("�������� ���������")) {
				// addCategory
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("addCategory").equals("������")) {
				// ��������� �� ������� �������� (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("addCategory").equals("�������")) {
				String categoryName = req.getParameter("category");
				// ��������� ��������� ������
				if (categoryName.isEmpty()) {
					String message = "�������� ��������� �� ����� ���� ������.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {				
					String message = "��������� ������������ ���������� ���� �������� ���������."+"\n"
							+ "(�������� ������ �������� �� ���� ���������� ��� �������� ��������,"+"\n"
							+ "���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						String message = "������ ��������� ��� ����������.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("addCategory.jsp");
						requestDispatcher.forward(req, resp);
					} else {
						// ������������� ��������� ���������, ���������� �� � ��
						category.setName(categoryName);
						categoryDao.create(category);
						// ��������� �� ������� �������� (mainAdmin)
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

	// ����� ���������� ������ ����
	public void addNewArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		MySqlArtistDao artistDao = (MySqlArtistDao) req.getSession().getAttribute("artistDao");
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		try {
			if (req.getParameter("addArt").equals("�������� ���")) {
				// addArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("addArt").equals("������")) {
				// ��������� �� ������� �������� (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("addArt").equals("�������")) {
				String artName = req.getParameter("artName");
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalURL = req.getParameter("originalURL");
				// ��������� ����������������� �����
				if (artName.isEmpty() || artistName.isEmpty() || categoryName.isEmpty() || originalURL.isEmpty()) {				
					String message = "��� ���� ����������� � ����������.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!artName.matches(".+\\.(jpg|png|jpeg|bmp|tif|gif)")) {
					String message = "��������� ������������ ���������� ���� �������� ����.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					String message = "��������� ������������ ���������� ���� �������� ���������."+"\n"
							+ "(�������� ������ �������� �� ���� ���������� ��� �������� ��������,"+"\n"
							+ "���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else if (!originalURL.matches("^(?i)http://.*$")) {				
					String message = "��������� ������������ ���������� ���� ������ �� ��������.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
					requestDispatcher.forward(req, resp);
				} else {
					Art art = artDao.readByName(artName);
					if (art.getName() != null) {										
						String message = "��� � ����� ��������� ��� ����������.";
						req.setAttribute("message", message);
						RequestDispatcher requestDispatcher = req.getRequestDispatcher("addArt.jsp");
						requestDispatcher.forward(req, resp);
					} else {
						// �������� � ������������� id ������ ����, ���� ������ ������ ��� ���, �������
						// ���
						Artist artist = artistDao.readByName(artistName);
						if (artist.getName() == null) {
							artist.setName(artistName);
							artist = artistDao.create(artist);
							art.setArtistId(artist.getId());
						} else {
							art.setArtistId(artist.getId());
						}
						// �������� � ������������� id ��������� ����, ���� ����� ��������� ��� ���,
						// ������� ��
						Category cat = categoryDao.readByName(categoryName);
						if (cat.getName() == null) {
							cat.setName(categoryName);
							cat = categoryDao.create(cat);
							art.setCategoryId(cat.getId());
						} else {
							art.setCategoryId(cat.getId());
						}
						// ������������� ��������� ����, ���������� ��� � ��
						art.setName(artName);
						art.setImage("img/content/" + artName);
						art.setOriginalUrl(originalURL);
						art = artDao.create(art);
						// ��������� �� �������� ���������� ����
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

	// ����� ��������� ����
	public void updateArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		MySqlArtistDao artistDao = (MySqlArtistDao) req.getSession().getAttribute("artistDao");
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			Art art = artDao.read(artId);
			Artist artist = artistDao.read(art.getArtistId());
			Category category = categoryDao.read(art.getCategoryId());
			req.getSession().setAttribute("art", art);
			req.getSession().setAttribute("artist", artist);
			req.getSession().setAttribute("category", category);
			if (req.getParameter("updateArt").equals("��������")) {			
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("updateArt").equals("������")) {
				// ������������ �� �������� ���� (artAdmin)
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("updateArt").equals("�������� ���")) {
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalUrl = req.getParameter("originalURL");
				
				if (!artistName.isEmpty()) {
					// �������� � ������������� id ������ ����, ���� ������ ������ ��� ���, �������
					// ���
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
						if (categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					// �������� � ������������� id ��������� ����, ���� ����� ��������� ��� ���,
					// ������� ��
					Category cat = categoryDao.readByName(categoryName);
					if (cat.getName() == null) {
						cat.setName(categoryName);
						cat = categoryDao.create(cat);
						art.setCategoryId(cat.getId());
					} else {
						art.setCategoryId(cat.getId());
					}
				} else {
					String message = "��������� ������������ ���������� ���� �������� ���������"+"\n"
							+ "(�������� ������ �������� �� ���� ���������� ��� �������� ��������,"+"\n"
							+ "���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
					requestDispatcher.forward(req, resp);
				}
				}
				if (!originalUrl.isEmpty()){
				if(originalUrl.matches("^(?i)http://\\w+\\.(com|ru|by|ua|edu|gov|net|org).*$")){
					art.setOriginalUrl(originalUrl);
				} else {				
					String message = "��������� ������������ ���������� ���� ������ �� ��������.";
					req.setAttribute("message", message);
					RequestDispatcher requestDispatcher = req.getRequestDispatcher("updateArt.jsp");
					requestDispatcher.forward(req, resp);
				}
				}
				artDao.update(art);
				// ��������� �� ����������� �������� ���� (artAdmin)
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

	// ����� �������� ����
	public void deleteArt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			if (req.getParameter("deleteArt").equals("�������")) {
				// deleteArt
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("deleteArt.jsp");
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("deleteArt").equals("������� ���")) {
				String yes = req.getParameter("yes");
				if (yes != null) {
					Art art = artDao.read(artId);
					artDao.delete(art);
					req.getSession().removeAttribute("artId");
					req.removeAttribute("artId");
					req.removeAttribute("deleteArt");				
					// ��������� �� ������� ��������
					resp.sendRedirect("/Chaos/ControllerServlet");				
				} else {
					// ������������ �� �������� ���� (artAdmin)
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

	// ����� ���������� �����������
	public void addComment(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MySqlCommentDao commentDao = (MySqlCommentDao) req.getSession().getAttribute("commentDao");
		try {
			if (req.getParameter("newComment").equals("�������� �����������")) {
				Integer userId = (Integer) req.getSession().getAttribute("userId");
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
			Exception e = (Exception) req.getSession().getAttribute("errorPage"); // �������� ���������� �� ������
			req.getSession().removeAttribute("errorPage"); // �������� ���������� � ������
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
