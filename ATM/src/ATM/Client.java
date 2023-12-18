package ATM;

public class Client {
	int clientNo; // 1001부터 자동 증가
	String id;
	String pw;
	String name;

	Client(int clientNo, String id, String pw, String name) {
		this.clientNo = clientNo;
		this.id = id;
		this.pw = pw;
		this.name = name;
	}

	@Override
	public String toString() {
		return clientNo + "\t" + id + "\t" + pw + "\t" + name;
	}
}
