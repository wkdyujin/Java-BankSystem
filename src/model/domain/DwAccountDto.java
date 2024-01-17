package model.domain;
import lombok.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import accUtil.AccUtil;

@Data
public class DwAccountDto {
    private int id;
    private int userId;
    private String dwAccNum;
    private String pwd;
    private long balance;
    private String openDate;
	
	public void updateBalance() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccUtil.getConnection();
            String sql = "UPDATE dwAccount SET balance = ? WHERE userId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, this.getBalance());
            pstmt.setInt(2, this.getUserId());
            pstmt.executeUpdate();
        } finally {
            AccUtil.close(conn, pstmt, null);
        }
    }
	
	public void updateBalance2() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccUtil.getConnection();
            String sql = "UPDATE dwAccount SET balance = ? WHERE dwAccNum = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, this.getBalance());
            pstmt.setString(2, this.getDwAccNum());
            pstmt.executeUpdate();
        } finally {
            AccUtil.close(conn, pstmt, null);
        }
    }
	
	public static DwAccountDto getDwAccNum(String loanAccNum) throws Exception {
        return AccUtil.getDwAccNum(loanAccNum);
    }
}