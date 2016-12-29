package com.baoduoduo.admin.service.menu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoduoduo.admin.service.menu.MenuService;
import com.baoduoduo.db.mapper.MenuMapper;
import com.baoduoduo.db.model.MenuVO;


@Service
public class MenuServiceImpl implements MenuService{
	@Autowired
	private MenuMapper menuMapper;
	
	@Override
	public List<MenuVO> queryMenu(Integer userId)throws Exception{
		List<MenuVO> list = menuMapper.queryMenu(userId);
		return list;
	}
}
