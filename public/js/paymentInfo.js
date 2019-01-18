$(document).ready(function(){
	$("#paymentMode").trigger('byCheque');
	$("#accountNumber").val('');
	$("#transactionId").val('');
	$("#receiptNumber").val('');
	$("#chequeNumber").val('');
	
	$("#chequeTable").hide();
	$("#cardsTable").hide();
	$("#depositeTable").hide();
	var paymentMode = $("#paymentMode").val();
	
	if(paymentMode == 'byCheque'){
		$("#chequeTable").show();
		$("#accountNumber").val('0');
		$("#transactionId").val('0');
		$("#receiptNumber").val('0');
		
	}
	if(paymentMode == 'ByNetBanking'){
		$("#cardsTable").show();
		$("#chequeNumber").val('0');
		$("#receiptNumber").val('0');
	}
	if(paymentMode =='ByCreditOrDebitCards'){
		 $("#cardsTable").show();
		 $("#chequeNumber").val('0');
		 $("#receiptNumber").val('0');
	}
	if(paymentMode == 'ByDeposit'){
		$("#depositeTable").show();
		$("#chequeNumber").val('0');
		$("#accountNumber").val('0');
		$("#transactionId").val('0');
	
	}
	
	$("#paymentMode").change(function(){
		

		$("#accountNumber").val('');
		$("#transactionId").val('');
		$("#receiptNumber").val('');
		$("#chequeNumber").val('');
		
		$("#chequeTable").hide();
		$("#cardsTable").hide();
		$("#depositeTable").hide();
		var paymentMode = $("#paymentMode").val();
		
		if(paymentMode == 'byCheque'){
			$("#chequeTable").show();
			$("#accountNumber").val('0');
			$("#transactionId").val('0');
			$("#receiptNumber").val('0');
			
		}
		if(paymentMode == 'ByNetBanking'){
			$("#cardsTable").show();
			$("#chequeNumber").val('0');
			$("#receiptNumber").val('0');
		}
		if(paymentMode =='ByCreditOrDebitCards'){
			 $("#cardsTable").show();
			 $("#chequeNumber").val('0');
			 $("#receiptNumber").val('0');
		}
		if(paymentMode == 'ByDeposit'){
			$("#depositeTable").show();
			$("#chequeNumber").val('0');
			$("#accountNumber").val('0');
			$("#transactionId").val('0');
		
		}
	});
	
});


