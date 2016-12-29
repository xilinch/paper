package com.baoduoduo.service.adminPrivileges.impl;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.baoduoduo.db.model.AdminPrivileges;
import com.baoduoduo.service.adminPrivileges.AdminPrivilegesService;
import com.baoduoduo.service.base.impl.BaseServiceImpl;

@Service
public class AdminPrivilegesServiceImpl extends BaseServiceImpl<AdminPrivileges> implements AdminPrivilegesService{
	
	@Override
	public PageInfo<AdminPrivileges> queryList(Integer page,Integer rows,String orderBy,final AdminPrivileges entity) throws Exception {
		final Example example = new Example(AdminPrivileges.class);
		if(entity != null && entity.getName() != null && !"".equals(entity.getName().trim())){
			example.createCriteria().andLike("name","%" + entity.getName() +"%");
		}
		PageInfo<AdminPrivileges> pageInfo = PageHelper.startPage(page, rows, orderBy).doSelectPageInfo(new ISelect() {
			@Override
			public void doSelect() {
				mapper.selectByExample(example);
			}
		});
		return pageInfo;
	}
}
