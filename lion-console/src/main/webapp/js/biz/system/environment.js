$(document).ready(function() {
	$('[data-toggle="modal"]').click(function(e) {
		e.preventDefault();
		var href = $(this).attr('href');
		$.ajax( {
			type : "GET",
			contentType : "application/json",
			url : href.prependcontext(),
			dataType : 'html',
			success : function(result) {
				var temp = result.replace(/&quot;/g, '\"');
				$(temp).modal();
			}
		});
});

});