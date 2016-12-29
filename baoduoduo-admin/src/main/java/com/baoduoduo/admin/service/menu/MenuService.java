package com.baoduoduo.admin.service.menu;

import java.util.List;

import com.baoduoduo.db.model.MenuVO;



public interface MenuService {
	public List<MenuVO> queryMenu(Integer userId)throws Exception;
}
