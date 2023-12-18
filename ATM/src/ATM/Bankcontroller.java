package ATM;

public class Bankcontroller {
	private final String BANKNAME = "우리은행";
	private AccountDAO accDAO;
	private ClientDAO cliDAO;
	private String log;
	
	private void init() {
		accDAO = new AccountDAO();
		cliDAO = new ClientDAO();
	}
	
	public Bankcontroller(){
		init();
		run();
	}
	
	private void run(){
		while(true) {
			mainMenuPrint();
			int sel = Util.getValue("메뉴 선택 : ", 0, 2,"입력오류");
			if(sel==0) break;
			if(sel==1) {
				managerRun();
			} else {
				clientRun();
			}
		}
	}
	private void mainMenuPrint() {
		System.out.println("==[ "+BANKNAME+" ]==");
		System.out.println("[1] 관리자");
		System.out.println("[2] 사용자");
		System.out.println("[0] 종료");
	}
	
	private void managerRun() {
		while(true) {
			System.out.println("==[ "+BANKNAME+" 관리자 ]==");
			managerMenuPrint();
			int sel = Util.getValue("메뉴 선택 : ", 0, 6,"입력오류");
			if(sel==0) return;	
			if(sel==1) {
				cliDAO.clientListPrint();
			}else if(sel==2) {
				cliDAO.modifyClientData();
			} else if(sel==3) {
				cliDAO.deleteManager(accDAO);
			} else if(sel==4) {
				Util.saveData(accDAO,cliDAO);
			} else if(sel==5) {
				Util.loadFromFile(accDAO, cliDAO);				
			} else {
				accDAO.printAllAcc();
			}
		}
	}
	private void managerMenuPrint() {
		System.out.println("[1] 회원목록");
		System.out.println("[2] 회원수정");//회원 아이디로 검색 비밀번호, 이름 수정가능
		System.out.println("[3] 회원 삭제"); //회원 아이디로 검색
		System.out.println("[4] 데이터 저장"); //acount.txt / client.txt
		System.out.println("[5] 데이터 불러오기");
		System.out.println("[6] 전체 계좌 목록");
		System.out.println("[0] 뒤로가기");
	}
	
	private void clientRun() {
		while(true) {
			System.out.println("==[ "+BANKNAME+" 사용자 ]==");
			clientMenuPrint();
			int sel = Util.getValue("메뉴 입력 : ", 0, 2,"입력오류");
			if(sel==0) return;
			if(sel==1) {
				cliDAO.inputNewClient();
			}else {
				log = cliDAO.loginClient();
				if(log.isEmpty()) continue;
				accountRun();
			}
		}
	}
	private void clientMenuPrint() {
		System.out.println("[1] 회원가입"); //회원 아이디 중복 금지
		System.out.println("[2] 로그인");
		System.out.println("[0] 뒤로가기");
	}
	
	private void accountRun() {
		while(true) {
			System.out.println("==[ "+BANKNAME+" ATM ]==");
			accountMenuPrint();
			int sel = Util.getValue("메뉴 입력 : ", 0, 7,"입력오류");
			if(sel==0) {
				log = null;
				return;
			}
			
			if(sel==1) {
				accDAO.addMyAccount(log);
			}else if(sel==2) {
				accDAO.deleteMyAccount(log);
			} else if(sel==3) {
				accDAO.inputMyAccountMoney(log);
			} else if(sel==4) {
				accDAO.outputMyAccountMoney(log);
			} else if(sel==5) {
				accDAO.sendMyAccountMoney(log);
			} else if(sel==6) {
				if(cliDAO.deleteClient(log,accDAO)){
					log = null;
					return;
				}
			} else {
				accDAO.myAccountPrint(log);
			}
		}
	}
	
	private void accountMenuPrint() {
		System.out.println("[1] 계좌추가"); //계좌 중복 금지 1111-1111-1111 형태만 가능
		System.out.println("[2] 계좌삭제"); //본인 회원 계좌만 가능
		System.out.println("[3] 입금"); //본인 계좌번호만 가능 : 100원 이상만 가능
		System.out.println("[4] 출금"); 
		System.out.println("[5] 이체"); //이체할 계좌와 이체받을 계좌가 일치할 수 없음
		System.out.println("[6] 탈퇴"); //패스워드 다시 입력 -> 탈퇴 가능
		System.out.println("[7] 마이페이지"); //내 계좌 목록(잔고) 확인
		System.out.println("[0] 로그아웃");
	}
}
