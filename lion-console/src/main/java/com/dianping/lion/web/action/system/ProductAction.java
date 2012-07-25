/**
 * Project: com.dianping.lion.lion-console-0.0.1
 * 
 * File Created at 2012-7-10
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.dianping.lion.web.action.system;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.dianping.lion.Constants;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Team;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ProductService;
import com.dianping.lion.service.ProjectService;
import com.dianping.lion.service.TeamService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class ProductAction extends AbstractLionAction implements ServletRequestAware{
	
	private ProductService productService;
	private UserService userService;
	private TeamService teamService;
	private ProjectService projectService;
	
	private List<Product> productList;
	private List<Team> teamList;
	private List<User> userList;
	
	private String active = Constants.PRODUCT_NAME;
	
	private Product product;
	private String selectedTeamValue, selectedUserValue;
	private int id;
	private String name;
	private int teamId;
	private String teamName;
	private int productLeaderId;
	private String productLeaderName;
	private String errorMessage = "产品已关联项目，不能删除";
	
	public String productAdd(){
		teamList = teamService.findAll();
		if(teamList != null && teamList.size() > 0) {
			selectedTeamValue = String.valueOf(teamList.get(0).getId());
		}
		userList = userService.findAll();
		if(userList != null && userList.size() > 0) {
			selectedUserValue = String.valueOf(userList.get(0).getId());
		}
		return SUCCESS;
	}
	
	public String productAddSubmit(){
		Product product = new Product();
		product.setName(name);
		product.setProductLeaderId(productLeaderId);
		product.setTeamId(teamId);
		product.setCreateTime(new Date(System.currentTimeMillis()));
		product.setModifyTime(new Date(System.currentTimeMillis()));
		productService.save(product);
		reInitiate();
		return SUCCESS;
	}
	
	public String productEdit(){
		teamList = teamService.findAll();
		userList = userService.findAll();
		product = productService.findProductByID(id);
		selectedTeamValue = String.valueOf(product.getTeamId());
		selectedUserValue = String.valueOf(product.getProductLeaderId());
		return SUCCESS;
	}
	
	public String productEditSubmit(){
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setProductLeaderId(productLeaderId);
		product.setTeamId(teamId);
		product.setCreateTime(new Date(System.currentTimeMillis()));
		product.setModifyTime(new Date(System.currentTimeMillis()));
		productService.update(product);
		reInitiate();
		return SUCCESS;
	}
	
	public String deleteProductAjax() {
		List<Product>  teamProducts =productService.findProductByTeamID(id);
		Team team = teamService.findTeamByID(id);
		if(teamProducts.size() > 0) {
			errorMessage = team.getName()+errorMessage;
			return ERROR;
		} else {
			productService.delete(id);
			reInitiate();
			return SUCCESS;
		}
	}
	
	public String productList(){
		this.active = Constants.PRODUCT_NAME;
		productList = productService.findAll();
		return SUCCESS;
	}
	
	public String teamList(){
		this.active = Constants.TEAM_NAME;
		return SUCCESS;
	}
	
	private void reInitiate() {
		active = Constants.PRODUCT_NAME;
		productList = productService.findAll();
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TeamService getTeamService() {
		return teamService;
	}

	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	
	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getProductLeaderId() {
		return productLeaderId;
	}

	public void setProductLeaderId(int productLeaderId) {
		this.productLeaderId = productLeaderId;
	}

	public String getProductLeaderName() {
		return productLeaderName;
	}

	public void setProductLeaderName(String productLeaderName) {
		this.productLeaderName = productLeaderName;
	}

	public String getSelectedTeamValue() {
		return selectedTeamValue;
	}

	public void setSelectedTeamValue(String selectedTeamValue) {
		this.selectedTeamValue = selectedTeamValue;
	}

	public String getSelectedUserValue() {
		return selectedUserValue;
	}

	public void setSelectedUserValue(String selectedUserValue) {
		this.selectedUserValue = selectedUserValue;
	}

}
