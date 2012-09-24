/**
 * Project: com.dianping.lion.lion-service-0.0.1
 * 
 * File Created at 2012-9-13
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
package com.dianping.lion.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.ServiceConstants;
import com.dianping.lion.dao.PrivilegeDao;
import com.dianping.lion.entity.Privilege;
import com.dianping.lion.entity.PrivilegeCategory;
import com.dianping.lion.entity.Resource;
import com.dianping.lion.entity.Role;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.CacheClient;
import com.dianping.lion.service.PrivilegeService;
import com.dianping.lion.service.UserService;
import com.dianping.lion.util.SecurityUtils;
import com.dianping.lion.vo.Paginater;
import com.dianping.lion.vo.RoleUserCriteria;

/**
 * @author danson.liu
 *
 */
public class PrivilegeServiceImpl implements PrivilegeService {
	
	private static final Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);
	
	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Autowired
	private UserService userService;
	
	private CacheClient cacheClient;
	
	private CacheClient userCacheClient;

	@Override
	public List<Role> getRoles() {
		return privilegeDao.getRoles();
	}
	
	@Override
	public List<PrivilegeCategory> getPrivilegeCategories() {
		return privilegeDao.getPrivilegeCategories();
	}
	
	@Override
	public List<Privilege> getRolePrivileges(int roleId) {
		return privilegeDao.getRolePrivileges(roleId);
	}
	
	@Override
	public void saveRolePrivilege(int roleId, List<Integer> privilegeIds) {
		privilegeDao.deleteRolePrivileges(roleId);
		privilegeDao.insertRolePrivileges(roleId, privilegeIds);
		cacheClient.removeAll();
	}

	@Override
	public int createRole(Role role) {
		role.setCreateUserId(SecurityUtils.getCurrentUserId());
		return privilegeDao.createRole(role);
	}

	@Override
	public Role getRole(int roleId) {
		return privilegeDao.getRole(roleId);
	}

	@Override
	public int updateRole(Role role) {
		return privilegeDao.updateRole(role);
	}

	@Override
	public void deleteRole(int roleId) {
		privilegeDao.deleteUserRoleRelation(roleId);
		privilegeDao.deleteRolePrivilegeRelation(roleId);
		privilegeDao.deleteRole(roleId);
		cacheClient.removeAll();
		userCacheClient.removeAll();
	}

	@Override
	public Resource getResourceMatchUrl(String url) {
		Resource matchedResource = cacheClient.get(ServiceConstants.CACHE_PRIV_URL_PREFIX + url);
		if (matchedResource == null) {
			List<Resource> allResources = getAllResource();
			for (Resource resource : allResources) {
				String resourceUrl = resource.getUrl();
				if (StringUtils.isNotBlank(resourceUrl)) {
					try {
						Pattern pattern = Pattern.compile("^" + resourceUrl + "$", Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(url);
						if (matcher.find()) {
							matchedResource = resource;
							break;
						}
					} catch (PatternSyntaxException e) {
						logger.error("ResourceUrl[" + resourceUrl + "] is an invalid url pattern.", e);
					}
				}
			}
			if (matchedResource == null) {
				matchedResource = NullResource.INSTANCE;
			}
			cacheClient.set(ServiceConstants.CACHE_PRIV_URL_PREFIX + url, matchedResource);
		}
		return !NullResource.INSTANCE.equals(matchedResource) ? matchedResource : null;
	}
	
	private List<Resource> getAllResource() {
		return privilegeDao.getAllResource();
	}
	
	private List<Resource> getUserResources(int userId) {
		return privilegeDao.getUserResources(userId);
	}

	@Override
	public boolean isUserHasResourcePrivilege(Integer userId, String resource) {
		if (userId == null) {
			return false;
		}
		User user = userService.findById(userId);
		if (user == null) {
			return false;
		}
		if (user.isAdmin() || user.isSystem()) {
			return true;
		}
		
		/*处理特殊资源，固定交由admin或sa处理的，不会拿来授权*/
		if (ServiceConstants.RES_CODE_SECURITY.equals(resource) || ServiceConstants.RES_CODE_JOB.equals(resource)
				|| ServiceConstants.RES_CODE_SETTING.equals(resource)) {
			return user.isAdmin();
		}
		if (ServiceConstants.RES_CODE_CACHE.equals(resource) || ServiceConstants.RES_CODE_USER.equals(resource)) {
			return user.isAdmin() || user.isSA();
		}
		String cacheKey = ServiceConstants.CACHE_USER_RES_PREFIX + userId + "_" + resource;
		Boolean hasPrivilege = cacheClient.get(cacheKey);
		if (hasPrivilege == null) {
			Resource resourceFound = findResourceByCode(resource);
			if (resourceFound == null) {
				logger.warn("Resource[code=" + resource + "] is not found.");
				hasPrivilege = false;
			} else {
				for (Resource resourceObj : getUserResources(userId)) {
					if (resourceObj.getId() == resourceFound.getId()) {
						hasPrivilege = true;
						break;
					}
				}
				if (!Boolean.TRUE.equals(hasPrivilege)) {
					hasPrivilege = false;
				}
			}
			cacheClient.set(cacheKey, hasPrivilege);
		}
		return hasPrivilege;
	}
	
	@Override
	public boolean isUserHasResourcePrivilege(Integer userId, int resourceId) {
		if (userId == null) {
			return false;
		}
		User user = userService.findById(userId);
		if (user == null) {
			return false;
		}
		if (user.isAdmin()) {
			return true;
		}
		String cacheKey = ServiceConstants.CACHE_USER_RES_PREFIX + userId + "_" + resourceId;
		Boolean hasPrivilege = cacheClient.get(cacheKey);
		if (hasPrivilege == null) {
			for (Resource resource : getUserResources(userId)) {
				if (resource.getId() == resourceId) {
					hasPrivilege = true;
					break;
				}
			}
			if (!Boolean.TRUE.equals(hasPrivilege)) {
				hasPrivilege = false;
			}
			cacheClient.set(cacheKey, hasPrivilege);
		}
		return hasPrivilege;
	}

	@Override
	public Resource findResourceByCode(String resourceCode) {
		return privilegeDao.findResourceByCode(resourceCode);
	}

	@Override
	public Paginater<User> paginateRoleUsers(RoleUserCriteria criteria, Paginater<User> paginater) {
		long userCount = privilegeDao.getUserCount(criteria);
		List<User> userList = privilegeDao.getUserList(criteria, paginater);
		paginater.setTotalCount(userCount);
        paginater.setResults(userList);
        return paginater;
	}

	@Override
	public void addRole2User(int roleId, int userId) {
		privilegeDao.addRole2User(roleId, userId);
		cacheClient.removeAll();
		userCacheClient.removeAll();
	}

	@Override
	public void deleteRoleFromUser(int roleId, int userId) {
		privilegeDao.deleteRoleFromUser(roleId, userId);
		cacheClient.removeAll();
		userCacheClient.removeAll();
	}

	public void setPrivilegeDao(PrivilegeDao privilegeDao) {
		this.privilegeDao = privilegeDao;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}
	
	public void setUserCacheClient(CacheClient userCacheClient) {
		this.userCacheClient = userCacheClient;
	}

	@SuppressWarnings("serial")
	static final class NullResource extends Resource {
		static Resource INSTANCE = new NullResource();
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof NullResource)) {
				return false;
			}
			return true;
		}
		@Override
		public int hashCode() {
			return 37;
		}
	}

}
