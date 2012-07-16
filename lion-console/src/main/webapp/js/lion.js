/**************************对String 对象的扩展*********************************/
String.prototype.prependcontext = function() {
	if (typeof contextpath == "undefined") {
		throw new Error("global variable contextpath not found, maybe you not include common.ftl.");
	}
	return contextpath + (this.startsWith("/") ? "" : "/") + this;
}

/**
* 判断字符串是否以指定的字符串开始
*/
String.prototype.startsWith = function(str) {
    return this.substr(0, str.length) == str;
}

