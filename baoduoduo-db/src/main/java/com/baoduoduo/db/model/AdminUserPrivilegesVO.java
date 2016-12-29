package com.baoduoduo.db.model;

public class AdminUserPrivilegesVO extends AdminUserPrivileges{

	private static final long serialVersionUID = -6848051147705311071L;
	private String userName;
	private String pvgName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPvgName() {
		return pvgName;
	}
	public void setPvgName(String pvgName) {
		this.pvgName = pvgName;
	}

}
