<div class="tab-pane active" id="owner">
  <table class="table table-bordered table-striped table-condensed">
  	<thead>
	    <tr>
	      <th width="200">域账号</th>
	      <th>姓名</th>
	      <th width="50">&nbsp;</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<@s.iterator value="owners">
	  	<tr>
	  		<td>${user.loginName}</td>
			<td>${user.name}</td>
			<td><a href="#" class="no-dec" onclick="deleteMember(${projectId}, 'owner', ${userId});">删除</a></td>
	  	</tr>
	  	</@s.iterator>
	  </tbody>
  </table>
</div>
<div class="tab-pane" id="member">
  <table class="table table-bordered table-striped table-condensed">
  	<thead>
	    <tr>
	      <th width="200">域账号</th>
	      <th>姓名</th>
	      <th width="50">&nbsp;</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<@s.iterator value="members">
	  	<tr>
	  		<td>${user.loginName}</td>
			<td>${user.name}</td>
			<td><a href="#" class="no-dec" onclick="deleteMember(${projectId}, 'member', ${userId});">删除</a></td>
	  	</tr>
	  	</@s.iterator>
	  </tbody>
  </table>
</div>
<div class="tab-pane" id="operator">
  <table class="table table-bordered table-striped table-condensed">
  	<thead>
	    <tr>
	      <th width="200">域账号</th>
	      <th>姓名</th>
	      <th width="50">&nbsp;</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<@s.iterator value="operators">
	  	<tr>
	  		<td>${user.loginName}</td>
			<td>${user.name}</td>
			<td><a href="#" class="no-dec" onclick="deleteMember(${projectId}, 'operator', ${userId});">删除</a></td>
	  	</tr>
	  	</@s.iterator>
	  </tbody>
  </table>
</div>