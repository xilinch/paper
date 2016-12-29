package com.baoduoduo.db.model;

public class AdminUserInfoVO extends AdminUserInfo{
	private static final long serialVersionUID = 893603561944180138L;
	private Integer roleId;
	private String roleDesc;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	
}
