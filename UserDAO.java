package dao;

import java.util.List;

import entity.Pic;
import entity.User;

public interface UserDAO {
	public List<User> findAll() throws Exception;
	public User findById(int id)throws Exception;
	public User login(String userName)throws Exception;
//	public void updata(String name,String pwd,String sex,String inFo)throws Exception;
	public void addUser(User user)throws Exception;
	public void addPic(String path,int userId)throws Exception;
	public List<Pic> findByPicId(int id)throws Exception;
//	public void updata(String serName, PrintWriter pw)throws Exception;
}
