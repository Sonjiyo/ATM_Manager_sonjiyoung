package ATM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Util {
	Scanner sc;
	final String CUR_PATH = System.getProperty("user.dir") + "\\src\\ATM\\";
	
	Util() {
		sc = new Scanner(System.in);
	}
	
	int getValue(String msg, int start, int end, String errMsg) {
		int sel = 0;
		while(true) {
			System.out.print(msg);
			try {
				sel = sc.nextInt();
				if(sel<start || sel>end) {
					System.out.println(errMsg);
					continue;
				}
				return sel;
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("입력 오류");
			}
		}
	}
	
	String getValue(String msg) {
		System.out.print(msg);
		String input = sc.next();
		return input;
	}
	
	void saveData(AccountDAO accDAO, ClientDAO cliDAO) {
		String accData = accDAO.saveAccountData();
		String cliData = cliDAO.saveClinetData();
		
		savaAccountData("account.txt",accData);
		savaAccountData("client.txt",cliData);
		System.out.println("[저장 성공]");
	}
	
	void savaAccountData(String fileName, String accData) {
		File file = new File(CUR_PATH+fileName);
		
		try(FileWriter fw = new FileWriter(file)){
			fw.write(accData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void savaClientData(String fileName, String cliData) {
		File file = new File(CUR_PATH+fileName);
		
		try(FileWriter fw = new FileWriter(file)){
			fw.write(cliData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String loadData(String fileName) {
		File file = new File(CUR_PATH+fileName);
		
		String data = "";
		try(FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);){
			String str = "";
			while(true) {
				str = br.readLine();
				if(str==null) break;
				data += str + "\n";
			}
		} catch (FileNotFoundException e) {
			System.out.println("파일이 없습니다.");
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		return data;
	}
	
	void loadFromFile(AccountDAO accDAO, ClientDAO cliDAO) {
		String accData = loadData("account.txt");
		String cliData = loadData("client.txt");
		
		accDAO.loadAccountData(accData);
		cliDAO.loadClientData(cliData);
	}

}
