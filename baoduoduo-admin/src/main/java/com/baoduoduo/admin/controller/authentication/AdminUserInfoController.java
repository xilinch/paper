package com.baoduoduo.admin.controller.authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.baoduoduo.admin.model.AdminUserDetailsVO;
import com.baoduoduo.admin.utils.Contants;
import com.baoduoduo.admin.utils.MsgCode;
import com.baoduoduo.admin.utils.StringUtils;
import com.baoduoduo.admin.utils.ZxxzxjDESPasswordEncoder;
import com.baoduoduo.db.model.AdminUserInfo;
import com.baoduoduo.db.model.AdminUserInfoVO;
import com.baoduoduo.db.model.AdminUserRole;
import com.baoduoduo.service.adminUserInfo.AdminUserInfoService;
import com.baoduoduo.service.adminUserRole.AdminUserRoleService;


@Controller
@RequestMapping("/secure/adminUserInfo")
public class AdminUserInfoController {
	
	@Autowired
	private AdminUserRoleService adminUserRoleService;
	
	@Autowired
	private AdminUserInfoService service;
	
	@RequestMapping("adminUserInfo.html")
	public ModelAndView html(){
		ModelAndView mv = new ModelAndView("secure/adminUserInfo");
		return mv;
	}
	
	private Logger log = Logger.getLogger(AdminUserInfoController.class);
	
	@RequestMapping("queryAdminUserInfoVOList")
	@ResponseBody
	public Map<String,Object> queryAdminUserInfoVO(AdminUserInfo entity,Integer page,Integer rows){
		Map<String,Object> map = new HashMap<String,Object>();
		List<AdminUserInfoVO> list = null;
		try{
			long r = service.queryAdminUserInfoVOTotalCount(entity.getId(), entity.getUserName());
			if(r > 0 ){
				list = service.queryAdminUserInfoVO(entity.getId(), entity.getUserName(), page, rows);
				if(list != null && list.size() > 0){
					for(int i=0; i<list.size(); i++){
						list.get(i).setPassword("");
					}
				}
			}
			map.put(Contants.TOTAL, r);
			map.put(Contants.ROWS, list);
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.FALSE.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("insertAdminUserInfo")
	@ResponseBody
	public Map<String,Object> insertAdminUserInfo(AdminUserInfo entity,Integer roleId){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Date now = new Date();
			ZxxzxjDESPasswordEncoder des = new ZxxzxjDESPasswordEncoder();
			String encPassword = des.encodePassword(entity.getPassword(), null);
			entity.setPassword(encPassword);
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			entity.setValid(MsgCode.TRUE.getCode());
			AdminUserInfo result = service.save(entity);
			
			
			AdminUserRole adminUserRoleVO = new AdminUserRole();
			adminUserRoleVO.setRoleId(roleId);
			adminUserRoleVO.setUserId(result.getId());
			adminUserRoleVO.setValid(MsgCode.TRUE.getCode());
			adminUserRoleVO.setCreateTime(now);
			adminUserRoleVO.setUpdateTime(now);
			adminUserRoleService.save(adminUserRoleVO);
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("updateAdminUserInfo")
	@ResponseBody
	public Map<String,Object> updateAdminUserInfo(AdminUserInfo entity,Integer roleId){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Date now = new Date();
			if(entity.getPassword() != null && !"".equals(entity.getPassword().trim())){
				ZxxzxjDESPasswordEncoder des = new ZxxzxjDESPasswordEncoder();
				String encPassword = des.encodePassword(entity.getPassword(), null);
				entity.setPassword(encPassword);
			}
			entity.setUpdateTime(now);
			service.updateNotNull(entity);
			
			AdminUserRole adminUserRoleVO = new AdminUserRole();
			adminUserRoleVO.setRoleId(roleId);
			adminUserRoleVO.setUserId(entity.getId());
			adminUserRoleVO.setValid(MsgCode.TRUE.getCode());
			adminUserRoleVO.setUpdateTime(now);
			adminUserRoleService.updateNotNull(adminUserRoleVO);
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	} 
	
	@RequestMapping("updateAdminSelfInfo")
	@ResponseBody
	public Map<String,Object> updateAdminSelfInfo(AdminUserInfo entity,Integer roleId){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Date now = new Date();
			if(entity.getPassword() != null && !"".equals(entity.getPassword().trim())){
				ZxxzxjDESPasswordEncoder des = new ZxxzxjDESPasswordEncoder();
				String encPassword = des.encodePassword(entity.getPassword(), null);
				entity.setPassword(encPassword);
			}
			entity.setUpdateTime(now);
			
			Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if ( obj instanceof AdminUserDetailsVO){
				AdminUserDetailsVO user = (AdminUserDetailsVO) obj;
				entity.setId(user.getAdminUserInfo().getId());
				entity.setLoginCode(user.getAdminUserInfo().getLoginCode());
				service.updateNotNull(entity);
				
				map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
				map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
			}else{
				map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
				map.put(Contants.MSG, "未登陆用户");
			}
			
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	} 
	
	@RequestMapping("batchDelete")
	@ResponseBody
	public Map<String,Object> batchDelete(String strIds){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Integer[] ids = StringUtils.convertStringToIds(strIds);
			service.delete(ids);
			map.put(Contants.RESULT_CODE, MsgCode.DELETE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.DELETE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.DELETE_FAILED.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("batchUpdateValid")
	@ResponseBody
	public Map<String,Object> batchUpdateValid(String strIds,Integer valid){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Integer[] ids = StringUtils.convertStringToIds(strIds);
			service.batchUpdateValid(valid, ids);
			map.put(Contants.RESULT_CODE, MsgCode.UPDATE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.UPDATE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.UPDATE_FAILED.getCode());
			map.put(Contants.MSG, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping("queryAll")
	@ResponseBody
	public List<AdminUserInfo> queryAll(HttpServletRequest request,HttpServletResponse response){
		try{
			List<AdminUserInfo> list = service.selectAll();
			if(list != null && list.size() > 0){
				for(int i=0; i<list.size(); i++){
					list.get(i).setPassword("");
				}
			}
			return list;
		}catch(Exception e){
			log.error(e);
			return null;
		}
	}
	
}
