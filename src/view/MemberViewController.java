package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import controller.MemberService;
import controller.MemberServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Member;

public class MemberViewController implements Initializable {
	@FXML	private Button btnCreate;
	@FXML	private Button btnUpdate;
	@FXML	private Button btnDelete;

	@FXML	private Button btnNameSearch;	//검색에 필요한 요
	@FXML	private Button btnAddressSearch;
	@FXML	private TextArea taFindResult;
	@FXML 	private TextField tfSearch;
	
	
	@FXML	private TextField tfEmail;
	@FXML	private PasswordField tfPW;
	@FXML	private TextField tfName;
	@FXML	private TextField tfMobilePhone;
	@FXML	private TextField tfBirthday;
	@FXML	private TextField tfAddress;
	
	
	@FXML 	private TableView<Member> tableViewMember;
	@FXML	private TableColumn<Member, String> columnName;
	@FXML	private TableColumn<Member, String> columnEmail;
	@FXML	private TableColumn<Member, String> columnPW;
	@FXML	private TableColumn<Member, String> columnMobilePhone;
	@FXML	private TableColumn<Member, String> columnAddress;
	@FXML	private TableColumn<Member, String> columnBirthday;
	@FXML	private TableColumn<Member, String> columnAge;
	
	// Member : model이라고도 하고 DTO, VO 라고도 함
	// 시스템 밖에 저장된 정보를 객체들간에 사용하는 정보로 변환한 자료구조 또는 객체
	private final ObservableList<Member> data = FXCollections.observableArrayList();
	// 목록 : 이중연결리스트는 아니지만 리스트의 특징과 배열 특징을 잘 혼용해 놓은 클래스 ArrayList 
	ArrayList<Member> memberList;
	MemberService memberService;
	
	public MemberViewController() {
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		memberService = new MemberServiceImpl();
		// 람다식 : java 8  함수형 언어 지원 
		
		columnName.setCellValueFactory(cvf -> cvf.getValue().nameProperty());				
		columnEmail.setCellValueFactory(cvf -> cvf.getValue().emailProperty());
	//	columnPW.setCellValueFactory(cvf -> cvf.getValue().pwProperty());	//pw가 창에 뜨지 않게 하기 위함 
		columnMobilePhone.setCellValueFactory(cvf ->cvf.getValue().mobilePhoneProperty());
		columnBirthday.setCellValueFactory(cvf ->cvf.getValue().birthdayProperty());
		columnAge.setCellValueFactory(cvf ->cvf.getValue().ageProperty());
		columnAddress.setCellValueFactory(cvf ->cvf.getValue().addressProperty());
		
		tableViewMember.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showMemberInfo(newValue));

		btnCreate.setOnMouseClicked(event -> handleCreate());		
		btnDelete.setOnMouseClicked(e -> handleDelete());		
		btnUpdate.setOnMouseClicked(event -> handleUpdate());
		
		btnAddressSearch.setOnMouseClicked(event->handleFindByAddress());
		btnNameSearch.setOnMouseClicked(event->handleFindByName());
		loadMemberTableView();
	}
	@FXML
	private void handleMessageBox() {
		this.showAlert("메시지 박스가 나타납니다.");
	}
	
	@FXML
	private void handleSearch() {
		
	}
	
	String str = ""; // 인스턴스 변수 - 객체 변수, 객체가 존재하는 동안 메모리에 존재

	
	private void showMemberInfo(Member member) {
		if (member != null) {
			tfEmail.setText(member.getEmail());
			tfPW.setText(member.getPw());
			tfName.setText(member.getName());
			tfMobilePhone.setText(member.getMobilePhone());
			tfBirthday.setText(member.getBirthday());
			tfAddress.setText(member.getAddress());
		}
		 else {
			 tfEmail.setText("");
			 tfPW.setText("");
		     tfName.setText("");
		     tfMobilePhone.setText("");
		     tfBirthday.setText("");
		     tfAddress.setText("");
		 }
	}
	
	private void loadMemberTableView() {
		memberList = memberService.readList();
		for(Member m : memberList) {
			data.add(m);
		}
		tableViewMember.setItems(data);
	}
	
	@FXML
	private void handleFindByAddress() {
		String condition = tfSearch.getText();
		taFindResult.setText("");
		if(condition.length() > 0) {
			List<Member> searched = memberService.findByAddress(condition);
			if(searched.size() > 0) {
				int no = 1;
				for(Member m : searched) {
					taFindResult.appendText(no++ + " ) " + m.getAddress() + " : " + m.getEmail() + " : " + m.getName() + " \n");
				}
			}
			else
				taFindResult.setText("검색 조건에 맞는 정보가 없습니다.");
		}
		else
			this.showAlert("검색 조건을 입력하십시요");			
	}
	@FXML
	private void handleFindByName() {
		String condition = tfSearch.getText();
		taFindResult.setText("");
		if(condition.length() > 0) {
			List<Member> searched = memberService.findByName(condition);
			if(searched.size() > 0) {
				int no = 1;
				for(Member m : searched) {
					taFindResult.appendText(no++ + " ) " +m.getName()  + " : " + m.getEmail() + " : " + m.getAddress() + " \n");
				}
			}
			else
				taFindResult.setText("검색 조건에 맞는 정보가 없습니다.");
		}
		else
			this.showAlert("검색 조건을 입력하십시요");	
	}
	@FXML 
	private void handleCreate() { // event source, listener, handler
		if(validation()) {
			Member newMember = new Member(tfEmail.getText(), tfPW.getText(), tfName.getText(), tfMobilePhone.getText(), tfBirthday.getText(), makeAge(),tfAddress.getText());
			if(memberService.findByUid(newMember)!=0) //email(=Uid)중복 체크, return값이 -1 일 때 중복
			{
			data.add(newMember);
			tableViewMember.setItems(data);
			memberService.create(newMember);
			}
			else
				showAlert("아이디 중복");
		}
		else
			showAlert("필수항목 완벽한 입력");
	}
	
	@FXML 
	private void handleUpdate() {
		if(validation()){
			Member newMember = new Member(tfEmail.getText(), tfPW.getText(), tfName.getText(), tfMobilePhone.getText(), tfBirthday.getText(), makeAge(),tfAddress.getText());
		int selectedIndex = tableViewMember.getSelectionModel().getSelectedIndex();
		if(selectedIndex != memberService.findByUid(newMember))
		{
			showAlert("이메일 수정 불가능");
		}
		else if (selectedIndex >= 0) {
				tableViewMember.getItems().set(selectedIndex, newMember);
				memberService.update(newMember);			
			} else {
				showAlert("수정할 목록을 선택하세요.");          
			}
		}
			
	}
	
	@FXML 
	private void handleDelete() {
		int selectedIndex = tableViewMember.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			memberService.delete(tableViewMember.getItems().remove(selectedIndex));		
			tfEmail.clear();
			tfPW.clear();
			tfName.clear();
			tfMobilePhone.clear();
			tfBirthday.clear();
			tfAddress.clear();
		} else {
			showAlert("오류");
        }
	}
	
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainApp.getRootStage());
        alert.setTitle("알림");
        alert.setContentText("경고 : " + message);            
        alert.showAndWait();
	}

	private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    public boolean validation() {
    	if(tfEmail.getText().length() < 0 || !tfEmail.getText().contains("@")) {
			this.showAlert("Email를 확인하십시요");
			return false;
		}
    	if(tfEmail.getText().length() > 0 && tfPW.getText().length() > 0 && tfName.getText().length() > 0 && tfBirthday.getText().length() > 0 && makeAge()!=null)
			return true;
		
		if(makeAge() == null)
			this.showAlert("생년월일 확인하십시요");

		return false;
    }
    
    public String makeAge() {
    	int year = Calendar.getInstance().get(Calendar.YEAR);	//현재 년도에 따라 계산하기 위한 변
    	int birthToInt= Integer.parseInt(tfBirthday.getText().substring(0,4));	//주민번호 앞 네 자리 
//    	String checkMilli = tfBirthday.getText().substring(6,7);	//주민번호 성별 및 1900년도 2000년도 구분하는 자리
//    	System.out.print(checkMilli);
//    	int checkAge = year-2000-birthToInt+1;	//2000년 이후일 경우 식 , 음수 체크 가능 
//    	
//    	if(checkMilli.equals("1") || checkMilli.equals("2"))	//1900년대 나이 계산
//    		return year-1900-birthToInt+1+"";
//    	else if(checkMilli.equals("3") || checkMilli.equals("4"))	{	//2000년대 나이 계산 
//    		if(checkAge-1 >= 0)
//    			return checkAge+"";
//    		else {	//현재 년도 보다 큰 숫자를 입력했을 경우 null반환 으로 에러 발생 유도 
//    			return null;
//    		}
//    	}
    	if(birthToInt < year)
    		return year-birthToInt+1+"";
		return null;
    }
}
