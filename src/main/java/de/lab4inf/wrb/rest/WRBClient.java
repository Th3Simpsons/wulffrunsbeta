package de.lab4inf.wrb.rest;

import static java.lang.String.format;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author sossenbrink
 * @since 07.01.2017
 */
public class WRBClient extends AbstractWRBClient {
	/**
	 * @param hostUrl
	 */

	String previousDefinition = "";

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
		String path = format("WRBService/differentiate?fct=%s&def=%s", fct, escapeHTTP(previousDefinition + def));
		return submitRequest(path);
	}

	public String getDifferentiation(String fct, String def, String fmt) throws Exception {
		fct = URLEncoder.encode(fct, "UTF-8");
		String path = format("WRBService/differentiate?fct=%s&def=%s&fmt=%s", escapeHTTP(fct),
				escapeHTTP(previousDefinition + def), escapeHTTP(fmt));
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

	public List<Double> getXYValue(String fctName, String definition, String fmt) throws Exception {
		String fct;

		fct = URLEncoder.encode(fctName, "UTF-8");

		String path = format("WRBService/evaluate?fct=%s&def=%s&fmt=%s", escapeHTTP(fct), escapeHTTP(definition),
				escapeHTTP(fmt));
		String array = submitRequest(path);

		// Parse to List
		List<Double> retVal = new ArrayList<Double>();
		array = array.substring(array.indexOf('[') + 1);
		while (array.contains(",")) {
			String val = array.substring(0, array.indexOf(','));
			retVal.add(Double.parseDouble(val));
			array = array.substring(array.indexOf(',') + 1);
		}
		if (array.contains("]")) {
			String val = array.substring(0, array.indexOf(']'));
			retVal.add(Double.parseDouble(val));
		}
		try {
			String val = array;
			retVal.add(Double.parseDouble(val));
		} catch (Exception e) {
		}

		return retVal;

	}

	public Set<String> getFunctionNames() throws Exception {
		String path = format("WRBService/fctNames");
		String ss = submitRequest(path);

		Set<String> retVal = new HashSet<String>();
		ss = ss.substring(ss.indexOf("[")+1);
		ss = ss.substring(0, ss.indexOf("]"));
		String[] split = ss.split(",");
		for (String f : split) {
			retVal.add(f.replace(",", ""));

		}

		return retVal;

	}

	public void addFunction(String fctName, String body) {

		previousDefinition += body + ";";

	}

	public List<Double> getValues(String fctName) throws Exception {

		return getXYValue(fctName, previousDefinition, "%.6f");
	}

	public double differentiate(String fctName, double x) throws NumberFormatException, Exception {
		return Double.parseDouble(this.getDifferentiation(fctName, "x=" + x));

	}

	public void setXmin(double xmin) {
		previousDefinition += "xmin=" + xmin + ";";
	}

	public void setXmax(double xmax) {
		previousDefinition += "xmax=" + xmax + ";";

	}

	public void setDeltaX(double dx) {
		previousDefinition += "dx=" + dx + ";";

	}

	public double integrate(String fctName, double a, double b) throws Exception {
		return Double.parseDouble(this.getIntegration(fctName, previousDefinition + "a=" + a + "; b=" + b + ";"));

	}

}
