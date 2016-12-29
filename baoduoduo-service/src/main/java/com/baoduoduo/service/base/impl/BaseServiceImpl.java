package com.baoduoduo.service.base.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baoduoduo.db.model.BaseVO;
import com.baoduoduo.service.base.BaseService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author zxx
 * @time 2016年11月29日
 *
 */
public abstract class BaseServiceImpl<T extends BaseVO> implements BaseService<T>{

	//日志类
	protected Log logger = LogFactory.getLog(this.getClass());
	
    @Autowired
    protected Mapper<T> mapper;
	

    public Mapper<T> getMapper() {
        return mapper;
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
	
	@Override
	public T selectByKey(Integer id) throws Exception {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public T save(T entity) throws Exception {
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		Integer result = mapper.insert(entity);
		if(result>0){
			this.logger.info("保存"+entity.getClass()+"--返回结果"+result);
		}
        return entity;
	}

	@Override
	public List<T> select(T entity) throws Exception {
		return mapper.select(entity);
	}

	@Override
	public int delete(Integer ...id) throws Exception {
		if(id!=null){
			int c = 0;
			for (int i = 0; i < id.length; i++) {
				c = c + mapper.deleteByPrimaryKey(id[i]);
			}
			return c;
		}else{
			throw new RuntimeException("id 不能为空");
		}
		
	}

	@Override
	public int updateAll(T entity) throws Exception {
		entity.setUpdateTime(new Date());
		return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateNotNull(T entity) throws Exception {
		entity.setUpdateTime(new Date());
		return mapper.updateByPrimaryKeySelective(entity);
	}
	@Override
	public int selectCount(T entity) throws Exception {
		return mapper.selectCount(entity);
	}

	@Override
	public List<T> selectAll() throws Exception {
		return this.mapper.selectAll();
	}



	@Override
	public List<T> selectByExample(Object example) throws Exception {
		return mapper.selectByExample(example);
	}
	


	@Override
	public int delete(Integer id) throws Exception {
		return this.mapper.deleteByPrimaryKey(id);
	}

	@Override
	public T oneSelect(T entity) throws Exception {
		return this.mapper.selectOne(entity);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.logger.info(this.getClass()+"初始化完毕...");
	}
	
	@Override
	public int batchUpdateValid(Integer valid,Integer ...id) throws InstantiationException, IllegalAccessException{
		if(id!=null){
			Date now = new Date();
			int c = 0;
			for (int i = 0; i < id.length; i++) {
				T entity = getActualTypeClass().newInstance();
				entity.setId(id[i]);
				entity.setValid(valid);
				entity.setUpdateTime(now);
				c = c + mapper.updateByPrimaryKeySelective(entity);
			}
			return c;
		}else{
			throw new RuntimeException("id 不能为空");
		}
	}
	
	public PageInfo<T> queryList(Integer page,Integer rows,String orderBy,final T entity) throws Exception {
		PageInfo<T> pageInfo = PageHelper.startPage(page, rows, orderBy).doSelectPageInfo(new ISelect() {
			
			@Override
			public void doSelect() {
				mapper.select(entity);
			}
		});
		return pageInfo;
	}
	
}
