package com.dianping.lion.service;

import com.dianping.lion.entity.GlobalSearch;
import com.dianping.lion.vo.GlobalSearchCriteria;
import com.dianping.lion.vo.Paginater;

import java.util.List;

public interface GlobalSearchService {
	public List<GlobalSearch> getGlobalSearchList(GlobalSearchCriteria criteria);
}
