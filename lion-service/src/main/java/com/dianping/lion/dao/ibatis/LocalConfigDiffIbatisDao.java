package com.dianping.lion.dao.ibatis;

import com.dianping.lion.dao.LocalConfigDiffDao;
import com.dianping.lion.entity.LocalConfigOverview;
import com.dianping.lion.util.Maps;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

public class LocalConfigDiffIbatisDao extends SqlMapClientDaoSupport implements LocalConfigDiffDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LocalConfigOverview> getLocalConfigDiffHalfList(int projectId, int envId) {
		try {
			return getSqlMapClientTemplate().queryForList("LocalConfigDiff.getLocalConfigOverview",
			      Maps.entry("projectId", projectId).entry("envId", envId).get());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
