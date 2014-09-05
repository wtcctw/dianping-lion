package com.dianping.lion.dao;

import com.dianping.lion.entity.LocalConfigOverview;

import java.util.List;

public interface LocalConfigDiffDao {
	public List<LocalConfigOverview> getLocalConfigDiffHalfList(int projectId, int lenvId);
}
