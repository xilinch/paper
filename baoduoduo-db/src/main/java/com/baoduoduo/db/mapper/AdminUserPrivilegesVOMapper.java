package com.baoduoduo.db.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoduoduo.db.model.AdminUserPrivilegesVO;

public interface AdminUserPrivilegesVOMapper {
	public List<AdminUserPrivilegesVO> queryAdminUserPrivileges(Integer userId);
	
	public List<AdminUserPrivilegesVO> queryAdminUserPrivilegesVOList(
			@Param("userId")Integer userId,
			@Param("start")Integer start,
			@Param("limit")Integer limit);
}
