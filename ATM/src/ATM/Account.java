package ATM;

//한 회원마다 계좌 3개까지 만들 수 있음
public class Account {
	String clientId;
	String accNumber;
	int money;
	
	
	Account(String clientNo, String accNumber, int money) {
		this.clientId = clientNo;
		this.accNumber = accNumber;
		this.money = money;
	}

	@Override
	public String toString() {
		return accNumber + "  " + money + "원";
	}
}
