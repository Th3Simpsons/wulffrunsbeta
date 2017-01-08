package de.lab4inf.wrb.rest;

import static java.lang.String.format;

import java.net.URLEncoder;

/**
 * 
 * @author sossenbrink
 * @since 07.01.2017
 */
public class WRBClient extends AbstractWRBClient {
	/**
	 * @param hostUrl
	 */
	public WRBClient(String hostUrl) {
		super(hostUrl);
	}

	

	public String getDifferentiation(String fct) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/differentiate?fct=%s", fct);
		return submitRequest(path);
	}

	public String getDifferentiation(String fct, String def) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/differentiate?fct=%s&def=%s", fct, escapeHTTP(def));
		return submitRequest(path);
	}

	public String getDifferentiation(String fct, String def, String fmt) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/differentiate?fct=%s&def=%s&fmt=%s", escapeHTTP(fct), escapeHTTP(def),
				escapeHTTP(fmt));
		return submitRequest(path);
	}

	//////////////////////////////////////////////////////////////

	public String getIntegration(String fct) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/integrate?fct=%s", fct);
		return submitRequest(path);
	}

	public String getIntegration(String fct, String def) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/integrate?fct=%s&def=%s", fct, escapeHTTP(def));
		return submitRequest(path);
	}

	public String getIntegration(String fct, String def, String fmt) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/integrate?fct=%s&def=%s&fmt=%s", escapeHTTP(fct), escapeHTTP(def),
				escapeHTTP(fmt));
		return submitRequest(path);
	}
	
	
}
