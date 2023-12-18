package ATM;

public class AccountDAO {
	Account[] accList;
	Util util;
	
	AccountDAO(){
		util = new Util();
	}
	
	String getAccountNumber(String msg) {
		while(true) {
			String accNum = util.getValue(msg);
			
			boolean check = true;
			String[] arr = accNum.split("[-]");
			if(arr.length!=3) check = false;
			for(String a : arr) {
				if(a.length()!=4) {
					check = false;
				}
				
				try {
					int num = Integer.parseInt(a);
				} catch (Exception e) {
					check = false;
				}
			}
			
			if(!check) {
				System.out.println("1111-1111-1111 형태로 계좌번호를 입력해주세요.");
				continue;
			}
			return accNum;
		}
		
	}
	
	boolean accountIsEmpty() {
		if(accList==null) {
			return true;
		}
		return false;
	}
	
	int checkClientId(String id) {
		if(accountIsEmpty()) return 0;
		int cnt =0;
		for (int i = 0; i < accList.length; i++) {
			if (accList[i].clientId.equals(id)) {
				cnt++;
			}
		}
		return cnt;
	}
	
	String saveAccountData() {
		String data = "";
		if(accountIsEmpty()) return data;
		
		for(Account a : accList) {
			data += "%s/%s/%d\n".formatted(a.clientId,a.accNumber,a.money);
		}
		data= data.substring(0,data.length()-1);
		return data;
	}
	
	void loadAccountData(String data) {
		if(data.isEmpty()) return;
		String[] arr = data.split("\n");
		
		accList = new Account[arr.length];
		
		for(int i =0; i<arr.length; i++) {
			String[] temp = arr[i].split("/");
			accList[i] = new Account(temp[0],temp[1],Integer.parseInt(temp[2]));
		}
		System.out.println("account.txt 로드 완료");
	}

	
	void deleteClientData(String id) {
		if(accountIsEmpty()) return;
		
		int cnt = checkClientId(id);
		if(cnt==0) {
			System.out.println("계좌가 없습니다.");
			return;
		}
		
		Account[] copy = accList;
		accList = new Account[accList.length-cnt];
		
		for(int i=0, j=0; i<copy.length; i++) {
			if(!copy[i].clientId.equals(id)) {
				accList[j++] = copy[i];
			}
		}
	}
	
	boolean myAccountIsEmpty(String id) {
		int cnt = checkClientId(id);
		if(cnt==0) {
			System.out.println("계좌가 없습니다.");
			return true;
		}
		return false;
	}
	
	void printAllAcc() {
		System.out.println("=============================");
		System.out.println("아이디\t계좌\t\t금액");
		System.out.println("-----------------------------");
		for(Account a : accList) {
			System.out.print(a.clientId+"\t");
			System.out.println(a);
		}
		System.out.println("=============================");
	}
	
	void myAccountDataPrint(String id) {
		System.out.println("=============================");
		System.out.println("계좌\t\t금액");
		System.out.println("-----------------------------");
		for(Account a : accList) {
			if(a.clientId.equals(id)) {
				System.out.println(a);
			}
		}
		System.out.println("=============================");
	}
	
	void myAccountPrint(String id) {
		if(myAccountIsEmpty(id))return;
		myAccountDataPrint(id);
	}
	
	int checkMyAccount(String id, String accNum) {
		for(int i =0; i<accList.length; i++) {
			if(accList[i].clientId.equals(id) && accList[i].accNumber.equals(accNum)) {
				return i;
			}
		}
		System.out.println("계좌번호가 올바르지 않습니다.");
		return -1;
	}
	
	int checkAccount(String accNum) {
		for(int i =0; i<accList.length; i++) {
			if(accList[i].accNumber.equals(accNum)) {
				return i;
			}
		}
		return -1;
	}
	
	void addMyAccount(String id) {
		if(checkClientId(id)==3) {
			System.out.println("더이상 계좌를 생성할 수 없습니다.");
			return;
		}
		String accNum = getAccountNumber("추가할 계좌번호를 입력해주세요 : ");

		if(accList!=null && checkAccount(accNum)!=-1) {
			System.out.println("이미 존재하는 계좌입니다.");
			return;
		}
		
		int cnt = accList==null ? 0 : accList.length;
		
		Account[] copy = accList;
		accList = new Account[cnt+1];
		for(int i =0; i<cnt; i++) {
			accList[i] = copy[i];
		}
		accList[cnt] = new Account(id,accNum,0);
		System.out.println("[계좌 추가 완료]");
	}
	
	void deleteMyAccount(String id) {
		if(myAccountIsEmpty(id))return;
		myAccountDataPrint(id);
		String accNum = getAccountNumber("삭제할 계좌를 입력해주세요 : ");
		
		int idx = checkMyAccount(id, accNum);
		if(idx==-1) return;
		
		Account[] copy = accList;
		accList = new Account[accList.length-1];
		
		for(int i=0, j=0; i<copy.length; i++) {
			if(i!=idx) {
				accList[j++] = copy[i];
			}
		}
		System.out.println("[삭제완료]");
	}
	
	int moneyChange(String tag, int limit, String id, int division) {
		myAccountDataPrint(id);
		while(true) {
			String accNum = getAccountNumber(tag+"할 계좌를 입력해주세요 : ");
			
			int idx = checkMyAccount(id, accNum);
			if(idx==-1) continue;
			
			if(limit==0) limit = accList[idx].money;
			
			int money = util.getValue(
					tag+"할 금액을 입력해주세요 : ", 100, limit,
					"금액은 "+100+"원 이상, "+limit+"원 이하로 입력해주세요.");
			
			System.out.println("["+tag+"완료]");
			
			accList[idx].money += money*division;
			return money;
		}
	}

	void inputMyAccountMoney(String id) { //입금
		if(myAccountIsEmpty(id))return;
		moneyChange("입금", 1111111,id,1);
	}
	
	void outputMyAccountMoney(String id) { //출금
		if(myAccountIsEmpty(id))return;
		moneyChange("출금", 0,id,-1);
	}
	
	void sendMyAccountMoney(String id) { //계좌이체
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
		
		int money = util.getValue(
				"이체할 금액을 입력해주세요 : ", 100, accList[idx].money,
				"금액은 "+100+"원 이상, "+accList[idx]+"원 이하로 입력해주세요.");

		accList[idx].money -= money;
		accList[idx1].money += money;
	}
	
}
