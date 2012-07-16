<@s.if test="%{errorMessage != null}">
<div class="alert alert-error alert-lion">
  <a class="close" data-dismiss="alert">×</a>
  <strong>Error：</strong> ${errorMessage}
</div>
</@s.if>
<@s.if test="%{warnMessage != null}">
<div class="alert alert-lion">
  <a class="close" data-dismiss="alert">×</a>
  <strong>Warn：</strong> ${warnMessage}
</div>
</@s.if>
<@s.if test="%{infoMessage != null}">
<div class="alert  alert-success alert-lion">
  <a class="close" data-dismiss="alert">×</a>
  <strong>Info：</strong> ${infoMessage}
</div>
</@s.if>