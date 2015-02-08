package birc.grni.util;

import java.util.*;

public class InputData {
	private final ArrayList<String> timePoints;		/* time point*/
	private final ArrayList<String> geneNames;	/* gene name*/
	private final ArrayList<ArrayList<Double>> data;
	
	public InputData(ArrayList<String> rowHeader, ArrayList<String> columnHeader, ArrayList<ArrayList<Double>> data) {
		this.timePoints = rowHeader;
		this.geneNames = columnHeader;
		this.data = data;
	}
	
	public ArrayList<String> getTimePoints() {
		return timePoints;
	}

	public ArrayList<String> getGeneNames() {
		return geneNames;
	}

	public ArrayList<ArrayList<Double>> getData() {
		return data;
	}
}
