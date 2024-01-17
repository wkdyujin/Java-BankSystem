package controller;

import java.sql.Connection;
import java.util.Scanner;
import accUtil.AccUtil;
import model.domain.LoanAccDto;
import model.domain.DwAccountDto;

public class LoanAcc {

    public static void repayLoan(int LoginId) throws Exception {
    	System.out.println("고객 번호: " + LoginId);
        Scanner scanner = new Scanner(System.in);

        System.out.println("상환할 대출 계좌번호를 입력하세요:");
        String loanAccNum = scanner.next();

        System.out.println("대출 계좌 비밀번호를 입력하세요:");
        String loanPwd = scanner.next();

        // 대출 계좌 확인 및 비밀번호 일치 여부 검사
        LoanAccDto loanAccount = getLoanAccount(LoginId, loanAccNum, loanPwd);
        
        // 대출 상환 금액 계산
        long remainingPayment = loanAccount.getLoan() - loanAccount.getRepayment();
        if (remainingPayment != 0) {
            System.out.println("상환해야 할 금액: " + remainingPayment + "원");

            System.out.println("상환할 금액을 입력하세요:");
            long userInput = 0;
            while (true) {
                try {
                    userInput = scanner.nextLong();
                    break;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("올바른 숫자를 입력하세요.");
                    scanner.next(); // 버퍼 비우기
                }
            }

            // 예금 계좌 정보 입력
            System.out.println("출금할 예금 계좌번호를 입력하세요:");
            String dwAccNum = scanner.next();

            System.out.println("예금 계좌 비밀번호를 입력하세요:");
            String dwPwd = scanner.next();

            // 예금 계좌 확인 및 비밀번호 일치 여부 검사
            DwAccountDto dwAccount = getDwAccount(LoginId, dwAccNum, dwPwd);

            // 상환 가능 여부 확인
            if (dwAccount.getBalance() >= userInput && remainingPayment >= userInput) {
                // 상환 실행
                loanAccount.setRepayment(loanAccount.getRepayment() + userInput);
                dwAccount.setBalance(dwAccount.getBalance() - userInput);

                // LoanAccDto의 update 메서드 호출하여 데이터베이스에 변경 반영
                loanAccount.updateLoan();
                dwAccount.updateBalance();

                // 상환 결과 출력
                System.out.println("상환이 완료되었습니다. 남은 대출 금액: " + (loanAccount.getLoan() - loanAccount.getRepayment()) + "원");
                if (loanAccount.getLoan() - loanAccount.getRepayment() == 0) {
                	System.out.println("모든 대출금의 상환이 완료되었습니다. 이용해주셔서 감사합니다.");
                	loanAccount.updateLoan2();
                }
            } else {
                System.out.println("상환할 수 없는 금액이거나 예금 계좌의 잔액이 부족합니다. 다시 시도하세요.");
            }

            // 스캐너 닫기
//            scanner.close();
            System.out.println("서비스를 종료합니다.");
        } else {
        	System.out.println("상환이 완료된 대출 계좌입니다. 업무를 종료합니다.");
        }
        } 

    
    // 대출 계좌 확인 및 비밀번호 일치 여부 검사
    public static LoanAccDto getLoanAccount(int id, String loanAccNum, String loanPwd) throws Exception {
        LoanAccDto loanAccount = null;
        boolean namePasswordMatch = false;
        Scanner reinput = new Scanner(System.in);

        try {
            while (!namePasswordMatch) {
                loanAccount = LoanAccDto.getLoanAccNum(loanAccNum);

                if (loanAccount != null && loanAccount.getLoanPwd().equals(loanPwd)) {
                	namePasswordMatch = true;
                } else {
                    System.out.println("대출 계좌번호 또는 비밀번호가 잘못되었습니다. 다시 입력하세요.");
                    System.out.println("대출 계좌번호를 입력하세요:");
                    loanAccNum = reinput.next();
                    System.out.println("계좌 비밀번호를 입력하세요:");
                    loanPwd = reinput.next();
                }
                
                if (loanAccount != null && loanAccount.getUserId() == id) {
                	namePasswordMatch = true;
                } else {
//                    System.out.println("상환은 고객님과 대출 계좌의 명의가 일치해야 진행할 수 있습니다.");
//                    System.out.println("올바른 계정으로 처음부터 로그인해주시기 바랍니다.");
                    throw new Exception();
                }
            }
        } finally {
//            reinput.close();
        		
        }

        return loanAccount;
    }

    // 예금 계좌 확인 및 비밀번호 일치 여부 검사
    public static DwAccountDto getDwAccount(int id, String dwAccNum, String dwPwd) throws Exception {
    	DwAccountDto dwAccount = null;
        boolean namePasswordMatch = false;
        Scanner reinput = new Scanner(System.in);
        
        try {
	        while (!namePasswordMatch) {
	            dwAccount = AccUtil.getDwAccNum(dwAccNum);
	
	            if (dwAccount != null && dwAccount.getPwd().equals(dwPwd)) {
	            	namePasswordMatch = true;
	            } else {
	                System.out.println("예금 계좌번호 또는 비밀번호가 잘못되었습니다. 다시 입력하세요.");
	                System.out.println("출금할 예금 계좌번호를 입력하세요:");
	                dwAccNum = reinput.next();
	                System.out.println("예금 계좌 비밀번호를 입력하세요:");
	                dwPwd = reinput.next();
	            }
	            if (dwAccount != null && dwAccount.getUserId() == id) {
	            	namePasswordMatch = true;
	            } else {
	//                System.out.println("상환은 고객님과 대출 계좌의 명의가 일치해야 진행할 수 있습니다.");
	//                System.out.println("올바른 계정으로 처음부터 로그인해주시기 바랍니다.");
	                throw new Exception();
	            }
	        }
        } finally {
//        	reinput.close();
        } 
        return dwAccount;
    }
    
    public static void LoanAccountInfo(int LoginId) throws Exception {
    	
    	Scanner scanner = new Scanner(System.in);
        System.out.println("정보를 확인하실 대출 계좌의 계좌번호를 입력하세요:");
        String loanAccNum = scanner.next();

        System.out.println("대출 계좌 비밀번호를 입력하세요:");
        String loanPwd = scanner.next();
        
        LoanAccDto loanAccount = getLoanAccount(LoginId, loanAccNum, loanPwd);
        System.out.println("고객 번호: " + loanAccount.getUserId());
        System.out.println("계좌번호: " + loanAccount.getLoanAccNum());
        String sub = loanAccount.getLoanPwd();
        System.out.println("계좌 비밀번호: " + "***" + sub.substring(sub.length() - 1));
        System.out.println("대출 잔액: " + loanAccount.getLoan());
        System.out.println("상환액: " + loanAccount.getRepayment());
        System.out.println("계좌 개설일: " + loanAccount.getOpenDate());
        System.out.println("대출 만기일: " + loanAccount.getMaturityDate());
        System.out.println("상환일: " + loanAccount.getRepaymentDate());
    }
    

}