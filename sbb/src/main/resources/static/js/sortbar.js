$(document).ready(function(){
	sortbarActiveChange();
});

function sortbarActiveChange(){
	$('.sort').click(function(){
		$('.sort').not(this).removeClass('active');
	    $(this).toggleClass('active');
	})
}