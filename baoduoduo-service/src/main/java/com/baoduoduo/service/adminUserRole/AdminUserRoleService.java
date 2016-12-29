package com.baoduoduo.service.adminUserRole;

import java.util.List;

import com.baoduoduo.db.model.AdminUserRole;
import com.baoduoduo.db.model.AdminUserRoleVO;
import com.baoduoduo.service.base.BaseService;

public interface AdminUserRoleService extends BaseService<AdminUserRole>{
	public List<AdminUserRoleVO> queryAdminUserRole(Integer userId);
}
