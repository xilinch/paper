package com.baoduoduo.admin.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.baoduoduo.db.model.AdminUserInfo;

public class AdminUserDetailsVO implements UserDetails{
	private static final long serialVersionUID = 732058084565018641L;
	private List<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private AdminUserInfo adminUserInfo;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.authorities;
	}
	
	@Override
	public String getPassword(){
		return this.adminUserInfo.getPassword();
	}
	
	@Override
	public String getUsername(){
		return this.adminUserInfo.getUserName();
	}
	
	@Override
	public boolean isEnabled(){
		return adminUserInfo.getValid() == 1;
	}
	
	/**
	 * 账号是否被锁
	 */
	@Override
	public boolean isAccountNonLocked(){
		return true;
	}
	
	/**
	 * 是否过期
	 */
	@Override
	public boolean isAccountNonExpired(){
		return true;
	}
	
	/**
	 * ƾ֤�Ƿ增加
	 */
	@Override
	public boolean isCredentialsNonExpired(){
		return true;
	}
	
	public AdminUserInfo getAdminUserInfo() {
		return adminUserInfo;
	}
	public void setAdminUserInfo(AdminUserInfo adminUserInfo) {
		this.adminUserInfo = adminUserInfo;
	}
	
	public AdminUserDetailsVO(AdminUserInfo adminUserInfo,List<? extends GrantedAuthority> authorities){
		super();
		this.adminUserInfo = adminUserInfo;
		this.authorities = authorities;
	}
}
