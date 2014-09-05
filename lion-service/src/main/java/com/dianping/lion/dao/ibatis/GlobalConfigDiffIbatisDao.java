package com.dianping.lion.dao.ibatis;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.dianping.lion.dao.GlobalConfigDiffDao;
import com.dianping.lion.entity.GlobalConfigDiff;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.lion.util.Maps;

public class GlobalConfigDiffIbatisDao extends SqlMapClientDaoSupport implements GlobalConfigDiffDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalConfigDiff> getGlobalConfigDiffList(int lenvId, int renvId, List<Integer> projectIds) {
		if (projectIds == null || projectIds.isEmpty()) {
			projectIds = Collections.emptyList();
		}

		try {
			return getSqlMapClientTemplate().queryForList("GlobalConfigDiff.getGlobalConfigDiffList",
			      Maps.entry("lenvId", lenvId).entry("renvId", renvId).entry("projectIds", projectIds).get());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setGlobalConfigDiff(GlobalConfigDiff globalConfigDiff) {
		 try {
			  getSqlMapClientTemplate().insert("GlobalConfigDiff.setGlobalConfigDiff",
					  globalConfigDiff);

		 } catch (Exception e) {
			  e.printStackTrace();
		 }
	}

	 @SuppressWarnings("unchecked")
	 @Override
	 public void updateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff) {
		  try {
				getSqlMapClientTemplate().update("GlobalConfigDiff.updateGlobalConfigDiff",
						globalConfigDiff);

		  } catch (Exception e) {
				e.printStackTrace();
		  }
	 }
}
