package ATM;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDAO {
	private ArrayList<Account> accList;
	
	public AccountDAO(){
		accList = new ArrayList<Account>();
	}
	
	private String getAccountNumber(String msg) {
		while(true) {
			String accNum = Util.getValue(msg);
			String accPattern ="^\\d{4}-\\d{4}-\\d{4}$";
			Pattern p = Pattern.compile(accPattern);
			Matcher m = p.matcher(accNum);
			
			if(m.matches()) {
				return accNum;
			} else {
				System.out.println("1111-1111-1111 형태로 계좌번호를 입력해주세요.");
				continue;
			}
		}
	}
	
	private boolean accountIsEmpty() {
		if(accList==null) {
			return true;
		}
		return false;
	}
	
	private int checkClientId(String id) {
		if(accountIsEmpty()) return 0;
		int cnt =0;
		for (int i = 0; i < accList.size(); i++) {
			if (accList.get(i).getClientId().equals(id)) {
				cnt++;
			}
		}
		return cnt;
	}
	
	public String saveAccountData() {
		String data = "";
		if(accountIsEmpty()) return data;
		
		for(Account a : accList) {
			data += "%s/%s/%d\n".formatted(a.getClientId(),a.getAccNumber(),a.getMoney());
		}
		data= data.substring(0,data.length()-1);
		return data;
	}
	
	public void loadAccountData(String data) {
		if(data.isEmpty()) return;
		String[] arr = data.split("\n");
		
		for(int i =0; i<arr.length; i++) {
			String[] temp = arr[i].split("/");
			accList.add(new Account(temp[0],temp[1],Integer.parseInt(temp[2])));
		}
		System.out.println("account.txt 로드 완료");
	}
	
	public void deleteClientData(String id) {
		if(accountIsEmpty()) return;
		
		int cnt = checkClientId(id);
		if(cnt==0) {
			System.out.println("계좌가 없습니다.");
			return;
		}
		
		for(int i=0, j=0; i<accList.size(); i++) {
			if(!accList.get(i).getClientId().equals(id)) {
				accList.remove(i);
			}
		}
	}
	
	public boolean myAccountIsEmpty(String id) {
		int cnt = checkClientId(id);
		if(cnt==0) {
			System.out.println("계좌가 없습니다.");
			return true;
		}
		return false;
	}
	
	public void printAllAcc() {
		System.out.println("=============================");
		System.out.println("아이디\t계좌\t\t금액");
		System.out.println("-----------------------------");
		for(Account a : accList) {
			System.out.print(a.getClientId()+"\t");
			System.out.println(a);
		}
		System.out.println("=============================");
	}
	
	private void myAccountDataPrint(String id) {
		System.out.println("=============================");
		System.out.println("계좌\t\t금액");
		System.out.println("-----------------------------");
		for(Account a : accList) {
			if(a.getClientId().equals(id)) {
				System.out.println(a);
			}
		}
		System.out.println("=============================");
	}
	
	public void myAccountPrint(String id) {
		if(myAccountIsEmpty(id))return;
		myAccountDataPrint(id);
	}
	
	private int checkMyAccount(String id, String accNum) {
		for(int i =0; i<accList.size(); i++) {
			if(accList.get(i).getClientId().equals(id) && accList.get(i).getAccNumber().equals(accNum)) {
				return i;
			}
		}
		System.out.println("계좌번호가 올바르지 않습니다.");
		return -1;
	}
	
	private int checkAccount(String accNum) {
		for(int i =0; i<accList.size(); i++) {
			if(accList.get(i).getAccNumber().equals(accNum)) {
				return i;
			}
		}
		return -1;
	}
	
	public void addMyAccount(String id) {
		if(checkClientId(id)==3) {
			System.out.println("더이상 계좌를 생성할 수 없습니다.");
			return;
		}
		String accNum = getAccountNumber("추가할 계좌번호를 입력해주세요 : ");

		if(accList!=null && checkAccount(accNum)!=-1) {
			System.out.println("이미 존재하는 계좌입니다.");
			return;
		}
		
		accList.add(new Account(id,accNum,0));
		System.out.println("[계좌 추가 완료]");
	}
	
	public void deleteMyAccount(String id) {
		if(myAccountIsEmpty(id))return;
		myAccountDataPrint(id);
		String accNum = getAccountNumber("삭제할 계좌를 입력해주세요 : ");
		
		int idx = checkMyAccount(id, accNum);
		if(idx==-1) return;
		
		accList.remove(idx);

		System.out.println("[삭제완료]");
	}
	
	private int moneyChange(String tag, int limit, String id, int division) {
		myAccountDataPrint(id);
		while(true) {
			String accNum = getAccountNumber(tag+"할 계좌를 입력해주세요 : ");
			
			int idx = checkMyAccount(id, accNum);
			if(idx==-1) continue;
			
			if(limit==0) limit = accList.get(idx).getMoney();
			
			int money = Util.getValue(
					tag+"할 금액을 입력해주세요 : ", 100, limit,
					"금액은 "+100+"원 이상, "+limit+"원 이하로 입력해주세요.");
			
			System.out.println("["+tag+"완료]");
			
			accList.get(idx).setMoney((money*division));
			return money;
		}
	}

	public void inputMyAccountMoney(String id) { //입금
		if(myAccountIsEmpty(id))return;
		moneyChange("입금", 1111111,id,1);
	}
	
	public void outputMyAccountMoney(String id) { //출금
		if(myAccountIsEmpty(id))return;
		moneyChange("출금", 0,id,-1);
	}
	
	public void sendMyAccountMoney(String id) { //계좌이체
		if(myAccountIsEmpty(id))return;
		myAccountDataPrint(id);
		String myAccNum = getAccountNumber("이체할 계좌를 입력해주세요 : ");

		int idx = checkMyAccount(id, myAccNum);
		if(idx==-1) return;
		
		String yoAccNum = getAccountNumber("이체받을 계좌를 입력해주세요 : ");

		if(yoAccNum.equals(myAccNum)) {
			System.out.println("같은 계좌로는 이체할 수 없습니다.");
			return;
		}
		
		int idx1 = checkAccount(yoAccNum);
		if(idx1==-1) {
			System.out.println("존재하지 않는 계좌입니다.");
			return;
		}
		
		int money = Util.getValue(
				"이체할 금액을 입력해주세요 : ", 100, accList.get(idx).getMoney(),
				"금액은 "+100+"원 이상, "+accList.get(idx).getMoney()+"원 이하로 입력해주세요.");

		accList.get(idx).setMoney(money*-1);
		accList.get(idx1).setMoney(money);
	}
	
}
