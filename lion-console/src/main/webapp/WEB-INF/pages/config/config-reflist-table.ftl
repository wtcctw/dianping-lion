<div class="tabbable">
	<ul class="nav nav-tabs">
		<@s.iterator value="environments" var="env" status="envStatus">
			<li<@s.if test="%{#envStatus.first}"> class="active"</@s.if>>
				<a href="#${name}" data-toggle="tab">${label}(<@s.property value="refList[#env].size()"/>)</a>
			</li>
		</@s.iterator>
		<a class="close" data-dismiss="modal" >&times;</a>
	</ul>
	<div class="tab-content" id="reflist-container">
    	<@s.iterator value="environments" var="env" status="envStatus">
	    	<div class="tab-pane<@s.if test="%{#envStatus.first}">  active</@s.if>" id="${name}">
			  <table class="table table-bordered table-striped table-condensed">
			  	<thead>
				    <tr>
				      <th width="200">Key (<span style="color:orange;">${name}</span>)</th>
				      <th>Value</th>
				    </tr>
				  </thead>
				  <tbody>
				  	<@s.iterator value="refList[#env]">
				  	<tr>
				  		<td>${config.key}</td>
						<td>${value}</td>
				  	</tr>
				  	</@s.iterator>
				  </tbody>
			  </table>
			</div>
    	</@s.iterator>
  	</div>
</div>
