package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import accUtil.AccUtil;

public class UserService {
	public static int login(String userId, String userPwd) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int id = 0;
		
		try {
			conn = AccUtil.getConnection();
			pstmt = conn.prepareStatement("select * from user where userId=? AND userPwd=?");
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				id = rs.getInt("id");
				System.out.println(rs.getString("name") + "님 어서오세요.");
			} else {
				id = -1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			AccUtil.close(conn, pstmt, rs);
		}
		return id;
	}
}
