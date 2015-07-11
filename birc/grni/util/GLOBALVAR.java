package birc.grni.util;

import java.io.File;

public class GLOBALVAR {
	
	public static final String dependentLibsFolder = "/deplibs";	/* folder of dependent libraries used by our own libs*/
	public static final String nativeLibFolder = "/nativelib";		/* folder of native library file(.dll, .so, and so on)*/
	public static final String resourceFolder = "/resource";		/* folder of all resources, for example, pictures*/
	public static final String resourcePictureFolder = "picture";	/* folder of picture resources, used together with "resourceFolder" variable*/
	public static final String tmpDir = "MetaGRNTmp";
	public static final String simulationNetworkTmpDir = GLOBALVAR.tmpDir+ File.separator + "simulationNetwork";
}
