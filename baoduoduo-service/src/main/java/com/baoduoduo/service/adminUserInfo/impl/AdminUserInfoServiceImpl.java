package com.baoduoduo.service.adminUserInfo.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoduoduo.db.model.AdminUserInfo;
import com.baoduoduo.db.model.AdminUserInfoVO;
import com.baoduoduo.db.mapper.AdminUserInfoMapper;
import com.baoduoduo.db.mapper.AdminUserInfoVOMapper;
import com.baoduoduo.service.adminUserInfo.AdminUserInfoService;
import com.baoduoduo.service.base.impl.BaseServiceImpl;

@Service
public class AdminUserInfoServiceImpl extends BaseServiceImpl<AdminUserInfo> implements AdminUserInfoService{
	
	@Autowired
	private AdminUserInfoVOMapper adminUserInfoVOMapper;
	
	@Autowired
	private AdminUserInfoMapper service;
	
	@Override
	public List<AdminUserInfoVO> queryAdminUserInfoVO(Integer id,String userName,Integer page,Integer rows){
		return adminUserInfoVOMapper.queryAdminUserInfoVO(id, userName, (page-1)*rows, rows);
	}
	
	@Override
	public long queryAdminUserInfoVOTotalCount(Integer id,String userName){
		return adminUserInfoVOMapper.queryAdminUserInfoVOTotalCount(id, userName);
	}
	
	@Override
	public AdminUserInfo save(AdminUserInfo vo)throws Exception{
		AdminUserInfo tmp = new AdminUserInfo();
		if(vo.getLoginCode() == null || "".equals(vo.getLoginCode().trim())){
			throw new Exception("账号不能为空");
		}
		tmp.setLoginCode(vo.getLoginCode());
		long r = mapper.selectCount(tmp);
		if(r != 0 ){
			throw new Exception("该账号已经被注册");
		}
		service.insertReturnPrimaryKey(vo);
		return vo;
	}
	
	@Override
	public int updateAll(AdminUserInfo entity) throws Exception {
		AdminUserInfo tmp = new AdminUserInfo();
		if(entity.getLoginCode() == null || "".equals(entity.getLoginCode().trim())){
			throw new Exception("账号不能为空");
		}
		tmp.setLoginCode(entity.getLoginCode());
		List<AdminUserInfo> list = mapper.select(tmp);
		if(list.size() > 0){
			if(list.size() == 1 && list.get(0).getId().equals(entity.getId())){
				
			}else{
				throw new Exception("该账号已经被注册");
			}
		}
		entity.setUpdateTime(new Date());
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int updateNotNull(AdminUserInfo entity) throws Exception {
		AdminUserInfo tmp = new AdminUserInfo();
		if(entity.getLoginCode() == null || "".equals(entity.getLoginCode().trim())){
			throw new Exception("账号不能为空");
		}
		tmp.setLoginCode(entity.getLoginCode());
		List<AdminUserInfo> list = mapper.select(tmp);
		if(list.size() > 0){
			if(list.size() == 1 && list.get(0).getId().equals(entity.getId())){
				
			}else{
				throw new Exception("该账号已经被注册");
			}
		}
		entity.setUpdateTime(new Date());
		return mapper.updateByPrimaryKeySelective(entity);
	}
	
}
