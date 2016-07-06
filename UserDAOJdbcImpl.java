package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBUtil1;
import dao.UserDAO;
import entity.Pic;
import entity.User;


public class UserDAOJdbcImpl implements UserDAO {
	Connection conn = null;
	public List<User> findAll() throws Exception {
		String sql="select * from f_user";
		conn=DBUtil1.openConnection();
		PreparedStatement past=conn.prepareStatement(sql);
		ResultSet rs=past.executeQuery();
		List<User> list=new ArrayList<User>();
		User u=null;
		while(rs.next()){
			u=new User();
			u.setId(rs.getInt("id"));
			u.setUsername(rs.getString("username"));
			u.setPhone(rs.getString("phone"));
			u.setName(rs.getString("name"));
			u.setPwd(rs.getString("pwd"));
			u.setAge(rs.getInt("age"));
			u.setGender(rs.getString("gender"));
			u.setAsk(rs.getString("ask"));
			list.add(u);
		}
		DBUtil1.closeConnection(conn);
		return list;
	}
	public User findById(int id) throws Exception {
		String sql="select * from f_user where id=?";
		conn=DBUtil1.openConnection();
		PreparedStatement past=conn.prepareStatement(sql);
		System.out.println("idid"+id);
		past.setInt(1, id);
		ResultSet rs=past.executeQuery();
		
		User u=new User();
		if(rs.next()){
			
			u.setId(rs.getInt("id"));
			u.setUsername(rs.getString("username"));
			u.setPhone(rs.getString("phone"));
			u.setName(rs.getString("name"));
			u.setPwd(rs.getString("pwd"));
			u.setAge(rs.getInt("age"));
			u.setGender(rs.getString("gender"));
			u.setAsk(rs.getString("ask"));
			System.out.println("u"+u);	
		}
	
		DBUtil1.closeConnection(conn);
		return u;
	}
	
	public List<Pic> findByPicId(int id) throws Exception {
		String sql="select * from f_pic where userId=?";
		conn=DBUtil1.openConnection();
		List<Pic> pic = new ArrayList<Pic>();
		PreparedStatement past=conn.prepareStatement(sql);
		past.setInt(1, id);
		ResultSet rs=past.executeQuery();
		Pic p=null;
		while(rs.next()){
			p=new Pic();
			p.setId(rs.getInt("id"));
			p.setPicName(rs.getString("picName"));
			p.setUserId(rs.getInt("userId"));
			pic.add(p);
		}
	
		DBUtil1.closeConnection(conn);
		return pic;
	}
	
	public void addUser(User user) throws Exception {
		String sql="insert into f_user(username,pwd,name,age,gender,phone,ask)values(?,?,?,?,?,?,?)";
		conn=DBUtil1.openConnection();
		PreparedStatement psta=conn.prepareStatement(sql);
		psta.setString(1,user.getUsername());
		psta.setString(2,user.getPwd());
		psta.setString(3,user.getName());
		psta.setInt(4,Integer.valueOf(user.getAge()));
		psta.setString(5,user.getGender());
		psta.setString(6,user.getPhone());
		psta.setString(7,user.getAsk());
		psta.executeUpdate();
	}
	
	public void addPic(String path,int userId) throws Exception {
		System.out.println(path+"  "+userId);
		String sql="insert into f_pic(id,picName,userId)values(f_picId.nextval,?,?)";
		conn=DBUtil1.openConnection();
		PreparedStatement psta=conn.prepareStatement(sql);
		psta.setString(1,path);
		psta.setInt(2,userId);
		psta.executeUpdate();
	}
	
	public User login(String userName) throws Exception {
		String sql = "select * from f_user " +
		"where username=?";
		System.out.println("username:"+userName);
			conn=DBUtil1.openConnection();
			PreparedStatement psta=conn.prepareStatement(sql);
			psta.setString(1,userName);
			ResultSet rs=psta.executeQuery();
			User u=null;
			if(rs.next()){
				u=new User();
				u.setId(rs.getInt("id"));
				u.setUsername(rs.getString("username"));
				u.setPhone(rs.getString("phone"));
				u.setName(rs.getString("name"));
				u.setPwd(rs.getString("pwd"));
				u.setAge(rs.getInt("age"));
				u.setGender(rs.getString("gender"));
				u.setAsk(rs.getString("ask"));
				
			}
			System.out.println("login"+"    "+u);
			return u;
		
	}
}
