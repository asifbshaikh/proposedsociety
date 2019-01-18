package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
public class IdGenerator {
	
	//Note : performance issue ,can be fixed by using ThreadLocal
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
	
	public enum Prefix{
		AGENT("AG"),
		INTERNAL_OFFICER("IO"),
		MEDIATOR("MA"),
		DIRECT_APPLICATION("DA"),
		APPLICATION_THROUGH_AGENT("AA");
		
		private String prefix;
		
		Prefix(String prefix){
			this.prefix = prefix ;
		}
		
		public String getPrefix(){
			return this.prefix;
		}
	}
	
	public  String generate(long id ,Prefix p){
		Date currentdate = new Date();
		return (p.getPrefix() + simpleDateFormat.format(currentdate) + id);
	}
}
