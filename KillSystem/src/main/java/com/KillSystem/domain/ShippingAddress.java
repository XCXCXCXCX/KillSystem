package com.KillSystem.domain;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * ShippingAddressBean
 * 
 * 2018年4月5日
 *
 */
public class ShippingAddress {

	private int address_id;
	private String user_id;
	private String address;
	private String tel_num;
	private String name;
	public int getAddress_id() {
		return address_id;
	}
	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel_num() {
		return tel_num;
	}
	public void setTel_num(String tel_num) {
		this.tel_num = tel_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
