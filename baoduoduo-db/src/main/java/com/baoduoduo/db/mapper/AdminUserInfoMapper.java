package com.baoduoduo.db.mapper;

import com.baoduoduo.db.model.AdminUserInfo;
import tk.mybatis.mapper.common.Mapper;

public interface AdminUserInfoMapper extends Mapper<AdminUserInfo> {
	public Integer insertReturnPrimaryKey(AdminUserInfo vo);
}