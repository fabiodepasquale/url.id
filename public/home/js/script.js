$('#datetimepicker').datetimepicker({
	minDate: moment().add(1, 'h')
});

$(function () {
	$("[data-toggle='tooltip']").tooltip();
});