package com.dianping.lion.service.impl;

import com.dianping.lion.dao.LocalConfigDiffDao;
import com.dianping.lion.entity.LocalConfigDiff;
import com.dianping.lion.entity.LocalConfigOverview;
import com.dianping.lion.service.LocalConfigDiffService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LocalConfigDiffServiceImpl implements LocalConfigDiffService {

	@Autowired
	LocalConfigDiffDao localConfigDiffDao;

	public Object[] getLocalConfigDiffOverview(int projectId, int lenvId, int renvId) {
		List<LocalConfigDiff> localConfigDiffs = getLocalConfigDiff(projectId, lenvId, renvId);

		int diffNum = 0;
		int omitNum = 0;

		for (LocalConfigDiff localConfigDiff : localConfigDiffs) {
			if (localConfigDiff.isDiff()) {
				++diffNum;
			}

			if (localConfigDiff.isOmit()) {
				++omitNum;
			}
		}

		Object[] objects = { 0, 0 };
		objects[0] = diffNum;
		objects[1] = omitNum;

		return objects;
	}

	public List<LocalConfigDiff> getLocalConfigDiffNumDetail(int projectId, int lenvId, int renvId) {
		return rmLocalConfigOmit(getLocalConfigDiff(projectId, lenvId, renvId));
	}

	public List<LocalConfigDiff> getLocalConfigOmitNumDetail(int projectId, int lenvId, int renvId) {
		return rmLocalConfigDiff(getLocalConfigDiff(projectId, lenvId, renvId));
	}

	public List<LocalConfigDiff> getLocalConfigDiff(int projectId, int lenvId, int renvId) {
		List<LocalConfigOverview> llocalConfigOverviews = localConfigDiffDao
		      .getLocalConfigDiffHalfList(projectId, lenvId);
		List<LocalConfigOverview> rlocalConfigOverviews = localConfigDiffDao
		      .getLocalConfigDiffHalfList(projectId, renvId);

		List<LocalConfigDiff> localConfigDiffs = genLocalConfigDiffs(llocalConfigOverviews, rlocalConfigOverviews);
		localConfigDiffs = rmLocalConfigSames(localConfigDiffs);
		return localConfigDiffs;
	}

	private List<LocalConfigDiff> rmLocalConfigSames(List<LocalConfigDiff> localConfigDiffs) {
		List<LocalConfigDiff> tmpLocalConfigDiff = new ArrayList<LocalConfigDiff>();
		for (LocalConfigDiff localConfigDiff : localConfigDiffs) {
			if (localConfigDiff.isOmit() || localConfigDiff.isDiff()) {
				tmpLocalConfigDiff.add(localConfigDiff);
			}
		}
		return tmpLocalConfigDiff;
	}

	private List<LocalConfigDiff> rmLocalConfigDiff(List<LocalConfigDiff> localConfigDiffs) {
		List<LocalConfigDiff> tmpLocalConfigDiff = new ArrayList<LocalConfigDiff>();
		for (LocalConfigDiff localConfigDiff : localConfigDiffs) {
			if (!localConfigDiff.isDiff()) {
				tmpLocalConfigDiff.add(localConfigDiff);
			}
		}
		return tmpLocalConfigDiff;
	}

	private List<LocalConfigDiff> rmLocalConfigOmit(List<LocalConfigDiff> localConfigDiffs) {
		List<LocalConfigDiff> tmpLocalConfigDiff = new ArrayList<LocalConfigDiff>();
		for (LocalConfigDiff localConfigDiff : localConfigDiffs) {
			if (!localConfigDiff.isOmit()) {
				tmpLocalConfigDiff.add(localConfigDiff);
			}
		}
		return tmpLocalConfigDiff;
	}

	private List<LocalConfigDiff> genLocalConfigDiffs(List<LocalConfigOverview> llocalConfigOverviews,
	      List<LocalConfigOverview> rlocalConfigOverviews) {
		int ls = llocalConfigOverviews.size();
		int rs = rlocalConfigOverviews.size();

		int lf = 0;
		int rf = 0;

		List<LocalConfigDiff> localConfigDiffs = new ArrayList<LocalConfigDiff>();
		String key, lvalue, rvalue;

		while (lf < ls && rf < rs) {
			LocalConfigOverview lhead = llocalConfigOverviews.get(lf);
			LocalConfigOverview rhead = rlocalConfigOverviews.get(rf);

			int compareValue = lhead.getKey().compareTo(rhead.getKey());

			if (compareValue < 0) {
				++lf;
				key = lhead.getKey();
				lvalue = lhead.getValue();
				rvalue = null;
			} else if (compareValue == 0) {
				++lf;
				++rf;
				key = lhead.getKey();
				lvalue = lhead.getValue();
				rvalue = rhead.getValue();
			} else {
				++rf;
				key = rhead.getKey();
				lvalue = null;
				rvalue = rhead.getValue();
			}

			localConfigDiffs.add(genLocalConfigDiff(key, lvalue, rvalue));
		}

		LocalConfigOverview head;
		if (lf == ls) {
			if (rf == rs) {
			} else {
				for (int i = rf; i != rs; ++i) {
					head = rlocalConfigOverviews.get(i);
					localConfigDiffs.add(genLocalConfigDiff(head.getKey(), null, head.getValue()));
				}
			}
		} else {
			for (int i = lf; i != ls; ++i) {
				head = llocalConfigOverviews.get(i);
				localConfigDiffs.add(genLocalConfigDiff(head.getKey(), head.getValue(), null));
			}
		}

		return localConfigDiffs;
	}

	private String checkValue(String value) {
		return value == null ? "" : value;
	}

	private LocalConfigDiff genLocalConfigDiff(String key, String lvalue, String rvalue) {
		boolean diff, omit;

		if (isConfigOmit(lvalue)) {
			if (isConfigOmit(rvalue)) {
				diff = false;
				omit = false;
			} else {
				diff = false;
				omit = true;
			}
		} else {
			if (isConfigOmit(rvalue)) {
				diff = false;
				omit = true;
			} else {
				diff = !(lvalue.equals(rvalue));
				omit = false;
			}
		}

		return new LocalConfigDiff(key, checkValue(lvalue), checkValue(rvalue), diff, omit);
	}

	private boolean isConfigOmit(String value) {
		return value == null || value.isEmpty();
	}
}
