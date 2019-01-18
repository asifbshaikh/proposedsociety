package birt;

public class ReportEngineException extends Exception{

	private static final long serialVersionUID = 1L;

	public ReportEngineException(String msg){
		super(msg);
	}
	
	public ReportEngineException(Throwable t){
		super(t);
	}
	
	public ReportEngineException( String msg, Throwable t){
		super(msg,t);
	}
}
