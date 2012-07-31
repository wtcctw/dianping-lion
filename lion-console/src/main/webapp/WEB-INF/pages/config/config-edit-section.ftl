<@s.if test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@String">
<textarea name="add-value" id="add-value" rows="5" style="width:930px;"></textarea>
</@s.if>
<@s.elseif test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@Number">
<input type="text" id="add-value" class="input-medium"><span class='help-inline hide message'>数字,必填!</span>
</@s.elseif>
<@s.elseif test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@Boolean">
<input type='radio' name='add-value' id='add-value-yes' value='true' checked='checked'><label for='add-value-yes' class='help-inline'>true</label>
<input type='radio' name='add-value' id='add-value-no' value='false'><label for='add-value-no' class='help-inline'>false</label>
</@s.elseif>
<@s.elseif test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@List_Str">
<textarea id='add-value' rows='7' style='width:930px;' readonly='readonly' onclick='openListEditor("add-value", false, event);'></textarea>
<a href='#' onclick='openListEditor("add-value" , true, event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>
</@s.elseif>
<@s.elseif test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@List_Num">
<textarea id='add-value' rows='7' style='width:930px;' readonly='readonly' onclick='openListEditor("add-value", false, event);'></textarea>
<a href='#' onclick='openListEditor("add-value" , false, event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>
</@s.elseif>
<@s.elseif test="config.typeEnum == @com.dianping.lion.entity.ConfigTypeEnum@Map">
<textarea id='add-value' rows='7' style='width:930px;' readonly='readonly' onclick='openMapEditor("add-value", event);'></textarea>
<a href='#' onclick='openMapEditor("add-value", event);'><i class='icon-edit' style='vertical-align:bottom;'></i></a>
</@s.elseif>