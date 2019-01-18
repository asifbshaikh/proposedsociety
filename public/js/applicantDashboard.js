var rowIndex = undefined;
function getReferDiv() {
	$('#dynamicReferInputDiv').show();
	$('#mobile').val('');
	$('#email').val('');
	$('#name').val('');
	$('#addMoreButton').hide();
	$('#referFriendLink').hide();
}

function cancelInputRow() {
	$('#dynamicReferInputDiv').hide();
	if($('#dynamicReferTableDiv').is(":visible") == true){
		$('#addMoreButton').show();
	}else{
		$('#addMoreButton').hide();
		$('#referFriendLink').show();
	}
}

function addMoreRow() {
	$('#dynamicReferInputDiv').show();
	$('#mobile').val('');
	$('#email').val('');
	$('#name').val('');
	$('#addMoreButton').hide();
}

function saveRow(name, mobile, email) {
	var mobile = $('#mobile').val();
	var email = $('#email').val();
	var name = $('#name').val();
	
	if(validateInputRefer(name, mobile, email)){
		$('#dynamicReferTableDiv').show();
		var tbody = $("#dynamicReferTableBody");
		tbody.append($('<tr>')
		        .append($('<td>'+ name +'</td>'+'<td>'+ mobile +'</td>'+'<td>'+ email +'</td>'+'<td>'+ '<input type="button" class="btn btn-mini btn-danger pull-right" onclick="deleteRow(this)" value="X" style="font-weight: bold;">'+'</td>'+'<td>'+ '<input type="button" class="btn btn-mini btn-info" onclick="editRow(this)" value="Edit" style="font-weight: bold;">' +'</td>')
		        )
	    	);
		$('#mobile').val('');
		$('#email').val('');
		$('#name').val('');
		$('#showInputError').hide();
	}
}

function updateRow() {
	var mobile = $('#mobile').val();
	var email = $('#email').val();
	var name = $('#name').val();
	
	if(validateInputRefer(name, mobile, email)){
		$('#mobile').val('');
		$('#email').val('');
		$('#name').val('');
		$('#showInputError').hide();
		$('#updateButton').hide();
		$('#saveButton').show();
		var x=document.getElementById('dynamicReferTable').rows;
	    var y=x[rowIndex].cells;
	    y[0].innerHTML= name;
	    y[1].innerHTML= mobile;
	    y[2].innerHTML= email;
	}
}

var tableToObj = function( table ) {
    var tableRows = table.rows,
        tableLength = tableRows.length,
        i = 0,
        j = 0,
        keys = [],
        obj, ret = [];

    for (; i < tableLength; i++) {
        if (i == 0) {
            for (; j < tableRows[i].children.length; j++) {
                keys.push(tableRows[i].children[j].innerHTML);
            }
        } else {
            obj = {};
            for (j = 0; j < tableRows[i].children.length; j++) {
                obj[keys[j]] = tableRows[i].children[j].innerHTML;
            }
            ret.push(obj);
        }
    }
    
    return ret;
};

function submitReferData(tableId) {
	var tableData = tableToObj(document.getElementById(tableId));
	$.ajax({
		url: '/applicant/dashboard/referAFriend',
		method: 'POST',
		dataType: 'html',
		data: JSON.stringify(tableData),
		contentType:'application/json',
		async: false,
		success: function(response) {
			var selectedRows = $("#dynamicReferTable").children('tbody').find('tr');
		    if (selectedRows.length){ 
		        selectedRows.remove(); 
		    }
			$('#dynamicReferTableDiv').hide();
			$('#dynamicReferInputDiv').hide();
			$('#addMoreButton').hide();
			$('#referFriendLink').show();
		},
		error: function(error) {
		}
	});
}

function validateInputRefer(name, mobile, email) {
	
	if(!mobile && !email){
		$('#showInputError').show().text("Both mobile number & email id can't be empty");
		return false;
	}
	if(mobile && !ValidateMobile(mobile) && !email){
		$('#showInputError').show().text("Enter valid mobile number");
		return false;
    }
	if(email && !ValidateEmail(email) && !mobile) {
		$('#showInputError').show().text("Enter valid email id");
		return false;
    }
	if(mobile && !ValidateMobile(mobile) && email && !ValidateEmail(email)) {
		$('#showInputError').show().text("Enter valid mobile number & email id");
		return false;
    }
	if(mobile && ValidateMobile(mobile) && email && !ValidateEmail(email)){
		$('#showInputError').show().text("Enter valid email id");
		return false;
    }
	if(mobile && !ValidateMobile(mobile) && email && ValidateEmail(email)) {
		$('#showInputError').show().text("Enter valid mobile number");
		return false;
    }
	return true;
}

function ValidateEmail(email) {
	    var expr = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	    return expr.test(email);
}

function ValidateMobile(mobile) {
    var expr = /^[1-9]{1}[0-9]{9}$/;
    return expr.test(mobile);
}

function deleteRow(r) {
    var i = r.parentNode.parentNode.rowIndex;
    document.getElementById("dynamicReferTable").deleteRow(i);
    var selectedRows = $("#dynamicReferTable").children('tbody').find('tr');
    if (!selectedRows.length){ 
    	$('#dynamicReferTableDiv').hide(); 
    	$('#addMoreButton').hide();
    	if($('#dynamicReferInputDiv').is(":visible") == false){
    		$('#referFriendLink').show();
    	}else{
    		$('#updateButton').hide();
        	$('#saveButton').show();
    	}
    }
}

function editRow(r) { 
	$('#updateButton').show();
	$('#saveButton').hide();
	rowIndex = r.parentNode.parentNode.rowIndex;
	$('#name').val($(r).closest('tr').find('td:nth-child(1)').text());
	$('#mobile').val($(r).closest('tr').find('td:nth-child(2)').text());
	$('#email').val($(r).closest('tr').find('td:nth-child(3)').text());
	
}


function sendQuerySubmit() {
	var queryText = $('#queryText').val();
	if(!queryText){
		$('#queryEmptyError').show();
		$('#querySent').hide();
	}else{
		$('#queryEmptyError').hide();
		var queryData = { 
	            'queryText':	queryText,
	    };
		
		$.ajax({
			url: '/applicant/dashboard/sendQuery',
			method: 'POST',
			dataType: 'html',
			data: JSON.stringify(queryData),
			contentType:'application/json',
			async: false,
			success: function(response) {
				$('#querySent').show();
				$('#queryText').val('');
			},
			error: function(error) {
				$('#querySent').hide();
			}
		});
	}
}