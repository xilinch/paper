/**
 * @author zxx
 * @time 2016年11月29日上午11:37:35
 */
package com.baoduoduo.service.base;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.baoduoduo.db.model.BaseVO;
import com.github.pagehelper.PageInfo;

/**
 * @author zxx
 * @time 2016年11月29日
 *
 */
public interface BaseService <T extends BaseVO> extends InitializingBean{

    public T selectByKey(Integer id)throws Exception;

    public T save(T entity)throws Exception;
    
    public List<T> select(T entity)throws Exception;
    
    public T oneSelect(T entity)throws Exception;

    public int delete(Integer id)throws Exception;
    
    public int delete(Integer ...id)throws Exception;

    public int updateAll(T entity)throws Exception;
    
    public int selectCount(T entity)throws Exception;

    public int updateNotNull(T entity)throws Exception;

    public List<T> selectAll()throws Exception;
    
    public List<T> selectByExample(Object example)throws Exception;
    
    public int batchUpdateValid(Integer valid,Integer ...id)throws InstantiationException, IllegalAccessException;
    
    public PageInfo<T> queryList(Integer page,Integer rows,String orderBy,T entity)throws Exception;
}