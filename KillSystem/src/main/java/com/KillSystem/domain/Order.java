package com.KillSystem.domain;


/**
 * @author xcxcxcxcx
 * 
 * OrderBean
 * 
 * 2018年4月5日
 *
 */
public class Order {

	private String order_id;
	private String tel_num;
	private String address;
	private int goods_id;
	private String create_time;
	private int is_success;
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getTel_num() {
		return tel_num;
	}
	public void setTel_num(String tel_num) {
		this.tel_num = tel_num;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getIs_success() {
		return is_success;
	}
	public void setIs_success(int is_success) {
		this.is_success = is_success;
	}
	
}
