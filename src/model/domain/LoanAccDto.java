package model.domain;

import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import accUtil.AccUtil;

@Data
public class LoanAccDto {
    private int id;
    private int userId;
    private String loanAccNum;
    private String loanPwd;
    private long loan;
    private long repayment;
    private String openDate;
    private String repaymentDate;
    private String maturityDate;

    // 대출 계좌 번호를 기반으로 LoanAccDto 인스턴스를 가져오는 정적 메서드
    public static LoanAccDto getLoanAccNum(String loanAccNum) throws Exception {
        return AccUtil.getLoanAccNum(loanAccNum);
    }
    
    
    public void updateLoan() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = AccUtil.getConnection();
            String sql = "UPDATE loanAccount SET loan = ?, repayment = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, this.getLoan());
            pstmt.setLong(2, this.getRepayment());
            pstmt.setInt(3, this.getId());
            pstmt.executeUpdate();
        } finally {
            AccUtil.close(conn, pstmt);
        }
    }
    
    public void updateLoan2() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        LocalDate currentDate = LocalDate.now();

        try {
            conn = AccUtil.getConnection();
            String sql = "UPDATE loanAccount SET repaymentDate = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            // LocalDate를 java.sql.Date로 변환
            java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
            pstmt.setDate(1, sqlDate);

            pstmt.setInt(2, this.getId());
            pstmt.executeUpdate();
        } finally {
            AccUtil.close(conn, pstmt);
        }
    }

    
    public String getMaturityDate() {
        return maturityDate;
    }
}