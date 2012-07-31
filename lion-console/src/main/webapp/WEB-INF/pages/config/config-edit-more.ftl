<head>
	<title>业务配置</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/config/config-edit-more.js"/>"></script>
	<style type="text/css">
		tr.clion td, tr.clion th,
		tr.clion:hover td, tr.clion:hover th {
		  background-color: #f9f9f9 !important;
		}
		/*tr.clion td, tr.clion th,*/
		tr.xlion:hover td, tr.xlion:hover th {
		  background-color: white !important;
		}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<ul class="noborder smallpadding smallbottommargin breadcrumb">
				  <li>
				    ${project.product.team.name}<span class="divider">></span>
				  </li>
				  <li>
				    ${project.product.name}<span class="divider">></span>
				  </li>
				  <li>
				    ${project.name}<span class="divider">></span>
				  </li>
				  <li>上下文相关配置<span class="divider">></span></li>
				  <li class="active">${environment.label}</li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<#include "/WEB-INF/pages/common/message.ftl">
				<table class="table table-bordered table-condensed lion">
					<tbody>
						<tr>
							<td class="lion-label">Key：</td>
							<td width="450"><b><@s.property value="config.key"/></b></td>
							<td class="lion-label">类型：</td>
							<td>${config.typeEnum.label}</td>
						</tr>
						<tr>
							<td class="lion-label">描述：</td>
							<td colspan="3"><@s.property value="config.desc"/></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered table-condensed lion">
					<tbody>
						<tr>
							<td colspan="5" class="lion-label" style="text-align: left;">
								&nbsp;&nbsp;<b>添加上下文相关的配置值</b> (如：该应用部署在某个ip的机器上或某个版本时使用的配置值)
							</td>
						</tr>
						<tr>
							<td class="lion-label">环境：</td>
							<td colspan="2" id="envContainer">
								<input type="checkbox" id="add-select-all-env"/><label for="add-select-all-env" class="help-inline">全选</label>
						      	&nbsp;&nbsp;
						      	<@s.iterator value="environments" status="envStatus" var="env">
					      		<input type="checkbox" name="add-config-env" id="add-config-env-${envStatus.index}" value="${id}" style="margin-left:5px;"
					      		<@s.if test="%{environment.id == #env.id}"> checked="checked"</@s.if>><label for="add-config-env-${envStatus.index}" class="help-inline">${label}</label>
						      	&nbsp;&nbsp;
						      	</@s.iterator>
							</td>
							<td class="lion-label">描述：</td>
							<td width="320">
								<input type="text" style="width:300px;" name="desc" maxlength="50">
							</td>
						</tr>
						<tr>
							<td class="lion-label">Value：</td>
							<td colspan="4" id="valueContainer">
								<#include "/WEB-INF/pages/config/config-edit-section.ftl">
							</td>
						</tr>
						<tr>
							<td class="lion-label" rowspan="3">上下文：</td>
							<td class="lion-label">IP：</td>
							<td colspan="3">
								<textarea id="add-ip" rows="3" style="width:800px;"></textarea>
								<a href='#' onclick='openIPEditor(event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>
								<div class="alert" style="padding:2px 1px 1px 1px;margin-bottom:0;margin-top:4px;width:807px;">
									<strong>Attention：</strong> 使用IP作为上下文时，请检查与&nbsp;"环境"&nbsp;项设置一起是否合理。
								</div>
							</td>
						</tr>
						<tr>
							<td class="lion-label">Version：</td>
							<td colspan="3">
								<input type="text" id="add-version" style="width:800px;">
								<a href='#' onclick='openVersionEditor(event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>
							</td>
						</tr>
						<tr>
							<td class="lion-label">自定义：</td>
							<td colspan="3"><textarea id="add-customized" rows="3" style="width:800px;"></textarea></td>
						</tr>
						<tr>
							<td colspan="5">
								<input id="add-btn" type="button" class="btn btn-primary pull-right" value=" 创建 ">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<ul class="nav nav-tabs">
					<@s.iterator value="environments" var="env">
					<li<@s.if test="%{#env.id == envId}"> class="active"</@s.if>>
						<a href="#tab-content-${label}" data-toggle="tab">${label}</a>
					</li>
					</@s.iterator>
				</ul>
				<div class="tab-content" style="min-height: 500px;">
					<@s.iterator value="environments" var="env">
						<div class="tab-pane<@s.if test="%{#env.id == envId}"> active</@s.if>" id="tab-content-${label}">
							<table class="table table-bordered table-condensed">
								<thead>
								    <tr>
								      <th width="50">序号</th>
								      <th width="270">描述</th>
								      <th width="130">创建时间</th>
								      <th>VALUE</th>
								      <th width="75">是否默认值</th>
								      <th>操作</th>
								    </tr>
								  </thead>
								  <tbody>
								  	<@s.iterator value="instanceMap[#env.id]" var="inst" status="instStatus">
								  	<@s.if test="context != ''">
								  	<tr class="<@s.if test="#instStatus.odd == true">clion</@s.if><@s.else>xlion</@s.else>">
								  		<td rowspan="2">${instStatus.index}</td>
								  		<td>
								  			<span<@s.if test="%{#inst.isLongDesc(35)}"> rel="tooltip" 
								  				data-original-title="${desc?html}"</@s.if>>
								  				<@s.property value="%{#inst.getAbbrevDesc(35)}"/>
								  			</span>
								  		</td>
								  		<td>${createTime?datetime}</td>
								  		<td>
								  			<span<@s.if test="%{#inst.isLongValue()}"> rel="tooltip" 
								  				data-original-title="${moreValue?html}"</@s.if>>
  												<@s.property value="%{#inst.getAbbrevValue(80)}"/>
  											</span>
  										</td>
								  		<td>否</td>
								  		<td rowspan="2" style="vertical-align: middle;">
								  			<a href="#" class="moveup-config-btn"><i class="icon-edit" rel="tooltip" data-original-title="编辑配置值"></i></a>
								  			<a href="#" class="moveup-config-btn"><i class="icon-remove" rel="tooltip" data-original-title="删除配置项"></i></a>
								  			<a href="#" class="moveup-config-btn"><i class="icon-arrow-up" rel="tooltip" data-original-title="上移"></i></a>
  											<a href="#" class="movedown-config-btn"><i class="icon-arrow-down" rel="tooltip" data-original-title="下移"></i></a>
								  		</td>
								  	</tr>
								  	<tr class="<@s.if test="#instStatus.odd == true">clion</@s.if><@s.else>xlion</@s.else>">
								  		<td colspan="4">context：<@s.property value="context"/></td>
								  	</tr>
								  	</@s.if>
								  	<@s.else>
								  	<tr class="xlion">
								  		<td>${instStatus.index}</td>
								  		<td>----------------------------------------------------</td>
								  		<td>${createTime?datetime}</td>
								  		<td>
								  			<span<@s.if test="%{#inst.isLongValue()}"> rel="tooltip" 
								  				data-original-title="${moreValue?html}"</@s.if>>
  												<@s.property value="%{#inst.getAbbrevValue(80)}"/>
  											</span>
  										</td>
  										<td>是</td>
  										<td style="vertical-align: middle;">
								  			<a href="#" class="moveup-config-btn"><i class="icon-arrow-up" rel="tooltip" data-original-title="上移"></i></a>
  											<a href="#" class="movedown-config-btn"><i class="icon-arrow-down" rel="tooltip" data-original-title="下移"></i></a>
								  		</td>
								  	</tr>
								  	</@s.else>
								  	</@s.iterator>
								  </tbody>
							</table>
						</div>
					</@s.iterator>
				</div>
			</div>
		</div>
		<br/><br/>
	</div>
</body>