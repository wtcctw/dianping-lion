package com.dianping.lion.web.action.system;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ConsoleConstants;
import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProductService;
import com.dianping.lion.service.TeamService;
import com.dianping.lion.web.action.common.AbstractLionAction;

@SuppressWarnings("serial")
public class TeamAction extends AbstractLionAction implements ServletRequestAware{
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ProductService productService;
	
	private List<Team> teamList;
	
	private String active = ConsoleConstants.TEAM_NAME;
	private int id;
	private Team team;
	private String name;
	private String errorMessage = "部门已关联产品，不能删除";
	
	public String teamList(){
		reInitiate();
		return SUCCESS;
	}
	
	public String teamAdd(){
		return SUCCESS;
	}
	
	public String teamAddSubmit(){
		Team team = new Team();
		team.setName(name);
		team.setCreateTime(new Date(System.currentTimeMillis()));
		team.setModifyTime(new Date(System.currentTimeMillis()));
		teamService.create(team);
		reInitiate();
		return SUCCESS;
	}
	
	public String teamEdit(){
		team = teamService.findTeamByID(id);
		return SUCCESS;
	}
	
	public String teamEditSubmit(){
		Team team = new Team();
		team.setId(id);
		team.setName(name);
		team.setCreateTime(new Date(System.currentTimeMillis()));
		team.setModifyTime(new Date(System.currentTimeMillis()));
		teamService.update(team);
		reInitiate();
		return SUCCESS;
	}
	
	public String deleteTeamAjax() {
		List<Product>  teamProducts = productService.findProductByTeamID(id);
		Team team = teamService.findTeamByID(id);
		if(teamProducts.size() > 0) {
			errorMessage = team.getName() + errorMessage;
			return ERROR;
		} else {
			teamService.delete(id);
			reInitiate();
			return SUCCESS;
		}
	}
	
	private void reInitiate() {
		this.active = ConsoleConstants.TEAM_NAME;
		teamList = teamService.findAll();
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

	public TeamService getTeamService() {
		return teamService;
	}

	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
