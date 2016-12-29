package com.baoduoduo.admin.controller.authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baoduoduo.admin.controller.base.BaseController;
import com.baoduoduo.admin.utils.Contants;
import com.baoduoduo.admin.utils.MsgCode;
import com.baoduoduo.admin.utils.StringUtils;
import com.baoduoduo.db.model.AdminUserPrivileges;
import com.baoduoduo.db.model.AdminUserPrivilegesVO;
import com.baoduoduo.service.adminUserPrivileges.AdminUserPrivilegesService;

@Controller
@RequestMapping("/secure/adminUserPrivilege")
public class AdminUserPrivilegesController extends BaseController<AdminUserPrivileges>{
	
	@RequestMapping("adminUserPrivilege.html")
	public String adminUserPrivilegeHtml(){
		return "secure/adminUserPrivilege";
	}
	
	@Autowired
	private AdminUserPrivilegesService service;
	
	@RequestMapping("batchInsert")
	@ResponseBody
	public Map<String,Object> batchInsert(Integer userId,String privilegeIds,Integer valid){
		Map<String,Object>map = new HashMap<String,Object>();
		try {
			Date now = new Date();
			Integer[] ids = StringUtils.convertStringToIds(privilegeIds);
			for(int i=0; i<ids.length; i++){
				AdminUserPrivileges entity = new AdminUserPrivileges();
				entity.setUserId(userId);
				entity.setPvgId(ids[i]);
				long r = service.selectCount(entity);
				entity.setValid(valid);
				entity.setUpdateTime(now);
				
				if(r == 0){
					entity.setCreateTime(now);
					service.save(entity);
				}else{
					service.updateNotNull(entity);
				}
			}
			
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
	@RequestMapping("queryAdminUserPrivilegesVOList")
	@ResponseBody
	public Map<String,Object> queryAdminUserPrivilegesVOList(Integer userId,Integer page,Integer rows){
		Map<String,Object> map = new HashMap<String,Object>();
		List<AdminUserPrivilegesVO> list = null;
		try{
			AdminUserPrivileges entity = new  AdminUserPrivileges();
			entity.setUserId(userId);
			long r = service.selectCount(entity);
			if(r > 0 ){
				list = service.queryAdminUserPrivilegesVOList(userId, page, rows);
			}
			map.put(Contants.TOTAL, r);
			map.put(Contants.ROWS, list);
		}catch(Exception e){
			e.printStackTrace();
			map.put(Contants.RESULT_CODE, MsgCode.FAILED.getCode());
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
}
