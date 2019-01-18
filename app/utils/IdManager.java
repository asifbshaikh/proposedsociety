package utils;

import utils.IdGenerator.Prefix;

public interface IdManager {
	
	public enum IdType{
		 RECEIPT_ID("receiptId"),
		 FORM_NUMBER("formNumber");
		 String type;
		 
		 IdType(String str){
			 type = str;
		 }
		 
		 public String getName(){
			 return type;
		 }
	}
	
	Long generateReceiptNumber(IdType idType);
	String generateFormNumber(IdType idType, Prefix prefix);
}
