package com.baoduoduo.db.mapper;
import java.util.List;

import com.baoduoduo.db.model.AdminUserRoleVO;
public interface AdminUserRoleVOMapper {
	public List<AdminUserRoleVO> queryAdminUserRole(Integer userId);
}
