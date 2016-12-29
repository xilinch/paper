package com.baoduoduo.service.adminUserInfo;

import java.util.List;

import com.baoduoduo.db.model.AdminUserInfo;
import com.baoduoduo.db.model.AdminUserInfoVO;
import com.baoduoduo.service.base.BaseService;

public interface AdminUserInfoService extends BaseService<AdminUserInfo>{
	public List<AdminUserInfoVO> queryAdminUserInfoVO(Integer id,String userName,Integer page,Integer rows);
	public long queryAdminUserInfoVOTotalCount(Integer id,String userName);
}
