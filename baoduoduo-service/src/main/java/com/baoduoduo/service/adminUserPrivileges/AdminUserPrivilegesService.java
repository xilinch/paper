package com.baoduoduo.service.adminUserPrivileges;

import java.util.List;

import com.baoduoduo.db.model.AdminUserPrivileges;
import com.baoduoduo.db.model.AdminUserPrivilegesVO;
import com.baoduoduo.service.base.BaseService;

public interface AdminUserPrivilegesService extends BaseService<AdminUserPrivileges>{
	public List<AdminUserPrivilegesVO> queryAdminUserPrivileges(Integer userId);
	public List<AdminUserPrivilegesVO> queryAdminUserPrivilegesVOList(Integer userId,int page,int rows) throws Exception;
}
