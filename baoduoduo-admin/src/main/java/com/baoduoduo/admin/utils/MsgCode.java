/**
 * @author zxx
 * @time 2016年7月29日上午9:02:15
 */
package com.baoduoduo.admin.utils;

/**
 * @author zxx
 * @time 2016年7月29日
 *
 */
public enum MsgCode {
	TRUE(1,"TRUE"),
	FALSE(0,"FALSE"),
	SUCCESS(0,"成功"),
	FAILED(-1,"失败"),
	OPERATOR_SUCCESS(0,"操作成功"),
	OPERATOR_FAILED(-1,"操作失败"),
	ADD_SUCCESS(0,"添加成功"),
	ADD_FAILED(-1,"添加失败"),
	UPDATE_SUCCESS(0,"更新成功"),
	UPDATE_FAILED(-1,"更新失败"),
	DELETE_SUCCESS(0,"删除成功"),
	DELETE_FAILED(-1,"删除失败"),
	SAVE_SUCCESS(0,"保存成功"),
	SAVE_FAILED(-1,"保存失败");
	
	MsgCode(Integer code,String msg){
		this.code = code;
		this.msg = msg;
	}
	private Integer code;
	private String msg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
