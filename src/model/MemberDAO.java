package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Member;
import model.MemberFileWriter;

public class MemberDAO {
	private ArrayList<Member> memberList = null;
	
	private File file = null;
	private MemberFileReader fr = null;
	private MemberFileWriter fw = null;
	
	public MemberDAO(File file) {	
		this.file = file;
		try {
			fr = new MemberFileReader(file);
			memberList = fr.readMember();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Member> selectAll() { 
		// 입력한 메모리 상에 존재하는 모든 멤버 정보를 가져와 출력			
		return memberList;	
	}
	
	public Member selectMember(Member member) {
		int index = -1;
		if((index = searchByID(member)) >= 0)
			return memberList.get(index);
		else
			return null;
	}
	
	public int searchByID(Member member) { 
		int ret = -1; // ret가 0 이상이면 검색 성공, -1 이면 검색 실패
		int index = 0;
		for(Member m : memberList) {
			if(m.getEmail().equals(member.getEmail())) {
				ret = index;
				return index;
			}
			index++;
		}				
		return ret;
	}
	
	public int insert(Member member) {
		int ret = -1;
		try {
			int index = searchByID(member);
			if(index < 0) { // -1이면 검색 실패, 등록 가능함
				fw = new MemberFileWriter(file);
				memberList.add(member);
				fw.saveMember(memberList);
				ret = 0;
			}else ret = -1;
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return ret;
	}
	
	public int update(Member member) {
		int ret = -1; // 0 이상이면 해당 아이디가 존재하므로 수정, -1이하이면 수정 실패		
		try {
			int index = searchByID(member);
			if(index >= 0) { // -1이면 검색 실패, 수정 불가
				fw = new MemberFileWriter(file);
				memberList.set(index, member);
				fw.saveMember(memberList);
				ret = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;
	}	
	public int delete(Member member) {		
		int ret = -1;
		try {
			int index = searchByID(member);
			if(index >=0) { // -1이면 검색 실패, 삭제불가능
				fw = new MemberFileWriter(file);
				memberList.remove(member);
				fw.saveMember(memberList);
				ret = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return ret;
	}
	
	public void printMemberList() {
		for(Member m : memberList)
			System.out.println(m.getName() + ":" + m.getEmail());
		
	}
	public List<Member> searchByAddress(String address) { 
		List<Member> searched = new ArrayList<Member>();
		for(Member m : memberList) {
			if(m.getAddress().equals(address)) {
				searched.add(m); 
			}
		}				
		return searched;
	}
	public List<Member> searchByName(String name) { 
		List<Member> searched = new ArrayList<Member>();
		for(Member m : memberList) {
			if(m.getName().equals(name)) {
				searched.add(m); 
			}
		}				
		return searched;
	}
}
