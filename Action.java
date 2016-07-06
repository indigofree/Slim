package web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import util.DAOFactory;
import dao.UserDAO;
import entity.Pic;
import entity.User;

public class Action extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		String gender = request.getParameter("sex");
		String phone = request.getParameter("phone");
		String ask = request.getParameter("ask");
		String number = request.getParameter("number");
		String uri = request.getRequestURI();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		String path = uri.substring(uri.lastIndexOf("/"), uri.lastIndexOf("."));
		UserDAO dao = (UserDAO) DAOFactory.getInstance("UserDAO");
		User user = new User();
		if ("/login".equals(path)) {
			try {
				user = dao.login(username);
				List<User> list = dao.findAll();
				if (user != null && user.getPwd().equals(pwd)) {
					HttpSession session = request.getSession();
					Date date = new Date();
					session.setAttribute("date", date);
					session.setAttribute("login", list);
					session.setAttribute("ulogin", user);
					request.getRequestDispatcher("userList.jsp").forward(
							request, response);
				} else if (user != null && user.getPwd() != pwd) {
					request.setAttribute("loginl", "用户名或者密码错误");
					request.getRequestDispatcher("login.jsp").forward(request,
							response);
				} else {
					request.setAttribute("loginl", "用户名不存在");
					request.getRequestDispatcher("login.jsp").forward(request,
							response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("/regist".equals(path)) {
			/*
			 * try { u = dao.login(username); System.out.println("u " + "    " +
			 * u); if (u!=null&&u.getName() != null) {
			 * System.out.println("!null"); request.setAttribute("regist",
			 * u.getUserName() + "被占用");
			 * request.getRequestDispatcher("regist.jsp").forward(request,
			 * response); } else { System.out.println("null"); User u1=new
			 * User(); u1.setName(name); u1.setPwd(password); u1.setSex(sex);
			 * u1.setUserName(username); dao.addUser(u1);
			 * response.sendRedirect("registSucees.jsp"); } } catch (Exception
			 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
			 * }else{ System.out.println("nu");
			 * request.setAttribute("yanzheng","验证码错误");
			 * request.getRequestDispatcher("regist.jsp").forward(request,
			 * response);
			 * 
			 * }
			 */
			HttpSession session = request.getSession();
			String num = (String) session.getAttribute("number");
			try {
				if (num.equals(number)) {
					if (user != null && user.getName() != null) {
						System.out.println("!null");
						request.setAttribute("regist", user.getUsername()
								+ "被占用");
						request.getRequestDispatcher("regist.jsp").forward(
								request, response);
					} else {
						user.setAge(Integer.valueOf(age));
						user.setAsk(ask);
						user.setGender(gender);
						user.setName(name);
						user.setPhone(phone);
						user.setPwd(pwd);
						user.setUsername(username);
						Date date = new Date();
						session.setAttribute("date", date);
						dao.addUser(user);
						response.sendRedirect("login.jsp");
					}

				} else {
					request.setAttribute("checkcodeErr", "验证码错误！");
					request.getRequestDispatcher("regist.jsp").forward(request,
							response);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("/userDetail".equals(path)) {
			List<Pic> pic = new ArrayList<Pic>();
			try {
				user = dao.findById(Integer.valueOf(id));
				pic = dao.findByPicId(Integer.valueOf(id));
				if (user.getId() == Integer.valueOf(id)) {
					Date date = new Date();
					HttpSession session = request.getSession();
					session.setAttribute("date", date);
					request.setAttribute("user", user);
					request.setAttribute("pic", pic);
					request.getRequestDispatcher("userDetail.jsp").forward(
							request, response);
				} else {
					request.setAttribute("user", user);
					request.setAttribute("pic", pic);
					request.getRequestDispatcher("userDetail1.jsp").forward(
							request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("/upPic".equals(path)) {
			try {
				List<FileItem> list = sfu.parseRequest(request);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).isFormField()) {
						String ss = list.get(i).getFieldName();
						String sss = list.get(i).getString("utf-8");
					} else {
						ServletContext sc = getServletContext();
						String picPath = sc.getRealPath("upload");
						String picName = list.get(i).getName();
						String pic = "pic_" + id;
						File file = new File(picPath + "\\" + pic);
						file.mkdirs();
						File file1 = new File(picPath + "\\" + pic + "\\"
								+ picName);
						dao.addPic(picName, Integer.valueOf(id));
						System.out.println(list.get(i)+"/////////"+file1);
						list.get(i).write(file1);
						response.sendRedirect("userDetail.do?id=" + id);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if("/prod".equals(path)){
			System.out.println("//////////////////////");
				String str=(String) request.getAttribute("checkma");
				HttpSession hs=request.getSession();
				String number1=(String) hs.getAttribute("number");
				if(!str.equalsIgnoreCase(number1)){
					out.print(0);
				}
		}
		out.close();
	}
}
