(function($) {
	$.fn.listeditor = function() {
		var init = this.data('list-editor-init');
		if (!init) {
			this.on("show", function() {
//				$('body').css({ 'overflow' : 'hidden' });
			});
			this.on("hide", function() {
//				$('body').css({ 'overflow' : 'auto' });
			});
			this.data('list-editor-init', true);
		}
		$.extend(this, {
			show : function(options) {
				this.reset();
				this.value(options.value);
				if (typeof options.title != "undefined") {
					this.find(".list-editor-title").text(options.title);
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
			value : function (list) {
				var editorHtml = "";
				if (!list.isBlank()) {
					try {
						var json = $.parseJSON(list);
						for (var i = 0; i < json.length; i++) {
							editorHtml += $.listeditor.buildNewItem(json[i]);
						}
					} catch (err) {
						editorHtml = "<div class='form-alert alert-warn'>非法的数据格式，该编辑器只支持简单的list数据!</div>";
					}
				} else {
					editorHtml = "<a class='new-item-btn' href='#' onclick='$.listeditor.addItem(this, event);'><i class='icon-plus'></i></a>";
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
	
	$.listeditor = {
		buildNewItem : function(value) {
			return "<div class='list-item control-group' style='margin-bottom:0px;'>" 
				+ "<input type='text' class='list-item-input' style='margin-left: 10px;width: 415px;height:14px;' value='" + value.escapeQuotes() + "'>&nbsp;&nbsp;"
				+ "<a href='#' onclick='$.listeditor.moveUpItem(this, event);'><i class='icon-arrow-up'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.listeditor.moveDownItem(this, event);'><i class='icon-arrow-down'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.listeditor.addItem(this, event);'><i class='icon-plus'></i></a>&nbsp;" 
				+ "<a href='#' onclick='$.listeditor.deleteItem(this, event);'><i class='icon-minus'></i></a></div>";
		},
		addItem : function(element, event) {
			var $editor_body = $(element).parents(".modal-body");
			var $item = $(element).parents(".list-item");
			$editor_body.find(".new-item-btn").remove();
			if ($(element).hasClass("new-item-btn")) {
				$editor_body.append($.listeditor.buildNewItem(""));
				$editor_body.find(".list-item-input:first").focus();
			} else {
				$item.after($.listeditor.buildNewItem(""));
				$item.next(".list-item").find(".list-item-input").focus();
			}
			event.preventDefault();
		},
		deleteItem : function(element, event) {
			var $editor_body = $(element).parents(".modal-body");
			var $item = $(element).parents(".list-item");
			$item.remove();
			if ($editor_body.find(".list-item").length == 0) {
				$editor_body.html("<a class='new-item-btn' href='#' onclick='$.listeditor.addItem(this, event);'><i class='icon-plus'></i></a>");
			}
			event.preventDefault();
		},
		moveUpItem : function(element, event) {
			var $item = $(element).parents(".list-item");
			var $prev = $item.prev(".list-item");
			if ($prev.length > 0) {
				$item.insertBefore($prev);
			}
			event.preventDefault();
		},
		moveDownItem : function(element, event) {
			var $item = $(element).parents(".list-item");
			var $next = $item.next(".list-item");
			if ($next.length > 0) {
				$item.insertAfter($next);
			}
			event.preventDefault();
		}
	}

})(jQuery);
