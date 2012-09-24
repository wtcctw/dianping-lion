<table class="table table-bordered table-striped table-condensed" style="margin-bottom:5px;">
	<thead>
    <tr>
      <th width="30">序号</th>
      <th width="280">KEY</th>
      <th>DESC</th>
    </tr>
  </thead>
  <tbody>
  	<@s.iterator value="paginater.results" var="config" status="configStatus">
  	<tr class="config_row">
  		<td>
  			<input type="radio" value="${key}" name="configkey">
  		</td>
  		<td class="config_key">${key}</td>
  		<td>${desc}</td>
  	</tr>
  	</@s.iterator>
  </tbody>
</table>
<#include "/WEB-INF/pages/common/paginater.ftl">
