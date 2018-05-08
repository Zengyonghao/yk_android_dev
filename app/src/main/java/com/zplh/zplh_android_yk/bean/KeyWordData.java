package com.zplh.zplh_android_yk.bean;

import org.litepal.crud.DataSupport;

/**
 * 
* @Description: (搜索记录保存在数据库) 
* @author 唐飞
* @date 2016年3月3日 下午6:39:29 *
 */
public class KeyWordData extends DataSupport {
	private String keyWord;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
}
