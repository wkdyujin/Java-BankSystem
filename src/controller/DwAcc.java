package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.util.Util;

import accUtil.AccUtil;
import model.domain.DwAccountDto;

public class DwAcc {
   	// (3) 고객 id가 dwAccount db에 없으면 "입출금 계좌 개설을 하지 않은 회원입니다" 출력
	// userId list만들기
	public static List<Integer> isDwAccount() throws Exception{
		List<Integer> dwIds = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int dwID = 0;
		 try {
	         conn = AccUtil.getConnection();
	         pstmt = conn.prepareStatement("select userId from dwAccount");
	//         pstmt.setInt(1, id);
	         
	         rs = pstmt.executeQuery();
	         while (rs.next()) {
	        	 dwID = rs.getInt("userId");	// dwAccount db에서 dwpwd를 불러옴 -> main에서 dwpwd 대조시키려고
	        	 dwIds.add(dwID);
	         }        
	         
	 } catch (Exception e) {
	         e.printStackTrace();
	 } finally { 
		 AccUtil.close(conn, pstmt, rs);
	 }
	 return dwIds;
	}
	
    // 2) 송금 서비스 - 고객 id를 받아서 DB와 연결 후 balance 연산
	// (1) Main에서 비밀번호 입력 -> 파라미터 값으로 받음
	public static String mydwpwd(int id) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String dwpwd = null;
		 try {
	         conn = AccUtil.getConnection();
	         pstmt = conn.prepareStatement("select dwPwd from dwAccount where userId = ?");
	         pstmt.setInt(1, id);
	         
	         rs = pstmt.executeQuery();
	         if(rs.next()) {
	         	dwpwd = rs.getString("dwPwd");	// dwAccount db에서 dwpwd를 불러옴 -> main에서 dwpwd 대조시키려고
	         }
	         
	 } catch (Exception e) {
	         e.printStackTrace();
	 } finally { 
	         AccUtil.close(conn, pstmt, rs);
	 }
	 return dwpwd;
	}
	
	// (2) 만약 비밀번호 일치하면 송금 진행 -> 파라미터 값 : 수취인계좌, 송금액
	// - 나의 계좌와 연동 : 내 잔액에서 -
	// - 수취인 계좌와 연동 : 수취인 잔액에서 +
	public static long remmitance(int id, String sendToAcc, long remitBill) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		long mybalance = 0;
		long tobalance = 0;
		
		try {
		        conn = AccUtil.getConnection();
		        pstmt = conn.prepareStatement("SELECT balance AS mybalance, "
		        		+ "(SELECT balance FROM dwAccount WHERE dwAccNum = ?) AS tobalance "
		        		+ "FROM dwAccount "
		        		+ "WHERE userId = ?");
		
		        pstmt.setString(1, sendToAcc);
		        pstmt.setInt(2, id);
		        
		        rs = pstmt.executeQuery();
		        if(rs.next()) {
		        	mybalance = rs.getInt("mybalance");	// DwAccount db에서 내 입출금 계좌의 잔액을 가져옴
		        	mybalance -= remitBill;
		        	
		        	tobalance = rs.getInt("tobalance");
		        	tobalance += remitBill;
		
		        }        
		    	
		    	// mybalance update
		    	DwAccountDto myAccount = new DwAccountDto();
		    	myAccount.setUserId(id);
		    	myAccount.setBalance(mybalance);
		    	myAccount.updateBalance();
		    	
		    	// tobalance update
		    	DwAccountDto toAccount = new DwAccountDto();
		    	toAccount.setDwAccNum(sendToAcc);
		    	toAccount.setBalance(tobalance);
		    	toAccount.updateBalance2();
		        
		} catch (Exception e) {
		        e.printStackTrace();
		} finally { 
		        AccUtil.close(conn, pstmt, rs);
		}
		return mybalance;
		}
	
	
}
