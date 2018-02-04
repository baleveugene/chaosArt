package by.java.dokwork.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.java.dokwork.domain.Art;
import by.java.dokwork.domain.Artist;
import by.java.dokwork.domain.Category;
import by.java.dokwork.domain.Comment;
import by.java.dokwork.domain.User;
import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlArtistDao;
import by.java.dokwork.mysql.MySqlCategoryDao;
import by.java.dokwork.mysql.MySqlCommentDao;
import by.java.dokwork.mysql.MySqlUserDao;

public class ControllerServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processing(req, resp);
	}

	public void processing(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		Enumeration<String> en = req.getParameterNames();
		try {
			if (req.getSession().getAttribute("errorPage") != null) {
				getExceptionPage(req, resp);
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
			getExceptionPage(req, resp); // �������� ����������
		}
	}

	// ������� �� ������� ��������, ������ �� ���� ������������
	public void mainPageProcessing(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		Integer roleId = (Integer) req.getSession().getAttribute("roleId");
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("main", req, resp);
			pw.println("</head>");
			pw.println("<body>");
			getHeader(req, resp); // header ��������
			pw.println("<div id=\"sidebar\">");
			pw.println("<h3>���������</h3>");
			List<Category> categoryList = categoryDao.getAll();
			for (Category c : categoryList) {
				pw.println("<p><a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" + c.getId() + "\">"
						+ c.getName() + "</a></p>");
			}
			if (roleId != null && roleId.equals(1)) {
				pw.println("<form name = \"addCategory\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				pw.println("<input id=\"button\" type=\"submit\" name = \"addCategory\" value=\"�������� ���������\">");
				pw.println("</form>");
			}
			pw.println("</div>");
			pw.println("<div id=\"content\">");
			if (roleId != null && roleId.equals(1)) {
				pw.println("<form name = \"addArt\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"�������� ���\">");
				pw.println("</form>");
			}
			List<Art> artList = artDao.getAll();
			if (req.getParameter("categoryId") != null) {
				String categoryId = req.getParameter("categoryId");
				String categoryName = categoryDao.read(categoryId).getName();
				pw.println("<h2>" + categoryName + "</h2>");
				artList = artDao.getAllOfCat(categoryId);
			}
			for (Art art : artList) {
				pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + art.getId() + "\"><img src= \""
						+ art.getImage() + "\"height=\"300\"></a>");
			}
			pw.println("</div>");
			getFooter(req, resp); // footer ��������
			pw.println("</body>");
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
		Integer roleId = (Integer) req.getSession().getAttribute("roleId");
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("art", req, resp);
			pw.println("</head>");
			pw.println("<body>");
			getHeader(req, resp); // header ��������
			Art art = artDao.read(artId);
			String artistId = art.getArtistId();
			Artist artist = artistDao.read(artistId);
			pw.println("<div id=\"content\">");
			pw.println("<div id=\"art\">");
			pw.println("<img src= \"" + art.getImage() + "\" height=55% >");
			if (roleId != null && roleId.equals(1)) {
				pw.println("<div id=\"buttons\">");
				pw.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"��������\">");
				pw.println("<input id=\"button\" type=\"submit\" name = \"deleteArt\" value=\"�������\">");
				pw.println("</form>");
				pw.println("</div>");
			}
			pw.println("</div>");
			pw.println("<div id=\"comments\">");
			pw.println("<h3>�����������</h3>");
			pw.println("<table>");
			List<Comment> commentList = commentDao.getAll(artId);
			for (Comment c : commentList) {
				User u = userDao.read(c.getUserId());
				pw.println("<tr>");
				pw.println("<td id=\"td1\">" + u.getName() + "</td>");
				pw.println("<td>" + c.getText() + "</td>");
				pw.println("</tr>");
			}
			pw.println("</table>");
			if (roleId != null && (roleId.equals(1) || roleId.equals(2))) {
				pw.println("<div id=\"form\">");
				pw.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				pw.println(
						"<textarea rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"����� �����������\"/></textarea>");
				pw.println(
						"<input id=\"button\" type=\"submit\" name = \"newComment\" value=\"�������� �����������\">");
				pw.println("</form>");
				pw.println("</div>");
			}
			pw.println("</div>");
			pw.println("</div>");
			pw.println("<div id=\"sidebar\">");
			pw.println("<h2>��� ������ ��  " + artist.getName() + "</h2>");
			List<Art> artList = artDao.getAll(artistId);
			for (Art a : artList) {
				pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId=" + a.getId() + "\"><img src= \""
						+ a.getImage() + "\"height=\"120\"></a>");
			}
			pw.println("</div>");
			getFooter(req, resp); // footer ��������
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("newAccount").equals("�����������")) {
				// registration
				getRegistrationPage(req, resp);
			} else if (req.getParameter("newAccount").equals("�������")) {
				String name = req.getParameter("name");
				String surname = req.getParameter("surname");
				String login = req.getParameter("login");
				String password = req.getParameter("password");
				String password2 = req.getParameter("password2");
				// �������� ���������� ��������� ������
				if (name.isEmpty() || login.isEmpty() || password.isEmpty() || password2.isEmpty()) {
					pw.println("<h3>���������� ��������� ��� ������������ ����</h3>");
					getRegistrationPage(req, resp);
				} else if (!name.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					pw.println("<h3>��������� ������������ ���������� ����� ��� � �������.</h3>");
					pw.println(
							"<h3>(��������� ��������� ������ �������� �� ���� ���������� ��� �������� ��������,</h3>");
					pw.println(
							"<h3>���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)</h3>");
					getRegistrationPage(req, resp);
				} else if (login.matches("^[*�;%:#&\'\"!)(\\.,]+") || password.matches("^[*�;%:#&\'\"!)(\\.,]+")) {
					pw.println("<h3>��������� ������������ ���������� ����� ����� � ������.</h3>");
					pw.println("<h3>(�� ������ ��������� �������� *, �, ?, %, ;, |, /, \\, (, ), &, !)</h3>");
					getRegistrationPage(req, resp);
				} else if (!password.equals(password2)) {
					pw.println("<h3>������ ������ ���������!</h3>");
					getRegistrationPage(req, resp);
				} else {
					String hashCode = String.valueOf(password.hashCode());
					String adminPassword = "Admin";
					String adminHashCode = String.valueOf(adminPassword.hashCode());
					User user = userDao.readByLogin(login);
					if (user.getLogin() != null) {
						pw.println("<h3>������������ � ����� ������� ��� ����������.</h3>");
						// registration
						getRegistrationPage(req, resp);
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

	public void getRegistrationPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("registration", req, resp);
			pw.println("<body>");
			pw.println("<div id=\"registration-page\">");
			pw.println("<div id=\"form\">");
			pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input type=\"text\" name = \"name\" placeholder=\"��� (������������ ����)\"/>");
			pw.println("<input type=\"text\" name = \"surname\" placeholder=\"�������\"/>");
			pw.println("<input type=\"text\" name = \"login\" placeholder=\"����� (������������ ����)\"/>");
			pw.println("<input type=\"password\" name = \"password\" placeholder=\"������ (������������ ����)\"/>");
			pw.println("<input type=\"password\" name = \"password2\" placeholder=\"��������� ������ \"/>");
			pw.println("<input id=\"button\" type=\"submit\" name = \"newAccount\" value=\"�������\">");
			pw.println("<p id=\"message\">��� ����������������? "
					+ "<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"link\" type=\"submit\" name = \"logIn\" value=\"����\">");
			pw.println("</form></p>");
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("logIn").equals("����")) {
				// login
				getLoginPage(req, resp);
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
					pw.println("<h3>��������� ������������ ��������� ������.</h3>");
					// login
					getLoginPage(req, resp);
				} else if (user.getPassword() != null && !hashCode.equals(user.getPassword())) {
					pw.println("<h3>��������� ������������ ��������� ������.</h3>");
					// login
					getLoginPage(req, resp);
				} else {
					String userId = String.valueOf(user.getId());
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

	public void getLoginPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("login", req, resp);
			pw.println("<body>");
			pw.println("<div id=\"login-page\">");
			pw.println("<div id=\"form\">");
			pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input type=\"text\" name = \"login\" placeholder=\"�����\"/>");
			pw.println("<input type=\"password\" name = \"password\" placeholder=\"������\"/>");
			pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"�����\">");
			pw.println("<p id=\"message\">��� �� ����������������? "
					+ "<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"link\" type=\"submit\" name = \"newAccount\" value=\"�����������\">");
			pw.println("</form></p>");
			pw.println("</form>");
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("addCategory").equals("�������� ���������")) {
				// addCategory
				getAddCategoryPage(req, resp);
			} else if (req.getParameter("addCategory").equals("������")) {
				// ��������� �� ������� �������� (mainAdmin)
				resp.sendRedirect("/Chaos/ControllerServlet");
			} else if (req.getParameter("addCategory").equals("�������")) {
				String categoryName = req.getParameter("category");
				// ��������� ��������� ������
				if (categoryName.isEmpty()) {
					pw.println("<h3>�������� ��������� �� ����� ���� ������</h3>");
					getAddCategoryPage(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					pw.println("<h3>��������� ������������ ���������� ���� �������� ���������.</h3>");
					pw.println("<h3>(�������� ������ �������� �� ���� ���������� ��� �������� ��������,</h3>");
					pw.println(
							"<h3>���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)</h3>");
					getAddCategoryPage(req, resp);
				} else {
					Category category = categoryDao.readByName(categoryName);
					if (category.getName() != null) {
						pw.println("<h3>������ ��������� ��� ����������.</h3>");
						// addCategory
						getAddCategoryPage(req, resp);
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

	public void getAddCategoryPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("addCategory", req, resp);
			pw.println("<body>");
			pw.println("<div id=\"addCat-page\">");
			pw.println("<div id=\"form\">");
			pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<p id=\"message\">������� �������� ���������:</p>");
			pw.println("<input type=\"text\" name = \"category\" placeholder=\"�������� ���������\"/>");
			pw.println("<input id=\"button\" type=\"submit\" name = \"addCategory\" value=\"�������\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"addCategory\" value=\"������\">");
			pw.println("</form>");
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("addArt").equals("�������� ���")) {
				// addArt
				getAddArtPage(req, resp);
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
					pw.println("<h3>��� ���� ����������� � ����������</h3>");
					getAddArtPage(req, resp);
				} else if (!artName.matches(".+\\.(jpg|png|jpeg|bmp|tif|gif)")) {
					pw.println("<h3>��������� ������������ ���������� ���� �������� ����.</h3>");
					getAddArtPage(req, resp);
				} else if (!categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)")) {
					pw.println("<h3>��������� ������������ ���������� ���� �������� ���������.</h3>");
					pw.println("<h3>(�������� ������ �������� �� ���� ���������� ��� �������� ��������,</h3>");
					pw.println(
							"<h3>���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)</h3>");
					getAddArtPage(req, resp);
				} else if (!originalURL.matches("^(?i)http://\\w+\\.(com|ru|by|ua|edu|gov|net|org)$")) {
					pw.println("<h3>��������� ������������ ���������� ���� ������ �� ��������.</h3>");
					getAddArtPage(req, resp);
				} else {
					Art art = artDao.readByName(artName);
					if (art.getName() != null) {
						pw.println("<h3>��� � ����� ��������� ��� ����������.</h3>");
						// addArt
						getAddArtPage(req, resp);
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

	public void getAddArtPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("addArt", req, resp);
			pw.println("<body>");
			pw.println("<div id=\"login-page\">");
			pw.println("<div id=\"form\">");
			pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<p id=\"message\">������� ��������� ����:</p>");
			pw.println("<input type=\"text\" name = \"artName\" placeholder=\"�������� ���� (������: ���1.jpg)\"/>");
			pw.println("<input type=\"text\" name = \"artistName\" placeholder=\"��� ���������\"/>");
			pw.println("<input type=\"text\" name = \"category\" placeholder=\"���������\"/>");
			pw.println("<input type=\"text\" name = \"originalURL\" placeholder=\"������ �� ��������\"/>");
			pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"�������\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"������\">");
			pw.println("</form>");
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("updateArt").equals("��������")) {
				// updateArt
				getUpdateArtPage(req, resp);
			} else if (req.getParameter("updateArt").equals("������")) {
				// ������������ �� �������� ���� (artAdmin)
				RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId=" + artId);
				requestDispatcher.forward(req, resp);
			} else if (req.getParameter("updateArt").equals("�������� ���")) {
				String artistName = req.getParameter("artistName");
				String categoryName = req.getParameter("category");
				String originalUrl = req.getParameter("originalURL");
				Art art = artDao.read(artId);
				if (!artistName.isEmpty()) {
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
				}
				if (!categoryName.isEmpty()
						&& (categoryName.matches("(^[A-Z]{1}[a-z]{0,20}$)|(^[�-�]{1}[�-�]{0,20}$)"))) {
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
					pw.println("<h3>��������� ������������ ���������� ���� �������� ���������.</h3>");
					pw.println("<h3>(�������� ������ �������� �� ���� ���������� ��� �������� ��������,</h3>");
					pw.println(
							"<h3>���������� � ��������� ����� � ��������� ���������� �������� � ��������� �� 1 �� 21)</h3>");
					getUpdateArtPage(req, resp);
				}
				if (!originalUrl.isEmpty()
						&& originalUrl.matches("^(?i)http://\\w+\\.(com|ru|by|ua|edu|gov|net|org)$")) {
					art.setOriginalUrl(originalUrl);
				} else {
					pw.println("<h3>��������� ������������ ���������� ���� ������ �� ��������.</h3>");
					getUpdateArtPage(req, resp);
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

	public void getUpdateArtPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MySqlArtDao artDao = (MySqlArtDao) req.getSession().getAttribute("artDao");
		MySqlArtistDao artistDao = (MySqlArtistDao) req.getSession().getAttribute("artistDao");
		MySqlCategoryDao categoryDao = (MySqlCategoryDao) req.getSession().getAttribute("categoryDao");
		String artId = (String) req.getSession().getAttribute("artId");
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("updateArt", req, resp);
			pw.println("<body>");
			pw.println("<div id=\"update-page\">");
			pw.println("<div id=\"form\">");
			Art art = artDao.read(artId);
			Artist artist = artistDao.read(art.getArtistId());
			Category category = categoryDao.read(art.getCategoryId());
			pw.println("<div id=\"content\">");
			pw.println("<img src= \"" + art.getImage() + "\" width= \"100%\">");
			pw.println("</div>");
			pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<p id=\"message\">��� ������:</p>");
			pw.println("<input type=\"text\" name = \"artistName\" placeholder=\"" + artist.getName() + "\"/>");
			pw.println("<p id=\"message\">���������:</p>");
			pw.println("<input type=\"text\" name = \"category\" placeholder=\"" + category.getName() + "\"/>");
			pw.println("<p id=\"message\">������ �� ��������:</p>");
			pw.println("<input type=\"text\" name = \"originalURL\" placeholder=\"" + art.getOriginalUrl() + "\"/>");
			pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"�������� ���\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"������\">");
			pw.println("</form>");
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</body>");
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
			PrintWriter pw = resp.getWriter();
			if (req.getParameter("deleteArt").equals("�������")) {
				// deleteArt
				pw.println("<head>");
				pw.println("<title>ChaosArt</title>");
				pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
				getCss("deleteArt", req, resp);
				pw.println("<body>");
				pw.println("<div id=\"delete-page\">");
				pw.println("<div id=\"form\">");
				pw.println("<div id=\"content\">");
				Art art = artDao.read(artId);
				pw.println("<img src= \"" + art.getImage() + "\" width= \"100%\">");
				pw.println("</div>");
				pw.println("<form id=\"delete-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
				pw.println("<p id=\"message\">�� ������������� ������ ������� ���� ���?</p>");
				pw.println("<input type=\"hidden\" name = \"deleteArt\" value=\"������� ���\"/>");
				pw.println("<input id=\"button\" type=\"submit\" name = \"yes\" value=\"��\"/>");
				pw.println("<input id=\"button\" type=\"submit\" name = \"no\" value=\"���\"/>");
				pw.println("</form>");
				pw.println("</div>");
				pw.println("</body>");
			} else if (req.getParameter("deleteArt").equals("������� ���")) {
				String yes = req.getParameter("yes");
				if (yes != null) {
					Art art = artDao.read(artId);
					artDao.delete(art);
					req.getSession().removeAttribute("artId");
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
				String userId = String.valueOf(req.getSession().getAttribute("userId"));
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

	public void getHeader(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<div id=\"header\">");
			pw.println(
					"<div id=\"logo\"><a href=\"/Chaos\"><img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			if (req.getSession().getAttribute("roleId") != null) {
				pw.println("<input id=\"button\" type=\"submit\" name=\"logIn\" value=\"�����\">");
			} else {
				pw.println("<input id=\"button\" type=\"submit\" name=\"newAccount\" value=\"�����������\">");
				pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"����\">");
			}
			pw.println("</form>");
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	public void getFooter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter pw = resp.getWriter();
			pw.println("<div id=\"footer\">&copy; Balev</div>");
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	public void getExceptionPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter pw;
		try {
			Exception e = (Exception) req.getSession().getAttribute("errorPage"); // �������� ���������� �� ������
			req.getSession().setAttribute("errorPage", null); // �������� ���������� � ������
			e.printStackTrace();
			pw = resp.getWriter();
			pw.println("<head>");
			pw.println("<title>ChaosArt</title>");
			pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("login", req, resp);
			pw.println("<body>");			
			pw.println("<div id=\"login-page\">");
			pw.println("<form id=\"form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<h3>���� ���������� ���� ���...</h3>");
			pw.println("<h3>���������� ��������� � �������!</h3>");
			pw.println("<a id=\"link\" href=\"/Chaos\">� �������</a>");
			pw.println("</form>");
			pw.println("</div>");
			pw.println("</body>");
		} catch (Exception e) {
			req.getSession().setAttribute("errorPage", e);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet");
			requestDispatcher.forward(req, resp);
		}
	}

	public void getCss(String page, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
		PrintWriter pw = resp.getWriter();

		switch (page) {

		case "main":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 10pt Arial, Helvetica, sans-serif;");
			pw.println("margin: 0;");
			pw.println("}");
			pw.println("#header {");
			pw.println("margin-top: 0;");
			pw.println("background-image: url(img/back_for_Chaos.jpg);");
			pw.println("background-position: center center;");
			pw.println("padding: 5px;");
			pw.println("height: 12%;");
			pw.println("}");
			pw.println("#logo {");
			pw.println("float: left;");
			pw.println("}");
			pw.println("#rightsideofheader{");
			pw.println("float: right;");
			pw.println("width: 30%;");
			pw.println("}");
			pw.println("#rightLinks{");
			pw.println("float: left;");
			pw.println("width: 40%;");
			pw.println("margin: 10% 0;");
			pw.println("}");
			pw.println("#rightTabs{");
			pw.println("float: right;");
			pw.println("margin: 3% 0;");
			pw.println("}");
			pw.println("a {"); /* ����� ������ */
			pw.println("font-family: sans-serif;");
			pw.println("font-size: 14px;");
			pw.println("color: #ffffff;");
			pw.println("margin: 0 5px 0 0;");
			pw.println("}");
			pw.println("#link {"); /* ����� ������ �� �������� ���� */
			pw.println("font-family: sans-serif;");
			pw.println("font-size: 16px;");
			pw.println("color: #111111;");
			pw.println("}");
			pw.println("#sidebar {"); /* ����� ������� */
			pw.println("float: left;"); /* ��������� ������ */
			pw.println("width: 15%;"); /* ������ ������� */
			pw.println("height: 80%;"); /* ������ ������� */
			pw.println("padding: 10px;");/* ���� ������ ������ */
			pw.println("background: #EAEBCA;");
			pw.println("}");
			pw.println("#content { ");/* ������� */
			pw.println("position: relative;");
			pw.println("z-index: 2;");
			pw.println("margin: 10px 10px 20px 17%;"); /* �������� �������� */
			pw.println("padding: 15px;"); /* ���� ������ ������ */
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#img { ");/* ����������� */
			pw.println("padding: 10px 10px 10px 0;"); /* ���� ������ ������ */
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #567348;");
			pw.println("border: 0;");
			pw.println("margin: 10px 8px 0 0;");
			pw.println("padding: 12px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("#footer { ");/* ������ ���� */
			pw.println("background: #453B78; ");
			pw.println("padding: 5px; ");
			pw.println("color: #FFFFFF; ");
			pw.println("clear: left; ");
			pw.println("}");
			pw.println("</style>");
			break;

		case "art":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 10pt Arial, Helvetica, sans-serif;");
			pw.println("margin: 0;");
			pw.println("}");
			pw.println("#header {");
			pw.println("margin-top: 0;");
			pw.println("background-image: url(img/back_for_Chaos.jpg);");
			pw.println("background-position: center center;");
			pw.println("padding: 5px;");
			pw.println("height: 12%;");
			pw.println("}");
			pw.println("#logo {");
			pw.println("float: left;");
			pw.println("}");
			pw.println("#rightsideofheader{");
			pw.println("float: right;");
			pw.println("width: 30%;");
			pw.println("}");
			pw.println("#rightLinks{");
			pw.println("float: left;");
			pw.println("width: 40%;");
			pw.println("margin: 8% 0;");
			pw.println("}");
			pw.println("a {"); /* ����� ������ */
			pw.println("font-family: sans-serif;");
			pw.println("font-size: 14px;");
			pw.println("color: #ffffff;");
			pw.println("margin: 0 5px 0 0;");
			pw.println("}");
			pw.println("#link {"); /* ����� ������ �� �������� ���� */
			pw.println("font-family: sans-serif;");
			pw.println("font-size: 14px;");
			pw.println("color: #111111;");
			pw.println("}");
			pw.println("#rightTabs{");
			pw.println("float: right;");
			pw.println("margin: 3% 0;");
			pw.println("}");
			pw.println("#sidebar {"); /* ������ ������� */
			pw.println("padding: 15px;");
			pw.println("margin: 0;");
			pw.println("height: 100%;");
			pw.println("text-align: center;");
			pw.println("font-size: 12px;");
			pw.println("background: #EAEBCA;");
			pw.println("}");
			pw.println("#content { ");/* ������� */
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("float: left;");
			pw.println("width: 60%;");
			pw.println("background: #FFFFFF;");
			pw.println("padding: 15px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#art { ");
			pw.println("width: 95%;");
			pw.println("float: center;");
			pw.println("padding: 10px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#artInfo { ");
			pw.println("float: right;");
			pw.println("width: 20%;");
			pw.println("padding: 10px;");
			pw.println("}");
			pw.println("#comments { ");
			pw.println("float: left;");
			pw.println("width: 100%;");
			pw.println("text-align: left;");
			pw.println("}");
			pw.println("table{ ");/* ������� */
			pw.println("float: left;");
			pw.println("table-layout: fixed;");
			pw.println("width: 100%;");
			pw.println("margin: 0 0 10px;");
			pw.println("text-align: left;");
			pw.println("padding: 10px;"); /* ���� ������ ������ */
			pw.println("border: 2px solid #AAAAAA;");
			pw.println("background: #444444;");
			pw.println("}");
			pw.println("#infoTable{ ");/* ������� */
			pw.println("float: left;");
			pw.println("table-layout: fixed;");
			pw.println("width: 100%;");
			pw.println("margin: 0 0 10px;"); /* �������� �������� */
			pw.println("text-align: left;");
			pw.println("padding: 5px;"); /* ���� ������ ������ */
			pw.println("border: 0;");
			pw.println("background: #ffffff;");
			pw.println("}");
			pw.println("td{ ");/* ������ ������� */
			pw.println("word-wrap:break-word;");
			pw.println("margin: 0 0 20px;"); /* �������� �������� */
			pw.println("text-align: left;");
			pw.println("padding: 5px;"); /* ���� ������ ������ */
			pw.println("border: 2px solid #AAAAAA;");
			pw.println("background: #ffffff;");
			pw.println("}");
			pw.println("#td1{ ");/* ������ ������� */
			pw.println("width: 15%;");
			pw.println("word-wrap:break-word;");
			pw.println("margin: 0 0 20px;"); /* �������� �������� */
			pw.println("text-align: left;");
			pw.println("padding: 5px;"); /* ���� ������ ������ */
			pw.println("border: 2px solid #AAAAAA;");
			pw.println("background: #C9DEBD;");
			pw.println("}");
			pw.println("textarea {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 2px solid #AAAAAA;");
			pw.println("background: #f1f1f1;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("margin: 12px 0 5px;");
			pw.println("padding: 12px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #567348;");
			pw.println("border: 0;");
			pw.println("margin: 10px 8px 0 0;");
			pw.println("padding: 12px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("#footer { ");/* ������ ���� */
			pw.println("background: #453B78; ");
			pw.println("padding: 5px; ");
			pw.println("color: #FFFFFF; ");
			pw.println("clear: left; ");
			pw.println("}");
			pw.println("</style>");
			break;

		case "updateArt":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#update-page {");
			pw.println("width: 600px;");
			pw.println("padding: 8% 0 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("height: 60%;");
			pw.println("margin: 0 auto;");
			pw.println("padding: 15px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#register-form {");
			pw.println("float: right;");
			pw.println("width: 50%;");
			pw.println("text-align: left;");
			pw.println("}");
			pw.println("#content {");
			pw.println("float: left;");
			pw.println("width: 40%;");
			pw.println("padding: 10px;");
			pw.println("}");
			pw.println("input {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 0;");
			pw.println("background: #f2f2f2;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("padding: 12px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 45%;");
			pw.println("border: 0;");
			pw.println("margin: 25px 10px 0 0;");
			pw.println("padding: 12px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;

		case "deleteArt":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#delete-page {");
			pw.println("width: 500px;"); /* ����� ������ */
			pw.println("padding: 8% 0 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("height: 50%;");
			pw.println("margin: 0 auto;");
			pw.println("padding: 15px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#delete-form {");
			pw.println("position: relative;");
			pw.println("top: 30%;");
			pw.println("z-index: 2;");
			pw.println("background: #F3FFDB;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("float: right;");
			pw.println("width: 55%;");
			pw.println("text-align: center;");
			pw.println("}");
			pw.println("#content {");
			pw.println("position: relative;");
			pw.println("top: 10%;");
			pw.println("z-index: 2;");
			pw.println("float: left;");
			pw.println("width: 35%;");
			pw.println("padding: 10px;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 42%;");
			pw.println("border: 0;");
			pw.println("margin: 8px;");
			pw.println("padding: 12px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;

		case "addArt":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#login-page {");
			pw.println("width: 360px;"); /* ����� ������ */
			pw.println("padding: 8% 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("margin: 0 auto;");
			pw.println("padding: 45px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("input {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 0;");
			pw.println("background: #f2f2f2;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("margin: 0 0 15px;");
			pw.println("padding: 15px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 45%;");
			pw.println("border: 0;");
			pw.println("padding: 15px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;

		case "addCategory":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#addCat-page {");
			pw.println("width: 360px;"); /* ����� ������ */
			pw.println("padding: 13% 0 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("margin: 0 auto 100px;");
			pw.println("padding: 40px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("input {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 0;");
			pw.println("background: #f2f2f2;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("margin: 0 0 15px;");
			pw.println("padding: 15px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 49%;");
			pw.println("border: 0;");
			pw.println("padding: 10px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;

		case "login":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#login-page {");
			pw.println("width: 360px;"); /* ����� ������ */
			pw.println("padding: 8% 0 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("padding: 45px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("input {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 0;");
			pw.println("background: #f2f2f2;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("margin: 0 0 15px;");
			pw.println("padding: 15px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("padding: 15px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("#link {");
			pw.println("font-family: sans-serif;");
			pw.println("background: #ffffff;");
			pw.println("border: 0;");
			pw.println("padding: 15px;");
			pw.println("font-size: 16px;");
			pw.println("font-color: #494154;");
			pw.println("font-style: italic;");
			pw.println("font-weight: 600;");
			pw.println("text-decoration: underline;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;

		case "registration":
			pw.println("<style>");
			pw.println("body {");
			pw.println("font: 11pt Arial, Helvetica, sans-serif;"); /* ����� ������ */
			pw.println("margin: 0;"); /* ������� �� �������� */
			pw.println("background: -webkit-linear-gradient(right, #76b852, #8DC26F);");
			pw.println("}");
			pw.println("#registration-page {");
			pw.println("width: 360px;"); /* ����� ������ */
			pw.println("padding: 8% 0 0;"); /* ������� �� �������� */
			pw.println("margin: auto;");
			pw.println("}");
			pw.println("#form {");
			pw.println("position: relative;");
			pw.println("z-index: 1;");
			pw.println("background: #FFFFFF;");
			pw.println("padding: 50px;");
			pw.println("text-align: center;");
			pw.println("box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.2), 0 5px 5px 0 rgba(0, 0, 0, 0.24);");
			pw.println("}");
			pw.println("input {");
			pw.println("font-family: sans-serif;");
			pw.println("outline: 0;");
			pw.println("background: #f2f2f2;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("margin: 0 0 12px;");
			pw.println("padding: 12px;");
			pw.println("box-sizing: border-box;");
			pw.println("font-size: 14px;");
			pw.println("}");
			pw.println("#button {");
			pw.println("font-family: sans-serif;");
			pw.println("text-transform: uppercase;");
			pw.println("outline: 0;");
			pw.println("background: #4CAF50;");
			pw.println("width: 100%;");
			pw.println("border: 0;");
			pw.println("padding: 12px;");
			pw.println("color: #FFFFFF;");
			pw.println("font-size: 14px;");
			pw.println("-webkit-transition: all 0.3 ease;");
			pw.println("transition: all 0.3 ease;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("#link {");
			pw.println("font-family: sans-serif;");
			pw.println("background: #ffffff;");
			pw.println("border: 0;");
			pw.println("padding: 12px;");
			pw.println("width: 40%;");
			pw.println("font-size: 16px;");
			pw.println("font-color: #494154;");
			pw.println("font-style: italic;");
			pw.println("font-weight: 600;");
			pw.println("text-decoration: underline;");
			pw.println("cursor: pointer;");
			pw.println("}");
			pw.println("</style>");
			break;
		}
	}
}
