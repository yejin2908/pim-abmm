package model;

import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member { // DB의 레코드와 매핑되는 객체
	private StringProperty email; // DB의 필드와 매핑
	private StringProperty pw;
	private StringProperty name;
	private final StringProperty mobilePhone;
	private final StringProperty birthday;
	private  StringProperty age;
	private  StringProperty address;
	/*
    private final IntegerProperty zipcode;
    private ObjectProperty<LocalDate> birthday;
	*/
	public Member() {
		this(null, null, null, null,null,null,null);
	}
	
	public Member(String email, String pw, String name, String mobilePhone, String birthday,String age,String address) {
		this.email = new SimpleStringProperty(email);
		this.name = new SimpleStringProperty(name);
		this.pw = new SimpleStringProperty(pw);
		this.mobilePhone = new SimpleStringProperty(mobilePhone);
		this.birthday = new SimpleStringProperty(birthday);
		this.age = new SimpleStringProperty(age);
		this.address = new SimpleStringProperty(address);
	}
	
	public String getEmail() {
		return this.email.get();
	}
	public void setEmail(String email) {
		this.email.set(email);
	}
    public StringProperty emailProperty() {
        return email;
    }
    
    public String getPw() {
    	return this.pw.get();
    }
    public void setPw(String upw) {
    	this.pw.set(upw);
    }
    public StringProperty pwProperty() {
        return pw;
    }
    public String getName() {
    	return this.name.get();
    }
    public void setName(String name) {
    	this.name.set(name);
    }
    public StringProperty nameProperty() {
        return name;
    }
	public String getMobilePhone() {
		return this.mobilePhone.get();
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone.set(mobilePhone);
	}
    public StringProperty mobilePhoneProperty() {
        return mobilePhone;
    }
	public String getBirthday() {
		return this.birthday.get();
	}
	public void setBirthday(String birthday) {
		this.birthday.set(birthday);
	}
    public StringProperty birthdayProperty() {
        return birthday;
    }
	public String getAge() {
		return this.age.get();
	}
	public void setAge(String age) {
		this.age.set(age);
	}
    public StringProperty ageProperty() {
        return age;
    }
    
	public String getAddress() {
		return this.address.get();
	}
	public void setAddress(String address) {
		this.address.set(address);
	}
    public StringProperty addressProperty() {
        return address;
    }
}
