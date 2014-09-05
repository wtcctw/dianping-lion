package com.dianping.lion.service.impl;

import com.dianping.lion.dao.GlobalSearchDao;
import com.dianping.lion.entity.GlobalSearch;
import com.dianping.lion.service.GlobalSearchService;
import com.dianping.lion.vo.GlobalSearchCriteria;
import com.dianping.lion.vo.Paginater;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GlobalSearchServiceImpl implements GlobalSearchService {

	@Autowired
	private GlobalSearchDao globalSearchDao;

	@Override
	public List<GlobalSearch> getGlobalSearchList(GlobalSearchCriteria criteria) {
		return globalSearchDao.getGlobalSearchList(criteria.getKey(), criteria.getValue(), criteria.getEnvId());
	}
}
