package accUtil;

import java.sql.Connection;
import java.sql.Statement;

import model.domain.DwAccountDto;
import model.domain.LoanAccDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AccUtil {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		String url = ignore.url;
		String id = ignore.id;
		String pw = ignore.pw;
		return DriverManager.getConnection(url, id, pw);
	}
	
	public static void close(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
		if(rs != null) {
			rs.close();
		}
		stmt.close();
		conn.close();
	}
	
	public static void close(Connection conn, Statement stmt) throws SQLException {
		stmt.close();
		conn.close();
	}
	
	public static LoanAccDto getLoanAccNum(String loanAccNum) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LoanAccDto loanAccDto = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM loanAccount WHERE loanAccNum = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loanAccNum);
            rs = pstmt.executeQuery();

            if (rs != null && rs.next()) {
                loanAccDto = new LoanAccDto();
                loanAccDto.setId(rs.getInt("id"));
                loanAccDto.setUserId(rs.getInt("userId"));
                loanAccDto.setLoanAccNum(rs.getString("loanAccNum"));
                loanAccDto.setLoanPwd(rs.getString("loanPwd"));
                loanAccDto.setLoan(rs.getLong("loan"));
                loanAccDto.setRepayment(rs.getLong("repayment"));
                loanAccDto.setOpenDate(rs.getString("openDate"));
                loanAccDto.setRepaymentDate(rs.getString("repaymentDate"));
                loanAccDto.setMaturityDate(rs.getString("maturityDate"));
            }
        } finally {
            // rs, pstmt, conn 모두 null 체크 후 close 호출
            if (rs != null) {
                close(conn, pstmt, rs);
            } else if (pstmt != null) {
                close(conn, pstmt);
            } else if (conn != null) {
                close(conn, pstmt);
            }
        }

        return loanAccDto;
    }



    
    public static DwAccountDto getDwAccNum(String dwAccNum) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DwAccountDto dwAccDto = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM dwAccount WHERE dwAccNum = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dwAccNum);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dwAccDto = new DwAccountDto();
                dwAccDto.setId(rs.getInt("id"));
                dwAccDto.setUserId(rs.getInt("userId"));
                dwAccDto.setDwAccNum(rs.getString("dwAccNum"));
                dwAccDto.setPwd(rs.getString("dwPwd"));
                dwAccDto.setBalance(rs.getLong("balance"));
                dwAccDto.setOpenDate(rs.getString("openDate"));
            }
        } finally {
            close(conn, pstmt, rs);
        }

        return dwAccDto;
    }
}
