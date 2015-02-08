package birc.grni.util;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.*;

/**
 * It's a wrapper class of Java 
 * @author liu xingliang
 *
 */
public class Logging {
	
	public final static Logger logger = Logger.getLogger("");	/* global logger*/
	
	static 
	{
		/* create the log file with name of current date and time*/
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		try {
			FileHandler fh = new FileHandler(dateFormat.format(date)+".log", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.INFO);	/* Message of FINE, CONFIG, INFO, WARNING, SEVERE level will be logged*/
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
}
