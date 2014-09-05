package com.dianping.lion.service;

import com.dianping.lion.entity.GlobalConfigDiff;

import java.util.Date;
import java.util.List;

public interface GlobalConfigDiffService {
	public List<GlobalConfigDiff> getGlobalConfigDiffList(int lenvId, int renvId, List<Integer> projectIds);

	public void setGlobalConfigDiff(GlobalConfigDiff globalConfigDiff);

	public void updateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff);
}
