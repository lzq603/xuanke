package com.lzq;

import java.io.File;

import com.lzq.entity.User;
import com.lzq.win.BrowserWin;

public class Program {

	private static User user;

	public static void main(String[] args) {
		delAllFile("C:\\getCourse");
		BrowserWin.run();
	}
	
	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		Program.user = user;
	}
	
	public static boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             flag = true;
	          }
	       }
	       return flag;
	     }
}
