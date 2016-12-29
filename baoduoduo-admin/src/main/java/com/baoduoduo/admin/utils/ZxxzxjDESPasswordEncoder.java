package com.baoduoduo.admin.utils;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;

public class ZxxzxjDESPasswordEncoder extends BaseDigestPasswordEncoder{
	private Logger log = Logger.getLogger(ZxxzxjDESPasswordEncoder.class);
	
	/**
	 * 合并password和salt数据--->真正的PASSWORD的值=password+'{+salt+'}',如果salt=null的话，则没有'{}'部分内容
	 */
	@Override
	public String encodePassword(String rawPass , Object salt) {
		String saltedPass = mergePasswordAndSalt(rawPass,salt,false);
		try {
			DES  des = new DES();
			return des.encrypt(saltedPass);
		} catch ( Exception e) {
			throw new IllegalArgumentException("Not support algorithm [zxxzxj-Des]:" + e.getMessage());
		}
	}
	
	public String uncodePassword(String password) {
		try {
			DES des = new DES();
			return des.decrypt(password);
		} catch ( Exception e) {
			throw new IllegalArgumentException("Not support algorithm [zxxzxj-Des].uncodePassword:" + e.getMessage());
		}
	}
	
	@Override
	public boolean isPasswordValid(String encPassword , String rawPassword , Object salt ) {
		boolean boo = false;
		String password1 = "" + encPassword;
		String password2 = encodePassword(rawPassword, salt);
		if ( log.isDebugEnabled()){
			log.info("encPassword is:" + password1 + ". rawPassword is:"+password2+". input rawPassword is:"+rawPassword);
		}
		//防止时序攻击
		boo = PasswordEncodeUtils.equals(password1, password2);
		return boo;
			
	}
}
