<@s.iterator value="mainMenu.subMenuOrGroups" var="menuOrGroup">
<@s.if test="%{#menuOrGroup instanceof com.dianping.lion.web.tag.MenuManager$SubMenu}">
<li><a href="<@s.url value=""/>">${label}</a></li>
</@s.if>
</@s.iterator>


