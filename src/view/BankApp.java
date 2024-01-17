package view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import controller.DwAcc;
import controller.LoanAcc;
import controller.UserService;

public class BankApp {
	static int id;
	String dwAccountNum;
	
	// 로그인 시 아이디 비밀번호 체크
	static boolean checkUser(Scanner sc) throws SQLException {
		int chance = 0;
		while(chance < 3) {
			System.out.print("ID를 입력하세요\nID: ");
			String inputUserId = sc.next();
			System.out.print("비밀번호를 입력하세요\nPWD: ");
			String inputUserPwd = sc.next();
			
			id = UserService.login(inputUserId, inputUserPwd);
			if (id == -1) {
				System.err.println("ID 혹은 비밀번호를 다시 확인해 주세요.");
				chance++;
			}
			else return true;
		}
		System.err.println("비밀번호를 3회 틀리셨습니다.");
		return false; // 3번 모두 실패한 경우
	}
	
	// 송금 시 계좌 비밀번호 체크
	static boolean checkAccPwd(Scanner sc) throws Exception {
		System.out.print("입출금 계좌 비밀번호: ");
		String inputPw = sc.next();
		 if ( DwAcc.mydwpwd(id) != null && DwAcc.mydwpwd(id).equals(inputPw)) {
	         System.out.println("비밀번호가 확인되었습니다.");  
			 return true;
	        } else {
	            System.err.println("올바르지않은 비밀번호입니다.");
	            return false; 
	        }
	}
	
	// 송금
	static void remit(Scanner sc) throws Exception {
		System.out.print("입금 계좌: ");
		String depositAcc = sc.next();
		System.out.print("입금 금액: ");
		long price = sc.nextLong();
		
		long curPrice = DwAcc.remmitance(id, depositAcc, price);
		System.out.println("입금이 완료되었습니다. 현재 잔액: " + curPrice);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("우리은행 모바일 뱅킹에 접속하셨습니다.");
		Scanner sc = new Scanner(System.in);
		boolean isRun = true;
		
		if (checkUser(sc)) {
			while(isRun) { // 로그인 성공
				System.out.print("[메인] 원하는 업무를 선택해 주세요.(1. 송금 | 2. 대출 | 3. 종료)\n-> ");
				int ans = sc.nextInt();
				switch (ans) {
				case 1 -> { // 송금
					List<Integer> userIds = DwAcc.isDwAccount();
					if (userIds.contains(id)) {
						if (checkAccPwd(sc)) { // 계좌 비밀번호 입력
							remit(sc);
						}
					} else {
						System.out.println("입출금 계좌 개설을 하지 않은 회원입니다.");
					}
					break;
				}
				case 2 -> { // 대출
					boolean isRun2 = true;
					while(isRun2) {
						System.out.print("[대출] 원하는 업무를 선택해 주세요.(1. 대출 계좌 조회 | 2. 대출 상환 | 3. 종료)\n-> ");
						int ans2 = sc.nextInt();
						switch (ans2) {
						case 1 -> {
							try {
						        LoanAcc.LoanAccountInfo(id);
						    } catch (Exception e) {
						        System.out.println("로그인 계정과 대출 계좌 혹은 연결 계좌의 명의가 일치하지 않아 업무를 종료합니다.");
						    }
							break;
						}
						case 2 -> {
							LoanAcc.repayLoan(id);
							break;
						}
						default -> isRun2 = false;
						}
					}
				}
				default -> isRun = false;
				}

			}
		} else {
			System.out.println("로그인 실패");
		}
		
		System.out.println("서비스가 종료되었습니다.");
	}
}
