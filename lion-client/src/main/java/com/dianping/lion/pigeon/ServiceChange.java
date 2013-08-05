package com.dianping.lion.pigeon;

import java.util.List;

/**
 * Comment of ServiceChange
 * 
 * @author yong.you
 * 
 */
public interface ServiceChange {
	// 当服务器权重变化
	public void onHostWeightChange(String host, int weight);

	// 当服务新增机器、修改机器。 String[]默认1维数组，ip+port
	public void onServiceHostChange(String serviceName, List<String[]> hostList);

	// 当服务新增机器、修改机器、增加服务分组。 String[]默认1维数组，ip+port
	public void onServiceHostChange(String serviceName, String group, List<String[]> hostList);
}
