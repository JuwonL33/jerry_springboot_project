$(document).ready(function(){
	sidebarActiveChange();
});

function sidebarActiveChange(){
	console.log("들어오니?")
	let pageUrl = window.location.href;
	if (pageUrl.indexOf('qna') > -1) {

		$("#free").removeClass("active");
		$("#qna").addClass("active");
	} else if (pageUrl.indexOf('free') > -1) {
		$("#qna").removeClass("active");
		$("#free").addClass("active");
	} else {
		$("#qna").removeClass("active");
		$("#free").removeClass("active");
	}
}