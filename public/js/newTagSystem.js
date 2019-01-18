$(document).ready(function(){

											/*All Vars are initialised here:*/

	//1.Variables for all Div-containing-tags. 
	var $tagDiv_LocationNearTo = $('#locationNearToTagDiv');
	var $tagDiv_ExternalAmenities = $('#externalAmenitiesTagDiv');
	var $tagDiv_InternalAmenities = $('#internalAmenitiesTagDiv');
	
	//2.Variables for all paragraph in Div-containing-tags.
	var $tagDiv_P_LocationNearTo = $('#locationNearToTagDiv p');
	var $tagDiv_P_ExternalAmenities = $('#externalAmenitiesTagDiv p');
	var $tagDiv_P_InternalAmenities = $('#internalAmenitiesTagDiv p');
	
	//3.Variables for all Div-containing-inputs.
	var $inputDiv_LocationNearTo = $('#locationInputs');
	var $inputDiv_ExternalAmenities = $('#externalInputs');
	var $inputDiv_InternalAmenities = $('#internalInputs');
	
	//4.Variables for all hidden-fields.
	var $hidden_LocationNearTo = $('#nearbyHidden');
	var $hidden_ExternalAmenities = $('#externalAmenitiesHidden');
	var $hidden_InternalAmenities = $('#internalAmenitiesHidden');

	//5.Variables for all drop-downs.
	var $drpDwn_LocationNearTo = $('#locationNearTo');
	var $drpDwn_ExternalAmenities = $('#externalAmenities');
	var $drpDwn_InternalAmenities = $('#internalAmenities');
	
	//6.Variables for all user entered tagTexts when drop down selection = "Other".
	var $otherText_LocationNearTo = $('#OtherTextForLoc');
	var $otherText_ExternalAmenities = $('#OtherTextForExt');
	var $otherText_InternalAmenities = $('#OtherTextForInt');

											/*Show or Hide On pageLoad*/
	$tagDiv_P_LocationNearTo.show();
	$tagDiv_P_ExternalAmenities.show();
	$tagDiv_P_InternalAmenities.show();
	
	$inputDiv_LocationNearTo.hide();
	$inputDiv_ExternalAmenities.hide();
	$inputDiv_InternalAmenities.hide();
	
	tagCreater($hidden_LocationNearTo,$tagDiv_LocationNearTo,"spanLabelForLoc",$tagDiv_P_LocationNearTo);
	tagCreater($hidden_ExternalAmenities,$tagDiv_ExternalAmenities,"spanLabelForExt",$tagDiv_P_ExternalAmenities);
	tagCreater($hidden_InternalAmenities,$tagDiv_InternalAmenities,"spanLabelForInt",$tagDiv_P_InternalAmenities);
	
	//7.Variables for all deleteSpan.
	var $deleteSpan_LocationNearTo = $("#locationNearToTagDiv > span > span.tag-delete");
	var $deleteSpan_ExternalAmenities = $("#externalAmenitiesTagDiv > span > span.tag-delete");
	var $deleteSpan_InternalAmenities = $("#internalAmenitiesTagDiv > span > span.tag-delete");
	
	deleteOnClick($deleteSpan_LocationNearTo);
	deleteOnClick($deleteSpan_ExternalAmenities);
	deleteOnClick($deleteSpan_InternalAmenities);
	
	var tem_tagValLocNear = new String();
	
	$drpDwn_LocationNearTo.change(function(){
		$tagDiv_P_LocationNearTo.hide();
        var tagValLocNear = $('#locationNearTo option:selected').text();
        var dropDownValLocNear2 = $drpDwn_LocationNearTo.val();
        
        if(tem_tagValLocNear.search(dropDownValLocNear2) == -1){
        	tem_tagValLocNear = tem_tagValLocNear.concat(dropDownValLocNear2);
        	if(dropDownValLocNear2 == "Other"){
            	$('#locationInputs').show();
            }else if(dropDownValLocNear2 == ""){
            	//do nothing for now
            }
            else {
            	spanForTagCreater($tagDiv_LocationNearTo,"spanLabelForLoc",tagValLocNear);
        		$('#locationInputs').hide();
            }
        }
        
        $("#locationNearToTagDiv > span:last-child > span.tag-delete").click(function(){
            $(this).parent().remove();        
        });
	});
	
	$otherText_LocationNearTo.click(function(){
		var tagValLocNear = $('#locationNearTo option:selected').text();
		var otherTextForNearBy = $('#ifOtherTextForNearBy').val();
		if(tagValLocNear == "Other"){
			if(otherTextForNearBy.length > 0){
				//$tagDiv_LocationNearTo.append('<span><span class="spanLabelForLoc">' + otherTextForNearBy + '</span></span>');
				spanForTagCreater($tagDiv_LocationNearTo,"spanLabelForLoc",otherTextForNearBy);
				$('#ifOtherTextForNearBy').val("");
			}
		}
		$("#locationNearToTagDiv > span:last-child > span.tag-delete").click(function(){
			$(this).parent().remove();      
        });
	});
	
	var tem_drpDwn_ExternalAmenities = new String();
	
	$drpDwn_ExternalAmenities.change(function(){
		$tagDiv_P_ExternalAmenities.hide();
        var tagValExtAm = $('#externalAmenities option:selected').text();
        var dropDownValExtAm2 = $drpDwn_ExternalAmenities.val();
        if(tem_drpDwn_ExternalAmenities.search(dropDownValExtAm2) == -1){
        	tem_drpDwn_ExternalAmenities = tem_drpDwn_ExternalAmenities.concat(dropDownValExtAm2);
        	if(dropDownValExtAm2 == "Other"){
        		$('#externalInputs').show();
        	}else if(dropDownValExtAm2 == ""){
           	//do nothing for now
        	}else{
        		spanForTagCreater($tagDiv_ExternalAmenities,"spanLabelForExt",tagValExtAm);
        		$('#externalInputs').hide();
        	}
        }
     
        $("#externalAmenitiesTagDiv > span:last-child > span.tag-delete").click(function(){
        	$(this).parent().remove();        
        });
	});

	$otherText_ExternalAmenities.click(function(){
    	var otherTextForExtAmnts = $('#ifOtherTextForExtAmnts').val();
    	var tagValExtAm = $('#externalAmenities option:selected').text();
   		if(tagValExtAm == "Other"){
   			if(otherTextForExtAmnts.length > 0){
   				spanForTagCreater($tagDiv_ExternalAmenities,"spanLabelForExt",otherTextForExtAmnts);
    			$('#ifOtherTextForExtAmnts').val("");
    		}
    	}
   		$("#externalAmenitiesTagDiv > span:last-child > span.tag-delete").click(function(){
   			$(this).parent().remove();        
        });
    });
	
	var tem_drpDwn_InternalAmenities = new String();
	$drpDwn_InternalAmenities.change(function(){
		$tagDiv_P_InternalAmenities.hide();
		var tagValIntAm = $('#internalAmenities option:selected').text();
        var dropDownValIntAm2 = $drpDwn_InternalAmenities.val();
        if(tem_drpDwn_InternalAmenities.search(dropDownValIntAm2) == -1 ){
        	tem_drpDwn_InternalAmenities = tem_drpDwn_InternalAmenities.concat(dropDownValIntAm2);
            if(dropDownValIntAm2 == "Other"){
      	        $('#internalInputs').show();
             }else if(dropDownValIntAm2 == ""){
              	//do nothing for now.
             }else{
              	spanForTagCreater($tagDiv_InternalAmenities,"spanLabelForInt",tagValIntAm);
              	$('#internalInputs').hide();
             }
        }
        
      
        
        $("#internalAmenitiesTagDiv > span:last-child > span.tag-delete").click(function(){
        	$(this).parent().remove();        
        });
	});

	$otherText_InternalAmenities.click(function(){
    	var otherTextForIntAmnts = $('#ifOtherTextForIntAmnts').val();
    	var tagValIntAm = $('#internalAmenities option:selected').text();
    	if(tagValIntAm == "Other"){
	    	if(otherTextForIntAmnts.length > 0){
	    		spanForTagCreater($tagDiv_InternalAmenities,"spanLabelForInt",otherTextForIntAmnts);
	     		$('#ifOtherTextForIntAmnts').val("");
	     	}
    	}
    	$("#internalAmenitiesTagDiv > span:last-child > span.tag-delete").click(function(){
    		$(this).parent().remove();        
        });
    });
	
	$('#submitShortForm').click(function (event) {
		
		var $tagTextSpan_LocationNearTo = $('#locationNearToTagDiv > span > span.spanLabelForLoc');
		var $tagTextSpan_ExternalAmenities = $("#externalAmenitiesTagDiv > span > span.spanLabelForExt");
		var $tagTextSpan_InternalAmenities = $("#internalAmenitiesTagDiv > span > span.spanLabelForInt");
		
		setHiddenFieldVals($tagTextSpan_LocationNearTo,$hidden_LocationNearTo);
		setHiddenFieldVals($tagTextSpan_ExternalAmenities,$hidden_ExternalAmenities);
		setHiddenFieldVals($tagTextSpan_InternalAmenities,$hidden_InternalAmenities);
		//following function is called to all get location values entered so far into Location Table and it is written in requirement.js
		getAllValuesOfAllLocations();
	});
});

function tagCreater(hiddenVal,tagDiv,spanClassName,ParaInTagDiv) {
    if(hiddenVal.val() != ""){
    	if (typeof hiddenVal.val() == 'string'){
			var array = hiddenVal.val().split("|");
			$.each(array,function(i){
				spanForTagCreater(tagDiv,spanClassName,array[i]);
			});
    	}
		ParaInTagDiv.hide();
	}
}

function spanForTagCreater(tagDiv,spanClassName,tagText) {
	
	//Handling duplicate span value 
	var spans = tagDiv.find('span');
	var test = 0;
	if(spans.length > 0){
		spans.each(
				function(){
					if($(this).text() == tagText){
						test = 1;
						return ;
					}
				}
		);
		
		if(test == 1){
			return ;
		}else{
			tagDiv.append('<span><span class=' + spanClassName + '>' + tagText + '</span><span class="tag-delete"></span></span>');
		}
	}else{
		tagDiv.append('<span><span class=' + spanClassName + '>' + tagText + '</span><span class="tag-delete"></span></span>');
	}
}

function setHiddenFieldVals($tagTextSpan,$hiddenField){
	var textCsv = "";
	$tagTextSpan.each(function(index) {
		var LoctnSpanVal = $(this).text();
		if(index > 0){
			textCsv = textCsv + "|" + $(this).text();
		}else{
			textCsv = textCsv + $(this).text();
		}
	});
	//alert("textCsv: "+textCsv);
	$hiddenField.val(textCsv);
}

function deleteOnClick(x){
    x.click(function(){
        $(this).parent().remove();        
    });
}