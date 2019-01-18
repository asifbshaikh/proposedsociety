package staticdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public final class InMemoryPinCodes implements Pincodes {
	private static final Logger LOG = LoggerFactory.getLogger(InMemoryPinCodes.class);
	private static final String PINCODES_CSV = "all_india_pin_code.csv";
	private final Map<String, Map<String, Map<String,Map<String,Collection<String>>>>> states = new TreeMap<String, Map<String, Map<String,Map<String,Collection<String>>>>>(); 

	public InMemoryPinCodes() {
		URL pincodeCsvUrl = InMemoryPinCodes.class.getClassLoader().getResource(PINCODES_CSV);
		
		if (pincodeCsvUrl == null) {
			throw new RuntimeException("Failed to find file: " + PINCODES_CSV + " on classpath.");
		}
		
		// Parse file here.
		parse(pincodeCsvUrl);
	}

	private void parse(URL pincodeCsvUrl) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(pincodeCsvUrl.openStream()));
			String [] nextLine;
	
			// Note: This is to skip the header row.
			reader.readNext();
			
			while ((nextLine = reader.readNext()) != null) {
			
				String state = nextLine[9];
				String district = nextLine[8];
				String taluka = nextLine[7];
				String pin = nextLine[1];
				String locality = nextLine[0];
		
				if (taluka == null || taluka.trim().length()  == 0) {
				    taluka = district;
				}
				
				Map<String, Map<String,Map<String,Collection<String>>>> districts = states.get(state);
				if(districts == null){
					districts = new TreeMap<String, Map<String,Map<String,Collection<String>>>>();
					
					if (LOG.isTraceEnabled()) {
						LOG.trace("Registering state: " + state);
					}
					
					states.put(state, districts);
				}
				
				Map<String,Map<String,Collection<String>>> talukas = districts.get(district);
				if(talukas == null){
					talukas = new TreeMap<String,Map<String,Collection<String>>>();

					if (LOG.isTraceEnabled()) {
						LOG.trace("Registering district: " + district);
					}
					
					districts.put(district, talukas);
				}
					
				Map<String,Collection<String>> pincodes = talukas.get(taluka);
				if(pincodes == null){
					pincodes = new TreeMap<String,Collection<String>>();

					if (LOG.isTraceEnabled()) {
						LOG.trace("Registering taluka: " + taluka);
					}
				
					talukas.put(taluka, pincodes);
				}
						
				Collection<String> localities = pincodes.get(pin);
				if(localities == null){
					localities =  new ArrayList<String>();
					
					if (LOG.isTraceEnabled()) {
						LOG.trace("Registering Pincode: " + localities);	
					}

					pincodes.put(pin+", "+locality, localities);
				}
				
				localities.add(locality); //Location under pincode is added 
			}
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed loading pin codes", e);
		} catch (IOException e) {
			throw new RuntimeException("Failed loading pin codes", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.warn("Failure closing the reader", e);
				} 
			}
		}		
	}
	
	
	
	@Override
	public Collection<String> getStates() {
		return Collections.unmodifiableCollection(states.keySet());
	}

	@Override
	public Collection<String> getDistricts(String state) {
		Map<String, Map<String,Map<String,Collection<String>>>> districts = states.get(state);
		if (districts == null) {
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableCollection(districts.keySet());
	}

	@Override
	public Collection<String> getTalukas(String state, String district) {
		Map<String, Map<String,Map<String,Collection<String>>>> districts = states.get(state);
		if (districts == null) {
			return Collections.emptyList();
		}
		
		Map<String,Map<String,Collection<String>>> talukas = districts.get(district);
		if(talukas == null){
			return Collections.emptyList();
		} 

		return Collections.unmodifiableCollection(talukas.keySet());
	}

	@Override
	public Collection<String> getPincodes(String state, String district, String taluka) {
		Map<String, Map<String, Map<String,Collection<String>>>> districts = states.get(state);
		if (districts == null) {
			return Collections.emptyList();
		}
		
		Map<String, Map<String,Collection<String>>> talukas = districts.get(district);
		if(talukas == null){
			return Collections.emptyList();
		}
		
		Map<String,Collection<String>> pincodes = talukas.get(taluka);
		if(pincodes == null){
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableCollection(pincodes.keySet());
	}
	
}
