package common;

import java.util.HashMap;
import java.util.Map;

public class ValidationHelper {
	private ValidationHelper(){}
	private static final Map<String, String> errorCodesToGenricError = new HashMap<String, String>();

	static {
		errorCodesToGenricError.put("error.required", "This field is required");
	}

	// TODO: This will in some near future return localized generic error messages.
	public static String translateErrorCode(String code) {
		return errorCodesToGenricError.get(code);
	}
}