package ATM;

public class ClientDAO {
	Client[] cliList;
	Util util;
	
	ClientDAO(){
		util = new Util();
	}
	
	boolean clientIsEmpty() {
		if(cliList==null) {
			System.out.println("회원이 없습니다.");
			return true;
		}
		return false;
	}
	
	int checkClientId(String id) {
		for (int i = 0; i < cliList.length; i++) {
			if (cliList[i].id.equals(id)) return i;
		}
		return -1;
	}

	String saveClinetData() {
		String data = "";
		if(clientIsEmpty()) return data;
		
		for(Client c : cliList) {
			data += "%d/%s/%s/%s\n".formatted(c.clientNo,c.id,c.pw,c.name);
		}
		data= data.substring(0,data.length()-1);
		return data;
	}
	
	void loadClientData(String data) {
		if(data.isEmpty()) return;
		String[] arr = data.split("\n");
		
		cliList = new Client[arr.length];
		
		for(int i =0; i<arr.length; i++) {
			String[] temp = arr[i].split("/");
			cliList[i] = new Client(Integer.parseInt(temp[0]),temp[1],temp[2],temp[3]);
		}
		System.out.println("client.txt 로드 완료");
	}
	
	void clientListPrint() {
		if(clientIsEmpty()) return;
		System.out.println("=============================");
		System.out.println("회원번호\t아이디\t비밀번호\t이름");
		System.out.println("-----------------------------");
		for(Client c : cliList) {
			System.out.println(c);
		}
		System.out.println("=============================");
	}
	
	void modifyClientData() {
		if(clientIsEmpty()) return;
		String id = util.getValue("아이디 입력 : ");
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return;
		}
		
		System.out.println("[1] 비밀번호");
		System.out.println("[2] 이름");
		System.out.println("[0] 돌아가기");
		
		int sel = util.getValue("수정할 데이터 선택 : ", 0, 2,"입력 오류");
		
		if(sel==0) return;
		
		if(sel==1) {
			String pw = util.getValue("수정할 비밀번호 입력 : ");
			if(pw.equals(cliList[idx].pw)) {
				System.out.println("수정 전 비밀번호와 같습니다.");
				return;
			}
			cliList[idx].pw = pw;
		} else {
			String name = util.getValue("수정할 이름 입력 : ");
			if(name.equals(cliList[idx].name)) {
				System.out.println("수정 전 이름과 같습니다.");
				return;
			}
			cliList[idx].name = name;
		}
		System.out.println("[수정 완료]");
	}
	
	void deleteManager(AccountDAO accDAO) {
		if(clientIsEmpty()) return;
		String id = util.getValue("아이디 입력 : ");
		deleteClientData(id,accDAO);
	}
	
	boolean deleteClient(String id,AccountDAO accDAO) {
		int idx = checkClientId(id);
		String pw = util.getValue("비밀번호 입력 : ");
		
		if(!cliList[idx].pw.equals(pw)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		deleteClientData(id,accDAO);
		return true;
	}
	
	void deleteClientData(String id,AccountDAO accDAO) {
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return;
		}
		
		accDAO.deleteClientData(cliList[idx].id);
		
		Client[] copy = cliList;
		cliList = new Client[cliList.length-1];
		
		for(int i =0, j=0; i<copy.length; i++) {
			if(i!=idx) cliList[j++] = copy[i];
		}
		System.out.println("[삭제 완료]");
	}
	
	void inputNewClient() {
		String id = util.getValue("아이디 입력 : ");
		if(cliList!=null && checkClientId(id)!=-1) {
			System.out.println("이미 존재하는 아이디입니다.");
			return;
		}
		
		String pw = util.getValue("비밀번호 입력 : ");
		String name = util.getValue("이름 입력 : ");
		
		int cnt = cliList==null ? 0 : cliList.length;
		int number = cnt==0 ? 1001 : cliList[cliList.length-1].clientNo+1;
		
		Client[] copy = cliList;
		cliList = new Client[cnt+1];
		
		for(int i =0; i<cnt; i++) {
			cliList[i] = copy[i];
		}
		cliList[cnt] = new Client(number,id,pw,name);
		System.out.println("[회원가입 완료]");
	}
	
	String loginClient() {
		String id = util.getValue("아이디 입력 : ");
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return "";
		}
		
		String pw = util.getValue("비밀번호 입력 : ");
		if(!cliList[idx].pw.equals(pw)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return "";
		}
		
		System.out.printf("[%s님 환영합니다.]\n",cliList[idx].name);
		
		return id;
	}
}
