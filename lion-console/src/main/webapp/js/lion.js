/**************************对String 对象的扩展*********************************/
var Res_Code_Success = 0;
var Res_Code_Error = -1;
var Res_Code_Warn = 1;

/**
 * 在字符串前拼接contextpath 
 */
String.prototype.prependcontext = function() {
	if (typeof contextpath == "undefined") {
		throw new Error("global variable contextpath not found, maybe you not include common.ftl.");
	}
	return contextpath + (this.startsWith("/") ? "" : "/") + this;
}

/**
* 去除两端空格
*/
String.prototype.trim = function() {
	if (this == void 0) {throw new Error("Illegal argument error.");}
	return this.replace(/(^\s+)|(\s+$)/g, "");
}

/**
 * 判断字符串是否为null or empty string
 */
String.prototype.isEmpty = function() {
	if (this == void 0) {throw new Error("Illegal argument error.");}
	return this == null || this.length == 0;
}

/**
 * 判断字符串是否为null or blank string 
 */
String.prototype.isBlank = function() {
	if (this == void 0) {throw new Error("Illegal argument error.");}
	return this == null || this.trim().length == 0;
}

/**
* 判断字符串是否以指定的字符串开始
*/
String.prototype.startsWith = function(str) {
	if (this == void 0) {throw new Error("Illegal argument error.");}
    return this.substr(0, str.length) == str;
}

String.prototype.isNumber = function() {
	if (this == void 0) {throw new Error("Illegal argument error.");}
	return this != null && /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(this);
}






