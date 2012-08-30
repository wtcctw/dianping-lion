<table class="table table-bordered table-condensed nohover">
	<thead>
	    <tr>
	      <th>Name</th>
	      <th>Value</th>
	    </tr>
	</thead>
	<@s.iterator value="keyMap">
	<tr>
		<td width="60"><@s.property value="key"/></td>
		<td style="word-break:break-all;"><pre><@s.property value="value"/></pre></td>
	<tr>
	</@s.iterator>
</table>