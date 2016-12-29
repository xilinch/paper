package com.baoduoduo.service.adminUserPrivileges.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoduoduo.db.model.AdminUserPrivileges;
import com.baoduoduo.db.model.AdminUserPrivilegesVO;
import com.baoduoduo.db.mapper.AdminUserPrivilegesVOMapper;
import com.baoduoduo.service.adminUserPrivileges.AdminUserPrivilegesService;
import com.baoduoduo.service.base.impl.BaseServiceImpl;

@Service
public class AdminUserPrivilegesServiceImpl extends BaseServiceImpl<AdminUserPrivileges> implements AdminUserPrivilegesService{
	
	@Autowired
	private AdminUserPrivilegesVOMapper adminUserPrivilegesVOMapper;

	@Override
	public List<AdminUserPrivilegesVO> queryAdminUserPrivileges(Integer userId) {
		return adminUserPrivilegesVOMapper.queryAdminUserPrivileges(userId);
	}
	
	@Override
	public List<AdminUserPrivilegesVO> queryAdminUserPrivilegesVOList(Integer userId,int page,int rows) throws Exception{
		return adminUserPrivilegesVOMapper.queryAdminUserPrivilegesVOList(userId, (page - 1)*rows, rows);
	}

}
