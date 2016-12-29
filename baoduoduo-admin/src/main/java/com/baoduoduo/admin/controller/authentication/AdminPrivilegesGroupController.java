package com.baoduoduo.admin.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baoduoduo.admin.controller.base.BaseController;
import com.baoduoduo.db.model.AdminPrivilegesGroup;

@Controller
@RequestMapping("/secure/adminUserPrivilegesGroup")
public class AdminPrivilegesGroupController extends BaseController<AdminPrivilegesGroup>{
	@RequestMapping("adminPrivilegesGroup.html")
	public String adminUserPrivilegeHtml(){
		return "secure/adminPrivilegesGroup";
	}
}
