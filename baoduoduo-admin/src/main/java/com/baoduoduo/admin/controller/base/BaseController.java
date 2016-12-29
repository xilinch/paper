package com.baoduoduo.admin.controller.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baoduoduo.admin.utils.Contants;
import com.baoduoduo.admin.utils.MsgCode;
import com.baoduoduo.admin.utils.StringUtils;
import com.baoduoduo.db.model.BaseVO;
import com.baoduoduo.service.base.BaseService;
import com.github.pagehelper.PageInfo;

public abstract class BaseController<T extends BaseVO> implements InitializingBean{

	@Autowired
	protected BaseService<T> service; 
	
	public BaseService<T> getService(){
		return this.service;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 获取泛型 class 对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getActualTypeClass(){
	 Class<T> entityClass = null;
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            entityClass = (Class<T>)p[0];
        }
        return entityClass;
	}
	
	@RequestMapping("queryList")
	@ResponseBody
	public Map<String,Object> queryList(Integer page,Integer  rows, String orderBy,T entity){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			if(orderBy == null || "".equals(orderBy.trim())){
				orderBy = " id desc";
			}
			PageInfo<T> pageInfo = service.queryList(page, rows, orderBy, entity);
			map.put(Contants.TOTAL, pageInfo.getTotal());
			map.put(Contants.ROWS, pageInfo.getList());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.FALSE.getCode());
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
	@RequestMapping("queryAll")
	@ResponseBody
	public List<T> queryAll(HttpServletRequest request,HttpServletResponse response){
		try{
			return service.selectAll();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping("insert")
	@ResponseBody
	public Map<String,Object> insert(T entity){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Date now = new Date();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			service.save(entity);
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
	@RequestMapping("update")
	@ResponseBody
	public Map<String,Object> update(T entity){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Date now = new Date();
			entity.setUpdateTime(now);
			service.updateNotNull(entity);
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.SAVE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.SAVE_FAILED.getCode());
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String,Object> delete(Integer id){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			service.delete(id);
			map.put(Contants.RESULT_CODE, MsgCode.DELETE_SUCCESS.getCode());
			map.put(Contants.MSG, MsgCode.DELETE_SUCCESS.getMsg());
		}catch(Exception e){
			map.put(Contants.RESULT_CODE, MsgCode.DELETE_FAILED.getCode());
			map.put(Contants.MSG, e);
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
			map.put(Contants.MSG, e);
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
			map.put(Contants.MSG, e);
		}
		return map;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

}
