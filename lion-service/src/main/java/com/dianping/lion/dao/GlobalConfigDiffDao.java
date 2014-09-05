package com.dianping.lion.dao;

import java.util.Date;
import java.util.List;

import com.dianping.lion.entity.GlobalConfigDiff;

public interface GlobalConfigDiffDao {
	public List<GlobalConfigDiff> getGlobalConfigDiffList(int lenvId, int renvId, List<Integer> projectIds);

	public void setGlobalConfigDiff(GlobalConfigDiff globalConfigDiff);

	public void updateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff);
}
