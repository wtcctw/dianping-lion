package com.dianping.lion.service.impl;

import java.util.Date;
import java.util.List;

import com.dianping.lion.dao.GlobalConfigDiffDao;
import com.dianping.lion.entity.GlobalConfigDiff;
import com.dianping.lion.service.GlobalConfigDiffService;
import org.springframework.beans.factory.annotation.Autowired;

public class GlobalConfigDiffServiceImpl implements GlobalConfigDiffService {

	@Autowired
	GlobalConfigDiffDao globalConfigDiffDao;

	public List<GlobalConfigDiff> getGlobalConfigDiffList(int lenvId, int renvId, List<Integer> projectIds) {
		return globalConfigDiffDao.getGlobalConfigDiffList(lenvId, renvId, projectIds);
	}

	public void setGlobalConfigDiff (GlobalConfigDiff globalConfigDiff) {
		globalConfigDiffDao.setGlobalConfigDiff(globalConfigDiff);
	}

	public void updateGlobalConfigDiff(GlobalConfigDiff globalConfigDiff) {
		 globalConfigDiffDao.updateGlobalConfigDiff(globalConfigDiff);
	}
}
