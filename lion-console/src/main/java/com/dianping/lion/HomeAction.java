/**
 * 
 */
package com.dianping.lion;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author liujian
 *
 */
public class HomeAction extends ActionSupport {

	public String execute() {
		System.out.println("here");
		return SUCCESS;
	}
	
}
