(function($) {
	$.fn.mapeditor = function() {
		var init = this.data('map-editor-init');
		if (!init) {
			this.on("show", function() {
				$('body').css({ 'overflow' : 'hidden' });
				//let me stay in the center
				$(this).css('margin-left', ($(this).outerWidth() / 2) * -1);
			});
			this.on("hide", function() {
				$('body').css({ 'overflow' : 'auto' });
			});
			this.data('map-editor-init', true);
		}
		$.extend(this, {
			show : function(options) {
				this.reset();
				this.value(options.value);
				if (typeof options.title != "undefined") {
					this.find(".map-editor-title").text(options.title);
				}
				var $ok_btn = this.find(".ok-btn").unbind("click");
				var $editor = this;
				if (typeof options.ok != "undefined") {
					$ok_btn.click(function() {
						options.ok($editor);
						return false;
					});
				} else {
					$ok_btn.click(function() {return false;});
				}
				return this.modal({backdrop : "static", keyboard : false});
			},
			value : function(map) {
				var editorHtml = "";
				if (!map.isBlank()) {
					try {
						var json = $.parseJSON(map);
						for (var prop in json) {
							editorHtml += $.mapeditor.buildNewItem(prop, json[prop]);
						}
					} catch (err) {
						editorHtml = "<div class='form-alert alert-warn'>非法的数据格式，该编辑器只支持简单的key/value数据!</div>";
					}
				} else {
					editorHtml = "<a class='new-item-btn' href='#' onclick='$.mapeditor.addItem(this, event);'><i class='icon-plus'></i></a>";
				}
				this.find(".modal-body").html(editorHtml);
			},
			reset : function() {
				this.hideAlerts();
				this.find(".trim_check").attr("checked", true);
			}
		});
		return this;
	};
	
	$.mapeditor = {
		buildNewItem : function(key, value) {
			return "<div class='map-item control-group' style='margin-bottom:0px;'>"
				+ "<input type='text' class='map-key-input' style='margin-left: 10px;width:200px;height:14px;' value='" + key.escapeQuotes() + "'>&nbsp;=&nbsp;"
				+ "<input type='text' class='map-value-input' style='width:320px;height:14px;' value='" + value.escapeQuotes() + "'>&nbsp;&nbsp;"
				+ "<a href='#' onclick='$.mapeditor.moveUpItem(this, event);'><i class='icon-arrow-up'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.mapeditor.moveDownItem(this, event);'><i class='icon-arrow-down'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.mapeditor.addItem(this, event);'><i class='icon-plus'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.mapeditor.deleteItem(this, event);'><i class='icon-minus'></i></a>" 
				+ "</div>";
		},
		addItem : function(element, event) {
			var $editor_body = $(element).parents(".modal-body");
			var $item = $(element).parents(".map-item");
			$editor_body.find(".new-item-btn").remove();
			if ($(element).hasClass("new-item-btn")) {
				$editor_body.append($.mapeditor.buildNewItem("", ""));
				$editor_body.find(".map-key-input").focus();
			} else {
				$item.after($.mapeditor.buildNewItem("", ""));
				$item.next(".map-item").find(".map-key-input").focus();
			}
			event.preventDefault();
		},
		deleteItem : function(element, event) {
			var $editor_body = $(element).parents(".modal-body");
			var $item = $(element).parents(".map-item");
			$item.remove();
			if ($editor_body.find(".map-item").length == 0) {
				$editor_body.html("<a class='new-item-btn' href='#' onclick='$.mapeditor.addItem(this, event);'><i class='icon-plus'></i></a>");
			}
			event.preventDefault();
		},
		moveUpItem : function(element, event) {
			var $item = $(element).parents(".map-item");
			var $prev = $item.prev(".map-item");
			if ($prev.length > 0) {
				$item.insertBefore($prev);
			}
			event.preventDefault();
		},
		moveDownItem : function(element, event) {
			var $item = $(element).parents(".map-item");
			var $next = $item.next(".map-item");
			if ($next.length > 0) {
				$item.insertAfter($next);
			}
			event.preventDefault();
		}
	}
})(jQuery);
