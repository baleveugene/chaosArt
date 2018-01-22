package by.java.dokwork.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.java.dokwork.dao.DaoFactory;
import by.java.dokwork.dao.PersistException;
import by.java.dokwork.domain.Art;
import by.java.dokwork.domain.Artist;
import by.java.dokwork.domain.Category;
import by.java.dokwork.domain.Comment;
import by.java.dokwork.domain.User;
import by.java.dokwork.mysql.MySqlArtDao;
import by.java.dokwork.mysql.MySqlArtistDao;
import by.java.dokwork.mysql.MySqlCategoryDao;
import by.java.dokwork.mysql.MySqlCommentDao;
import by.java.dokwork.mysql.MySqlDaoFactory;
import by.java.dokwork.mysql.MySqlRoleDao;
import by.java.dokwork.mysql.MySqlUserDao;

public class ControllerServlet extends HttpServlet {
	
	MySqlArtDao artDao = null;
	MySqlArtistDao artistDao = null;
	MySqlCategoryDao categoryDao = null;
	MySqlUserDao userDao = null;
	MySqlRoleDao roleDao = null;
	MySqlCommentDao commentDao = null;

	public ControllerServlet() throws PersistException {
		
	try{
		DaoFactory factory = new MySqlDaoFactory();
		artDao = factory.getMySqlArtDao();
		artistDao = factory.getMySqlArtistDao();
		categoryDao = factory.getMySqlCategoryDao();
		roleDao = factory.getMySqlRoleDao();
		userDao = factory.getMySqlUserDao();
		commentDao = factory.getMySqlCommentDao();
	} catch (Exception e) {
		throw new PersistException("Unable to create DAO.", e);
	}	
	}
	
	public void close() throws PersistException {
		Exception e = null;
    	try{
			if(artDao!= null){
    		artDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	try{
			if(artistDao!= null){
    		artistDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	try{
			if(categoryDao!= null){
    		categoryDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	try{
			if(commentDao!= null){
    		commentDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	try{
			if(roleDao!= null){
    		roleDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	try{
			if(userDao!= null){
    		userDao.close();
			}
		} catch (Exception ex){
			e=ex;
		}
    	if (e != null) {
			throw new PersistException("Unable to close resourses.", e);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {     
		doPost(req, resp);
	}
	

	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
        PrintWriter pw = resp.getWriter();
        Enumeration<String> en = req.getParameterNames();
        try{
// �������� ��-���������        
		if(!en.hasMoreElements()||req.getParameter("Chaos")!=null){
	        getHtml("mainWithOutReg", req, resp);
	        HttpSession session = req.getSession(false);
	        if(session!=null){
	        	session.invalidate();
	        }
// ����� �����������		
	    } else if (req.getParameter("newAccount")!=null) {
        	regFormProcessing(req, resp);      		
// ����� Login
        } else if (req.getParameter("logIn")!=null) {
        	loginFormProcessing(req, resp);                       
// ��������� ����� ���������� ����� ���������       
        } else if (req.getParameter("addCategory")!=null) {
        	addNewCategory(req, resp);
// ��������� ����� ���������� ������ ����
        } else if (req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("�������� ���")) {
        	getHtml("addArt", req, resp);
        } else if (req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("������")) {
        	getHtml("mainAdmin", req, resp);
        } else if (req.getParameter("addArt")!=null&&req.getParameter("addArt").equals("�������")) {      	
        	String artName = req.getParameter("artName");
        	String artistName = req.getParameter("artistName");
        	String category = req.getParameter("category");
        	String originalURL = req.getParameter("originalURL");
        	List<Art> arts = artDao.getAll();
            ArrayList<String> names = new ArrayList<>();
            for (Art art: arts){
            	names.add(art.getName());           	 
            }
            Art art = new Art();
            if(names.contains(artName)){            	
            	pw.println("<h3>��� � ����� ��������� ��� ����������.</h3>");
            	getHtml("addArt", req, resp);
            } else {
            	//�������� � ������������� id ������ ����, ���� ������ ������ ��� ���, ������� ���
            	List<Artist> artists = artistDao.getAll();
            	HashMap<String, String> artistNames = new HashMap<>();
                for (Artist artist: artists){
                	artistNames.put(artist.getName(), artist.getId());           	 
                }
                if(!artistNames.containsKey(artistName)){            	               	
                	Artist artist = new Artist();
                	artist.setName(artistName);
                	artist = artistDao.create(artist);                	               	
                	art.setArtistId(artist.getId());
                } else {               	
                	art.setArtistId(artistNames.get(artistName));
                }
                //�������� � ������������� id ��������� ����, ���� ����� ��������� ��� ���, ������� ��
                List<Category> cats = categoryDao.getAll();
            	HashMap<String, String> catNames = new HashMap<>();
                for (Category cat: cats){
                	catNames.put(cat.getName(), cat.getId());           	 
                }
                if(!catNames.containsKey(category)){            	               	
                	Category cat = new Category();
                	cat.setName(category);
                	cat = categoryDao.create(cat);                	               	
                	art.setCategoryId(cat.getId());
                } else {               	
                	art.setCategoryId(catNames.get(category));
                }
                // ������������� ��������� ����, ���������� ��� � ��, ��������� �� ������� ��������            
                art.setName(artName);
            	art.setImage("img/content/"+artName); 
            	art.setOriginalUrl(originalURL);
            	artDao.create(art);
            	getHtml("mainAdmin", req, resp);
            }
// �������� ����������� ����        
         } else if (req.getParameter("artId")!=null) {
        	 if(req.getSession(false)==null){
        		 getHtml("artWithOutReg", req, resp); 
        	 } else if(req.getSession(false).getAttribute("roleId")!=null){
        		 if(req.getSession(false).getAttribute("roleId").equals(1)){
        			 getHtml("artAdmin", req, resp); 
        		 } else {
        			 getHtml("artUser", req, resp);
        		 }
        	 } 
// ������� �������� � ������ ���������� ���������        
         } else if (req.getParameter("categoryId")!=null) {
        	 if(req.getSession(false)==null){
        		 getHtml("mainWithOutReg", req, resp); 
        	 } else if(req.getSession(false).getAttribute("roleId")!=null){
        		 if(req.getSession(false).getAttribute("roleId").equals(1)){
        			 getHtml("mainAdmin", req, resp); 
        		 } else {
        			 getHtml("mainUser", req, resp);
        		 }
        	 }            	
// ��������� ����� ��������� ����      
        } else if (req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("��������")) {
        	getHtml("updateArt", req, resp);
        } else if (req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("������")) {
        	getHtml("mainAdmin", req, resp);
        } else if (req.getParameter("updateArt")!=null&&req.getParameter("updateArt").equals("�������� ���")) {
        	String artId = req.getParameter("aartId");
        	String artistName = req.getParameter("artistName");
        	String category = req.getParameter("category");
        	String originalUrl = req.getParameter("originalURL");
        	Art art = artDao.read(artId);
        	if(!artistName.isEmpty()){        		
        		//�������� � ������������� id ������ ����, ���� ������ ������ ��� ���, ������� ���
            	List<Artist> artists = artistDao.getAll();
            	HashMap<String, String> artistNames = new HashMap<>();
                for (Artist artist: artists){
                	artistNames.put(artist.getName(), artist.getId());           	 
                }
                if(!artistNames.containsValue(artistName)){            	               	
                	Artist artist = new Artist();
                	artist.setName(artistName);
                	artist = artistDao.create(artist);                	               	
                	art.setArtistId(artist.getId());
                } else {               	
                	art.setArtistId(artistNames.get(artistName));
                }
        	}
        	if(!category.isEmpty()){       		
        	//�������� � ������������� id ��������� ����, ���� ����� ��������� ��� ���, ������� ��
        		List<Category> cats = categoryDao.getAll();
        		HashMap<String, String> catNames = new HashMap<>();
        		for (Category cat: cats){
        			catNames.put(cat.getName(), cat.getId());           	 
        		}
        		if(!catNames.containsKey(category)){            	               	
        			Category cat = new Category();
        			cat.setName(category);
        			cat = categoryDao.create(cat);                	               	
        			art.setCategoryId(cat.getId());
        		} else {               	
            	art.setCategoryId(catNames.get(category));
        		}
        	}
        	if(!originalUrl.isEmpty()){
        		art.setOriginalUrl(originalUrl);
        	}       	       	                	        
        	artDao.update(art);
        	getHtml("mainAdmin", req, resp);        
// ��������� ����� �������� ����         	
        } else if (req.getParameter("deleteArt")!=null&&req.getParameter("deleteArt").equals("�������")) {
        	getHtml("deleteArt", req, resp);
        } else if (req.getParameter("deleteArt")!=null&&req.getParameter("deleteArt").equals("������� ���")) {
        	String artId = req.getParameter("aartId");
        	String yes = req.getParameter("yes");        	
        	if(yes!=null&&yes.equals("��")){
        		Art art = artDao.read(artId);
        		artDao.delete(art);
        		getHtml("mainAdmin", req, resp);
        	} else {
        		getHtml("mainAdmin", req, resp);
        	}      	 
// ��������� ����� ���������� ������ �����������	
        } else if (req.getParameter("newComment")!=null&&req.getParameter("newComment").equals("�������� �����������")) {      		
        		String userId = String.valueOf(req.getSession(false).getAttribute("userId"));
        		String comment = req.getParameter("comment");
        		String artId = req.getParameter("aartId");
        		Comment com = new Comment();
        		if(!comment.isEmpty()){
        			com.setText(comment);
        			com.setUserId(userId);
        			com.setArtId(artId);
        			commentDao.create(com);
        			req.setAttribute("artId", artId);
        			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/ControllerServlet?artId="+artId);
        	        requestDispatcher.forward(req, resp);
        		}      		
       		}         	      
        } catch (Exception e) {
            throw new ServletException(e);
        } 
     }  
	
// ����� �����������
		public void regFormProcessing(HttpServletRequest req, HttpServletResponse resp) throws PersistException{	
			try{
				PrintWriter pw = resp.getWriter();
				if (req.getParameter("newAccount").equals("�����������")) {
					getHtml("registration", req, resp);
	    
				} else if (req.getParameter("newAccount").equals("�������")) {        	
					String name = req.getParameter("name");  
					String surname = req.getParameter("surname");
					String login = req.getParameter("login");  
					String password = req.getParameter("password");
					User user = userDao.readByLogin(login);
					if(user.getLogin()!= null){            	
						pw.println("<h3>������������ � ����� ������� ��� ����������.</h3>");
						getHtml("registration", req, resp);
					} else {            	
						if (password.equals("Admin")){
							user.setRoleId(1);           		
							getHtml("mainAdmin", req, resp);
						} else {           		                    
							user.setRoleId(2);
							getHtml("mainUser", req, resp);
						}           	
						user.setName(name);
						user.setSurname(surname);
						user.setLogin(login);
						user.setPassword(password);	        	        
						user = userDao.create(user); 
	    	    // ������� ������ � ���������� � ��� ��������� ������������
						HttpSession session = req.getSession(true);
						session.setAttribute("userId", user.getId());
						session.setAttribute("roleId", user.getRoleId());
					} 
				}
			} catch (Exception e) {
						throw new PersistException("Unable to process Registration form.", e);
					}	
	    } 
		
	// ����� �����
			public void loginFormProcessing(HttpServletRequest req, HttpServletResponse resp) throws PersistException{	
				try{
					PrintWriter pw = resp.getWriter();
					if (req.getParameter("logIn").equals("����")) {
			        	getHtml("login", req, resp);    
			        } else if (req.getParameter("logIn").equals("�����")) {
			        	HttpSession session = req.getSession(false);
			        	session.invalidate();
			        	getHtml("mainWithOutReg", req, resp); 
			        } else if (req.getParameter("logIn").equals("�����")) {          	
			        	String login = req.getParameter("login");  
			            String password = req.getParameter("password");            
			            User user = userDao.readByLogin(login);           
			            if(user.getLogin()== null){            	
			            	pw.println("<h3>��������� ������������ ��������� ������.</h3>");           	
			            	getHtml("login", req, resp);
			            } else if(user.getPassword()!=null&&!password.equals(user.getPassword())){
			            	pw.println("<h3>��������� ������������ ��������� ������.</h3>");           	
			            	getHtml("login", req, resp);
			            } else { 
			            	String userId = String.valueOf(user.getId());
			            	HttpSession session = req.getSession(true);
			        		session.setAttribute("userId", userId);
			            	if (password.equals("Admin")){       		           		
			            		session.setAttribute("roleId", new Integer(1));
			            		getHtml("mainAdmin", req, resp);
			            	} else {           		
			            		session.setAttribute("roleId", new Integer(2));
			            		getHtml("mainUser", req, resp);
			            		}           	        	   
			            }
			        }
				} catch (Exception e) {
					throw new PersistException("Unable to process Login form.", e);
					}	
			}
			
			// ����� ���������� ����� ���������
			public void addNewCategory(HttpServletRequest req, HttpServletResponse resp) throws PersistException{	
					try{
						PrintWriter pw = resp.getWriter();
						if (req.getParameter("addCategory").equals("�������� ���������")) {
				        	getHtml("addCategory", req, resp);
				        } else if (req.getParameter("addCategory").equals("������")) {
				        	getHtml("mainAdmin", req, resp);
				        } else if (req.getParameter("addCategory").equals("�������")) {      	
				        	String categoryName = req.getParameter("category");
				        	Category category = categoryDao.readByName(categoryName);				          
				            if(category.getName()!=null){            	
				            	pw.println("<h3>������ ��������� ��� ����������.</h3>");
				            	getHtml("addCategory", req, resp);
				            } else { 
				            	category.setName(categoryName);				                 	        
				        	    categoryDao.create(category);
				            	getHtml("mainAdmin", req, resp);
				            }
				        }
							} catch (Exception e) {
								throw new PersistException("Unable to process Login form.", e);
								}	
						}
	                  
	
	public void getHtml(String page, HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
	
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("utf-8");
        PrintWriter pw = resp.getWriter();
	
        try{      		
			
        switch(page){
        
        case "mainWithOutReg":
			pw.println("<head>");        	
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
	    	getCss("main", req, resp);
    		pw.println("</head>");
    		pw.println("<body>");        	 
    		pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\">"
					+ "<img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");		
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name=\"newAccount\" value=\"�����������\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"����\">");
			pw.println("</form>");					
    		pw.println("</div>");
    		pw.println("</div>");
    		pw.println("</div>");
    		pw.println("<div id=\"sidebar\">");
    		pw.println("<h3>���������</h3>");
    		List<Category> categoryList = categoryDao.getAll();
    		for (Category c : categoryList) {
    		pw.println("<p><a id=\"link\" href=\"?categoryId=" + c.getId() + "\">" + c.getName() + "</a></p>");
    		}
    		pw.println("</div>");
    		pw.println("<div id=\"content\">");
    		List<Art> artList = artDao.getAll();
    		if(req.getParameter("categoryId")!=null){
    			String categoryId = req.getParameter("categoryId");
    			String categoryName = categoryDao.read(categoryId).getName();
    			pw.println("<h2>"+categoryName+"</h2>");
    			artList = artDao.getAllOfCat(categoryId);
    		} 
    		for (Art art : artList) {	
    		pw.println("<a id=\"img\" href=\"?artId="+art.getId()+"\"><img src= \"" 
    				+ art.getImage() + "\"height=\"300\"></a>");
    		}
    		pw.println("</div>");
    		pw.println("<div id=\"footer\">&copy; Balev</div>");
    		pw.println("</body>");	
        	break;        
        
        case "mainAdmin":
        	pw.println("<head>");        	
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
	    	getCss("main", req, resp);
    		pw.println("</head>");
    		pw.println("<body>");        	 
    		pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\">"
					+ "<img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");		
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"�����\">");
			pw.println("</form>");					
    		pw.println("</div>");
    		pw.println("</div>");
    		pw.println("</div>");
    		pw.println("<div id=\"sidebar\">");
    		pw.println("<h3>���������</h3>");
    		categoryList = categoryDao.getAll();
    		for (Category c : categoryList) {
    			pw.println("<p><a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" + c.getId() + "\">" + c.getName() + "</a></p>");
    		}
    		pw.println("<form name = \"addCategory\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
    		pw.println("<input id=\"button\" type=\"submit\" name = \"addCategory\" value=\"�������� ���������\">");
    		pw.println("</form>");
    		pw.println("</div>");
    		pw.println("<div id=\"content\">");
    		pw.println("<form name = \"addArt\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
    		pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"�������� ���\">");
    		pw.println("</form>");
    		artList = artDao.getAll();
    		if(req.getParameter("categoryId")!=null){
    			String categoryId = req.getParameter("categoryId");
    			String categoryName = categoryDao.read(categoryId).getName();
    			pw.println("<h2>"+categoryName+"</h2>");
    			artList = artDao.getAllOfCat(categoryId);
    		}
    		for (Art art : artList) {	
    		pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+art.getId()+"\"><img src= \"" 
    				+ art.getImage() + "\"height=\"300\"></a>");
    		}
    		pw.println("</div>");
    		pw.println("<div id=\"footer\">&copy; Balev</div>");
    		pw.println("</body>");	
        	break; 
        
        case "mainUser":
        	pw.println("<head>");        	
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
	    	getCss("main", req, resp);
    		pw.println("</head>");
    		pw.println("<body>");        	 
    		pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\">"
					+ "<img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");		
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"�����\">");
			pw.println("</form>");						
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");			 		
    		pw.println("<div id=\"sidebar\">");
    		pw.println("<h3>���������</h3>");
    		categoryList = categoryDao.getAll();
    		for (Category c : categoryList) {
    			pw.println("<p><a id=\"link\" href=\"/Chaos/ControllerServlet?categoryId=" 
    		+ c.getId() + "\">" + c.getName() + "</a></p>");
    		}
    		pw.println("</div>");
    		pw.println("<div id=\"content\">");     		
    		artList = artDao.getAll();
    		if(req.getParameter("categoryId")!=null){
    			String categoryId = req.getParameter("categoryId");
    			String categoryName = categoryDao.read(categoryId).getName();
    			pw.println("<h2>"+categoryName+"</h2>");
    			artList = artDao.getAllOfCat(categoryId);  			
    		}   		
    		for (Art art : artList) {	
    		pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+art.getId()+"\"><img src= \"" 
    				+ art.getImage() + "\"height=\"300\"></a>");
    		}
    		pw.println("</div>");
    		pw.println("<div id=\"footer\">&copy; Balev</div>");
    		pw.println("</body>");	
        	break; 
        
        case "artWithOutReg":
        	pw.println("<head>");
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
	    	getCss("art", req, resp);			
			pw.println("</head>");
			pw.println("<body>");        	 
			pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\">"
					+ "<img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");		
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"newAccount\" value=\"������� �������\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"logIn\" value=\"����\">");
			pw.println("</form>");						
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");			
			String id = req.getParameter("artId");
			Art art = artDao.read(id);
			String artistId = art.getArtistId();
			Artist artist = artistDao.read(artistId);   				
			pw.println("<div id=\"content\">");		
			pw.println("<div id=\"art\">");
			pw.println("<img src= \"" + art.getImage() + "\">");
			pw.println("</div>");
			pw.println("<div id=\"comments\">");
			pw.println("<h3>�����������</h3>");
			pw.println("<table border=1>");
			List<Comment> commentList = commentDao.getAll(id);
			for (Comment c : commentList) {
			User u = userDao.read(c.getUserId());
			pw.println("<tr>");
			pw.println("<td id=\"td1\">" + u.getName() + "</td>");
			pw.println("<td>" + c.getText() + "</td>");
			pw.println("</tr>");
			}
			pw.println("</table>");
			pw.println("</div>");
			pw.println("</div>");		
			pw.println("<div id=\"sidebar\">");
	    	pw.println("<h2>��� ������ �� <a id=\"link\" href=\"\">"+artist.getName()+"</a></h2>");
	    	List<Art> artList1 = artDao.getAll(artistId);
	    	for (Art a : artList1) {	
	    		pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+a.getId()+"\"><img src= \"" 
	    	+ a.getImage() + "\"height=\"120\"></a>");
	    	}
	    	pw.println("</div>");		    		
			pw.println("<div id=\"footer\">&copy; Balev</div>");
			pw.println("</body>");
			break;
        
        case "artAdmin":
        	pw.println("<head>");
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");
			getCss("art", req, resp);
			pw.println("</head>");
			pw.println("<body>");        	 
			pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\"><img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");			
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name=\"logIn\" value=\"�����\">");
			pw.println("</form>");						
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");
			id = req.getParameter("artId");
			art = artDao.read(id);
			artistId = art.getArtistId();
			artist = artistDao.read(artistId);   							
			pw.println("<div id=\"content\">");	
			pw.println("<div id=\"art\">");
			pw.println("<img src= \""+ art.getImage() +"\" height=55% >");		
			pw.println("<div id=\"buttons\">");
			pw.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input type=\"hidden\" name = \"aartId\" value=\""+id+"\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"��������\">");
			pw.println("<input id=\"button\" type=\"submit\" name = \"deleteArt\" value=\"�������\">");
			pw.println("</form>");
			pw.println("</div>");
			pw.println("</div>");
/*			pw.println("<div id=\"artInfo\">");
			pw.println("<table id=\"infoTable\">");
			pw.println("<tr><td><h4>��������</h4></td><td></td></tr>");
			pw.println("<tr><td><h4>�������� ����</h4></td><td>"+art.getName()+"</td></tr>");
			pw.println("<tr><td><h4>�����</h4></td><td>"+artist.getName()+"</td></tr>");
			pw.println("<tr><td><h4>������ �� ��������</h4></td>"
					+ "<td><a id=\"link\" href=\""+art.getOriginalUrl()+"\">�� �������� ���������!</a></td></tr>");
			pw.println("</table>");
			pw.println("</div>"); */
			pw.println("<div id=\"comments\">");
			pw.println("<h3>�����������</h3>");
			pw.println("<table>");
			commentList = commentDao.getAll(id);
			for (Comment c : commentList) {
			User u = userDao.read(c.getUserId());
			pw.println("<tr>");
			pw.println("<td id=\"td1\">" + u.getName() + "</td>");
			pw.println("<td>" + c.getText() + "</td>");
			pw.println("</tr>");
			}
			pw.println("</table>");
			pw.println("<div id=\"form\">");
        	pw.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">"); 
        	pw.println("<input type=\"hidden\" name = \"aartId\" value=\""+id+"\">");       	
        	pw.println("<textarea rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"����� �����������\"/></textarea>");       	
        	pw.println("<input id=\"button\" type=\"submit\" name = \"newComment\" value=\"�������� �����������\">");
        	pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");						
			pw.println("<div id=\"sidebar\">");
	    	pw.println("<h2>��� ������ ��  "+artist.getName()+"</h2>");
	    	artList1 = artDao.getAll(artistId);
	    	for (Art a : artList1) {	
	    		pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+a.getId()+"\"><img src= \"" 
	    	+ a.getImage() + "\"height=\"120\"></a>");
	    	}
	    	pw.println("</div>");		    		
			pw.println("<div id=\"footer\">&copy; Balev</div>");
			pw.println("</body>");
			break;
			
        case "artUser":
	        pw.println("<head>");
	    	pw.println("<title>ChaosArt</title>"); 
	    	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");			
			getCss("art", req, resp);			
			pw.println("</head>");
			pw.println("<body>");        	 
			pw.println("<div id=\"header\">");
			pw.println("<div id=\"logo\"><a href=\"/Chaos\">"
					+ "<img src='img/logo_2.png' height=\"70\" alt=\"logo\"></a></div>");			
			pw.println("<div id=\"rightsideofheader\">");
			pw.println("<div id=\"rightLinks\">");
			pw.println("<a href=\"/Chaos\">� �������</a>");
			pw.println("<a href=\"/Chaos/ControllerServlet\">�������</a>");
			pw.println("</div>");
			pw.println("<div id=\"rightTabs\">");
			pw.println("<form ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"button\" type=\"submit\" name=\"logIn\" value=\"�����\">");
			pw.println("</form>");						
			pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");
			id = req.getParameter("artId");
			art = artDao.read(id);			
			artist = artistDao.read(art.getArtistId());   				
			pw.println("<div id=\"content\">");	
			pw.println("<div id=\"art\">");
			pw.println("<img src= \""+ art.getImage() +"\" height=55% >");
			pw.println("</div>");
			pw.println("<div id=\"comments\">");
			pw.println("<h3>�����������</h3>");
			pw.println("<table>");
			commentList = commentDao.getAll(id);
			for (Comment c : commentList) {
			User u = userDao.read(c.getUserId());
			pw.println("<tr>");
			pw.println("<td id=\"td1\">" + u.getName() + "</td>");
			pw.println("<td>" + c.getText() + "</td>");
			pw.println("</tr>");
			}
			pw.println("</table>");
			pw.println("<div id=\"form\">");
        	pw.println("<form id=\"comment-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">"); 
        	pw.println("<input type=\"hidden\" name = \"aartId\" value=\""+id+"\">");
        	pw.println("<textarea rows=\"3\" cols=\"20\" name = \"comment\" placeholder=\"����� �����������\"/></textarea>");       	
        	pw.println("<input id=\"button\" type=\"submit\" name = \"newComment\" value=\"�������� �����������\">");
        	pw.println("</div>");
			pw.println("</div>");
			pw.println("</div>");		
			pw.println("<div id=\"sidebar\">");
	    	pw.println("<h2>��� ������ ��  "+artist.getName()+"</h2>");
	    	artList1 = artDao.getAll(art.getArtistId());
	    	for (Art a : artList1) {	
	    		pw.println("<a id=\"img\" href=\"/Chaos/ControllerServlet?artId="+a.getId()+"\"><img src= \"" 
	    	+ a.getImage() + "\"height=\"120\"></a>");
	    	}
	    	pw.println("</div>");		    		
			pw.println("<div id=\"footer\">&copy; Balev</div>");
			pw.println("</body>");
			break;
			
        case "registration":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
    		pw.println("<body>");
        	pw.println("<div id=\"registration-page\">");
        	pw.println("<div id=\"form\">");
        	pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">"); 
        	pw.println("<input type=\"text\" name = \"name\" placeholder=\"���\"/>");
        	pw.println("<input type=\"text\" name = \"surname\" placeholder=\"�������\"/>");
        	pw.println("<input type=\"text\" name = \"login\" placeholder=\"�����\"/>");
        	pw.println("<input type=\"password\" name = \"password\" placeholder=\"������\"/>");
        	pw.println("<input id=\"button\" type=\"submit\" name = \"newAccount\" value=\"�������\">");       	
        	pw.println("<p id=\"message\">��� ����������������? "
        			+ "<form name = \"newAccount\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
			pw.println("<input id=\"link\" type=\"submit\" name = \"logIn\" value=\"����\">");
			pw.println("</form></p>"); 
        	pw.println("</body>");
        	break;
        
        case "login":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
        	break;
        	
        case "addCategory":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
        	break;
        	
        case "addArt":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
    		pw.println("<body>");
        	pw.println("<div id=\"login-page\">");
        	pw.println("<div id=\"form\">");
        	pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">"); 
        	pw.println("<p id=\"message\">������� ��������� ����:</p>");
        	pw.println("<input type=\"text\" name = \"artName\" placeholder=\"�������� ����\"/>");
        	pw.println("<input type=\"text\" name = \"artistName\" placeholder=\"��� ���������\"/>");
        	pw.println("<input type=\"text\" name = \"category\" placeholder=\"���������\"/>");
        	pw.println("<input type=\"text\" name = \"originalURL\" placeholder=\"������ �� ��������\"/>");      
        	pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"�������\">");
        	pw.println("<input id=\"button\" type=\"submit\" name = \"addArt\" value=\"������\">");
        	pw.println("</form>"); 
        	pw.println("</body>");
        	break;
        	
        case "updateArt":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
    		pw.println("<body>");
        	pw.println("<div id=\"update-page\">");
        	pw.println("<div id=\"form\">");
        	id = req.getParameter("aartId");
			art = artDao.read(id);			
			artist = artistDao.read(art.getArtistId());
			Category category = categoryDao.read(art.getCategoryId());
			pw.println("<div id=\"content\">");		
			pw.println("<img src= \"" + art.getImage() +"\" width= \"100%\">");
			pw.println("</div>");	
        	pw.println("<form id=\"register-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");         	
        	pw.println("<input type=\"hidden\" name = \"aartId\" value=\""+id+"\"/>");
        	pw.println("<p id=\"message\">��� ������:</p>");
        	pw.println("<input type=\"text\" name = \"artistName\" placeholder=\""+artist.getName()+"\"/>");
        	pw.println("<p id=\"message\">���������:</p>");
        	pw.println("<input type=\"text\" name = \"category\" placeholder=\""+category.getName()+"\"/>");
        	pw.println("<p id=\"message\">������ �� ��������:</p>");
        	pw.println("<input type=\"text\" name = \"originalURL\" placeholder=\""+art.getOriginalUrl()+"\"/>");      
        	pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"�������� ���\">"); 
        	pw.println("<input id=\"button\" type=\"submit\" name = \"updateArt\" value=\"������\">");
        	pw.println("</form>"); 
        	pw.println("</div>"); 
        	pw.println("</div>"); 
        	pw.println("</body>");
        	break;
        	
        case "deleteArt":
        	pw.println("<head>");
        	pw.println("<title>ChaosArt</title>"); 
        	pw.println("<link rel=\"shortcut icon\" href=\"img/logo_1.jpg\" type=\"image/jpg\">");        	
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
    		pw.println("<body>");
        	pw.println("<div id=\"delete-page\">");
        	pw.println("<div id=\"form\">");
        	id = req.getParameter("aartId");
			art = artDao.read(id);			
			pw.println("<div id=\"content\">");		
			pw.println("<img src= \"" + art.getImage() + "\" width= \"100%\">");
			pw.println("</div>");
        	pw.println("<form id=\"delete-form\" ACTION=\"/Chaos/ControllerServlet\" METHOD=\"POST\">");
        	pw.println("<p id=\"message\">�� ������������� ������ ������� ���� ���?</p>");
        	pw.println("<input type=\"hidden\" name = \"aartId\" value=\""+id+"\"/>");
        	pw.println("<input type=\"hidden\" name = \"deleteArt\" value=\"������� ���\"/>");
        	pw.println("<input id=\"button\" type=\"submit\" name = \"yes\" value=\"��\"/>");
        	pw.println("<input id=\"button\" type=\"submit\" name = \"no\" value=\"���\"/>");      				
        	pw.println("</form>"); 
        	pw.println("</div>");
        	pw.println("</body>");
        	break;      
        }
        } catch (Exception e) {
            throw new ServletException(e);
        } 
        }
	
        
        public void getCss(String page, HttpServletRequest req, HttpServletResponse resp) 
    			throws ServletException, IOException{
        	   		
    		resp.setContentType("text/html;charset=utf-8");
    		req.setCharacterEncoding("utf-8");
            PrintWriter pw = resp.getWriter();
        	
        switch(page){
            
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
			pw.println("a {");	/* ����� ������ */	
			pw.println("font-family: sans-serif;");	
			pw.println("font-size: 14px;");
			pw.println("color: #ffffff;");
			pw.println("margin: 0 5px 0 0;");
			pw.println("}");
			pw.println("#link {");	/* ����� ������ �� �������� ���� */	
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
			pw.println("a {");	/* ����� ������ */	
			pw.println("font-family: sans-serif;");	
			pw.println("font-size: 14px;");
			pw.println("color: #ffffff;");
			pw.println("margin: 0 5px 0 0;");
			pw.println("}");
			pw.println("#link {");	/* ����� ������ �� �������� ���� */	
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
        }
    }     
}


