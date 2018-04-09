package com.KillSystem.domain;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * UserBean
 * 
 * 2018年4月5日
 *
 */
public class User {

	private String tel_num;
	private String username;
	private String passwd;
	private String register_date;
	public String getTel_num() {
		return tel_num;
	}
	public void setTel_num(String tel_num) {
		this.tel_num = tel_num;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRegister_date() {
		return register_date;
	}
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	
}
