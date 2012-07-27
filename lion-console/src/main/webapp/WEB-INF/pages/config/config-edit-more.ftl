<head>
	<title>业务配置</title>
	<script type="text/javascript" src="<@s.url value="/js/biz/config/config-add.js"/>"></script>
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
		<#include "/WEB-INF/pages/common/message.ftl">
		<table class="table table-bordered table-condensed lion">
			<tbody>
				<tr>
					<td class="lion-label">Key：</td>
					<td width="450"><b>${config.key}</b></td>
					<td class="lion-label">类型：</td>
					<td>${config.typeEnum.label}</td>
				</tr>
				<tr>
					<td class="lion-label">描述：</td>
					<td colspan="3">${config.desc}</td>
				</tr>
			</tbody>
		</table>
		<table class="table table-bordered table-condensed lion">
			<tbody>
				<tr>
					<td colspan="3" class="lion-label" style="text-align: left;">
						&nbsp;&nbsp;<b>添加上下文相关的配置值</b> (如：该应用部署在某个ip的机器上或某个版本时使用的配置值)
					</td>
				</tr>
				<tr>
					<td class="lion-label">环境：</td>
					<td colspan="2" id="envContainer">
						<input type="checkbox" id="add-select-all-env"/><label for="add-select-all-env" class="help-inline">全选</label>
				      	&nbsp;&nbsp;
				      	<@s.iterator value="environments" status="envStatus" var="env">
			      		<input type="checkbox" name="add-config-env" id="add-config-env-${envStatus.index}" value="${id}" style="margin-left:10px;"
			      		<@s.if test="%{environment.id == #env.id}"> checked="checked"</@s.if>><label for="add-config-env-${envStatus.index}" class="help-inline">${label}</label>
				      	&nbsp;&nbsp;
				      	</@s.iterator>
					</td>
				</tr>
				<tr>
					<td class="lion-label">Value：</td>
					<td colspan="2" id="valueContainer">
						<textarea name="add-value" id="add-value" rows="5" style="width:930px;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="lion-label" rowspan="3">上下文：</td>
					<td class="lion-label">IP：</td>
					<td>
						<textarea id="add-ip" rows="3" style="width:800px;"></textarea>
						<div class="alert" style="padding:2px 1px 1px 1px;margin-bottom:0;margin-top:4px;width:807px;">
							<strong>Attention：</strong> 使用IP作为上下文时，请检查与&nbsp;"环境"&nbsp;项设置一起是否合理。
						</div>
					</td>
				</tr>
				<tr>
					<td class="lion-label">Version：</td>
					<td><input type="text" id="add-version" style="width:800px;"></td>
				</tr>
				<tr>
					<td class="lion-label">自定义：</td>
					<td><textarea id="add-customized" rows="3" style="width:800px;"></textarea></td>
				</tr>
				<tr>
					<td colspan="3">
						<input id="add-btn" type="button" class="btn btn-primary pull-right" value=" 保存 ">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>