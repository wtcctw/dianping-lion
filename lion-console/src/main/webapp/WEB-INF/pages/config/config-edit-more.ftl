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
				  <li>泳道配置<span class="divider">></span></li>
				  <li class="active">${environment.label}</li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<#include "/WEB-INF/pages/common/message.ftl">
				<input type=hidden id="config-key" value="${config.key}"/>
				<input type=hidden id="config-id" value="${config.id}"/>
				<input type=hidden id="env-id" value="${environment.id}"/>

				<table class="table table-bordered table-condensed lion">
					<tbody>
						<tr>
							<td class="lion-label">Key：</td>
							<td><b><@s.property value="config.key"/></b></td>
						</tr>
						<tr>
							<td class="lion-label">描述：</td>
							<td><@s.property value="config.desc"/></td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered table-condensed lion">
					<tbody>
						<tr>
							<td colspan="2" class="lion-label" style="text-align: left;">
								&nbsp;&nbsp;<b>添加泳道配置</b>
							</td>
						</tr>
						<tr>
							<td class="lion-label">环境：</td>
							<td id="valueContainer">
						      	${environment.label}
							</td>
						</tr>
						<tr>
						    <td class="lion-label">描述：</td>
                            <td id="valueContainer">
                                <input type="text" id="context-desc" name="desc" style="width:600px;">
                            </td>
						</tr>
						<tr>
							<td class="lion-label">泳道：</td>
							<td id="valueContainer">
								<input type="text" id="context-name" style="width:600px;">
							</td>
						</tr>
						<tr>
							<td class="lion-label">Value：</td>
							<td id="valueContainer">
								<textarea name="add-value" id="context-value" rows="5" style="width:600px;"></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input id="create-context-btn" type="button" class="btn btn-primary pull-right" value=" 创建 ">
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
	        <div class="span12" id="context-list-container">
	            <#include "/WEB-INF/pages/config/context-list-table.ftl">
	        </div>
        </div>
	</div>
	<#include "/WEB-INF/pages/config/config-context-modal.ftl">
</body>