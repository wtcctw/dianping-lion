package com.dianping.lion.service;

import com.dianping.lion.entity.LocalConfigDiff;

import java.util.List;

public interface LocalConfigDiffService {
	public Object[] getLocalConfigDiffOverview(int projectId, int lenvId, int renvId);

	public List<LocalConfigDiff> getLocalConfigDiff(int projectId, int lenvId, int renvId);

	public List<LocalConfigDiff> getLocalConfigDiffNumDetail(int projectId, int lenvId, int renvId);

	public List<LocalConfigDiff> getLocalConfigOmitNumDetail(int projectId, int lenvId, int renvId);
}
