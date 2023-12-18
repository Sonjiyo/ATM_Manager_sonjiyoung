package ATM;

import java.util.ArrayList;

public class ClientDAO {
	private ArrayList<Client> cliList;
	
	public ClientDAO(){
		cliList = new ArrayList<Client>();
	}
	
	private boolean clientIsEmpty() {
		if(cliList==null) {
			System.out.println("회원이 없습니다.");
			return true;
		}
		return false;
	}
	
	private int checkClientId(String id) {
		for (int i = 0; i < cliList.size(); i++) {
			if (cliList.get(i).getId().equals(id)) return i;
		}
		return -1;
	}

	public String saveClinetData() {
		String data = "";
		if(clientIsEmpty()) return data;
		
		for(Client c : cliList) {
			data += "%d/%s/%s/%s\n".formatted(c.getClientNo(),c.getId(),c.getPw(),c.getName());
		}
		data= data.substring(0,data.length()-1);
		return data;
	}
	
	public void loadClientData(String data) {
		if(data.isEmpty()) return;
		cliList.clear();
		String[] arr = data.split("\n");
		
		for(int i =0; i<arr.length; i++) {
			String[] temp = arr[i].split("/");
			cliList.add(new Client(Integer.parseInt(temp[0]),temp[1],temp[2],temp[3]));
		}
		System.out.println("client.txt 로드 완료");
	}
	
	public void clientListPrint() {
		if(clientIsEmpty()) return;
		System.out.println("=============================");
		System.out.println("회원번호\t아이디\t비밀번호\t이름");
		System.out.println("-----------------------------");
		for(Client c : cliList) {
			System.out.println(c);
		}
		System.out.println("=============================");
	}
	
	public void modifyClientData() {
		if(clientIsEmpty()) return;
		String id = Util.getValue("아이디 입력 : ");
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return;
		}
		
		System.out.println("[1] 비밀번호");
		System.out.println("[2] 이름");
		System.out.println("[0] 돌아가기");
		
		int sel = Util.getValue("수정할 데이터 선택 : ", 0, 2,"입력 오류");
		
		if(sel==0) return;
		
		if(sel==1) {
			String pw = Util.getValue("수정할 비밀번호 입력 : ");
			if(pw.equals(cliList.get(idx).getPw())) {
				System.out.println("수정 전 비밀번호와 같습니다.");
				return;
			}
			cliList.get(idx).setPw(pw);
		} else {
			String name = Util.getValue("수정할 이름 입력 : ");
			if(name.equals(cliList.get(idx).getName())) {
				System.out.println("수정 전 이름과 같습니다.");
				return;
			}
			cliList.get(idx).setName(name);
		}
		System.out.println("[수정 완료]");
	}
	
	public void deleteManager(AccountDAO accDAO) {
		if(clientIsEmpty()) return;
		String id = Util.getValue("아이디 입력 : ");
		deleteClientData(id,accDAO);
	}
	
	public boolean deleteClient(String id,AccountDAO accDAO) {
		int idx = checkClientId(id);
		String pw = Util.getValue("비밀번호 입력 : ");
		
		if(!cliList.get(idx).getPw().equals(pw)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		deleteClientData(id,accDAO);
		return true;
	}
	
	private void deleteClientData(String id,AccountDAO accDAO) {
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return;
		}
		
		accDAO.deleteClientData(id);
		
		cliList.remove(idx);

		System.out.println("[삭제 완료]");
	}
	
	public void inputNewClient() {
		String id = Util.getValue("아이디 입력 : ");
		if(cliList!=null && checkClientId(id)!=-1) {
			System.out.println("이미 존재하는 아이디입니다.");
			return;
		}
		
		String pw = Util.getValue("비밀번호 입력 : ");
		String name = Util.getValue("이름 입력 : ");
		
		int number = cliList.size()==0 ? 1001 : cliList.get(cliList.size()-1).getClientNo()+1;

		cliList.add(new Client(number,id,pw,name));
		System.out.println("[회원가입 완료]");
	}
	
	public String loginClient() {
		String id = Util.getValue("아이디 입력 : ");
		int idx = checkClientId(id);
		if(idx==-1) {
			System.out.println("존재하지 않는 아이디입니다.");
			return "";
		}
		
		String pw = Util.getValue("비밀번호 입력 : ");
		if(!cliList.get(idx).getPw().equals(pw)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return "";
		}
		
		System.out.printf("[%s님 환영합니다.]\n",cliList.get(idx).getName());
		
		return id;
	}
}
