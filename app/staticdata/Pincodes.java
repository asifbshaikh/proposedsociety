package staticdata;

import java.util.Collection;

public interface  Pincodes{
	Collection<String> getStates();
	Collection<String> getDistricts(String state);
	Collection<String> getTalukas(String state, String district);
	Collection<String> getPincodes(String state, String district, String taluka);
}