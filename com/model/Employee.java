package com.model;

import com.annotation.MyCloumn;
import com.annotation.MyEntiy;
import com.annotation.MyId;

@MyEntiy
public class Employee {
	@MyId
	private Integer id;
	private String name;
	private Float salary;
	@MyCloumn(name = "tele_phone")
	private String telePhone;
	@MyCloumn(name = "card_id")
	private String CardId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getSalary() {
		return salary;
	}
	public void setSalary(Float salary) {
		this.salary = salary;
	}
	public String getTelePhone() {
		return telePhone;
	}
	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}
	public String getCardId() {
		return CardId;
	}
	public void setCardId(String cardId) {
		CardId = cardId;
	}
	
	
}
