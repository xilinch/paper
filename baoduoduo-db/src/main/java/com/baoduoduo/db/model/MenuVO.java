package com.baoduoduo.db.model;

import java.io.Serializable;
import java.util.List;

public class MenuVO implements Serializable{
	private static final long serialVersionUID = -2686633874658492697L;
	private String url;			//菜单对应页面所对应的url
	private String icon;		//菜单的图标
	private Integer menuid;		//菜单id
	private String menuname;	//菜单名称
	private List<MenuVO> subMenus;	//子菜单 
	
	public List<MenuVO> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<MenuVO> subMenus) {
		this.subMenus = subMenus;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getMenuid() {
		return menuid;
	}
	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
}
