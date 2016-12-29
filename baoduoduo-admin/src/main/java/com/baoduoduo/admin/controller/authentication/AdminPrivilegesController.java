package com.baoduoduo.admin.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baoduoduo.admin.controller.base.BaseController;
import com.baoduoduo.db.model.AdminPrivileges;

@Controller
@RequestMapping("/secure/privileges")
public class AdminPrivilegesController extends BaseController<AdminPrivileges>{
	@RequestMapping("privileges.html")
	public String privilegesHtml(){
		return "secure/privileges";
	}
}
