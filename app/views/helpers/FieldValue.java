package views.helpers;
public class FieldValue {
	 public static String fieldVal( Object field , String defaultVal){
		 if(field != null){
			return(field.toString());
		}else{
			return(defaultVal);
		}
	 }
}