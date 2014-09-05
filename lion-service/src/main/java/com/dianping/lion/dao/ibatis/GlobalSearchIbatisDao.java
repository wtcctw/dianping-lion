package com.dianping.lion.dao.ibatis;

import java.util.List;

import com.dianping.lion.util.Maps;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.dao.GlobalSearchDao;
import com.dianping.lion.entity.GlobalSearch;

public class GlobalSearchIbatisDao extends SqlMapClientDaoSupport implements GlobalSearchDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalSearch> getGlobalSearchList(String key, String value, Integer envId) {
		try {
			return getSqlMapClientTemplate().queryForList("GlobalSearch.getGlobalSearchList",
			      Maps.entry("key", key).entry("value", value).entry("envId", envId).get());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
