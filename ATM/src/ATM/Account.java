package ATM;

//한 회원마다 계좌 3개까지 만들 수 있음
public class Account {
	private String clientId;
	private String accNumber;
	private int money;
	
	
	public Account(String clientNo, String accNumber, int money) {
		this.clientId = clientNo;
		this.accNumber = accNumber;
		this.money = money;
	}

	public String getClientId() {
		return clientId;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money += money;
	}

	@Override
	public String toString() {
		return accNumber + "  " + money + "원";
	}
}
