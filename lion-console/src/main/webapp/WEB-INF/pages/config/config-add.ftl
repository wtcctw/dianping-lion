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
				  <li class="active">配置添加</li>
				</ul>
			</div>
		</div>
		<#include "/WEB-INF/pages/common/message.ftl">
		<div class="row">
			<div class="span12">
			<@s.form action="/config/createConfig.vhtml">
				<@s.hidden name="menu"/><@s.hidden name="pid"/><@s.hidden name="envId"/>
				<table class="table table-bordered table-condensed lion">
					<thead>
						<tr>
							<th colspan="5">新增配置</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="lion-label">Key：</td>
							<td>
								<@s.textfield name="config.key" cssClass="input-medium"/>
							</td>
							<td class="lion-label">描述：</td>
							<td>
								<@s.textfield name="config.desc" cssClass="input-large"/>
							</td>
							<td class="lion-label">类型：</td>
							<td>
								<@s.select id="type" name="config.type" list="%{@com.dianping.lion.entity.ConfigTypeEnum@values()}" listKey="value"
									listValue="label" cssClass="input-medium"/>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="form-actions">
					<div class="pull-right">
						<button type="submit" class="btn btn-primary">保存</button>&nbsp;&nbsp;&nbsp;
						<button type="reset" class="btn">reset</button>
					</div>
				</div>
			</@s.form>
			</div>
			<@s.if test="%{config != null && config.id > 0}">
			<table class="table table-bordered table-condensed lion">
				<tbody>
					<tr>
						<td class="lion-label">Value：</td>
						<td colspan="5" id="valueContainer">
							<textarea name="value" rows="5" style="width:750px;"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
			</@s.if>
		</div>
	</div>
</body>