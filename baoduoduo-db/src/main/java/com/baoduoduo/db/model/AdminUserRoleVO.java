package com.baoduoduo.db.model;

public class AdminUserRoleVO extends AdminUserRole{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4020422359591947139L;
	private String userName;
	private String roleName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
