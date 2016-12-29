/**
 * @author zxx
 * @time 2016年11月29日上午11:11:41
 */
package com.baoduoduo.db.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alibaba.fastjson.JSON;

/**
 * @author zxx
 * @time 2016年11月29日
 *
 */
public class BaseVO implements Serializable{
	/**
	 * @author zxx
	 * @time 2016年11月29日
	 * 
	 */
	private static final long serialVersionUID = -1633311698327031642L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
	
	
	@Column(name="create_time")
	protected Date createTime;
	
	@Column(name="update_time")
	protected Date updateTime;
	
	@Column(name="valid")
	protected Integer valid;
	
	@Column(name="operator")
	protected String operator;

	

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public <T> T convertExt(Class<T> clazz){
		String json = JSON.toJSONString(this);
		T t =  JSON.parseObject(json,clazz);
		return t;
	}


	@Override
	public final boolean equals(Object obj) {
		if(obj instanceof BaseVO&&this.id!=null){
			return this.id.equals(((BaseVO)obj).getId());
		}
		return super.equals(obj);
	}
}
