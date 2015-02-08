package birc.grni.util;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.net.*;

public class MyFileUtils
{
	public static boolean copyFile(final File toCopy, final File destFile) 
	{
		try 
		{
			return MyFileUtils.copyStream(new FileInputStream(toCopy), new FileOutputStream(destFile));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyFilesRecusively(final File toCopy, final File destDir) 
	{
		assert destDir.isDirectory();

		if (!toCopy.isDirectory()) 
		{
			File destFile = new File(destDir, toCopy.getName());
			destFile.deleteOnExit();
			return MyFileUtils.copyFile(toCopy, destFile);
		} 
		else 
		{
			//TEST: use destDir instead of newDestDir
			//Q: the purpose of constructing newDestDir
			
			if (!destDir.exists() && !destDir.mkdir()) 
			{
				return false;
			}
			
			for (final File child : toCopy.listFiles()) 
			{
				if (!MyFileUtils.copyFilesRecusively(child, destDir)) 
				{
					return false;
				}
			}
			
//			final File newDestDir = new File(destDir, toCopy.getName());
//			
//			if (!newDestDir.exists() && !newDestDir.mkdir()) 
//			{
//				return false;
//			}
//			
//			for (final File child : toCopy.listFiles()) 
//			{
//				if (!FileUtils.copyFilesRecusively(child, newDestDir)) 
//				{
//					return false;
//				}
//			}
		}
		return true;
	}

	public static boolean copyJarResourcesRecursively(final File destDir, final JarURLConnection jarConnection) throws IOException 
	{
		final JarFile jarFile = jarConnection.getJarFile();

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) 
		{
			final JarEntry entry = e.nextElement();
			if (entry.getName().startsWith(jarConnection.getEntryName())) 
			{
				final String filename = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());

				final File f = new File(destDir, filename);
				if (!entry.isDirectory()) 
				{
					final InputStream entryInputStream = jarFile.getInputStream(entry);
					if (!MyFileUtils.copyStream(entryInputStream, f)) 
					{
						return false;
					}
					entryInputStream.close();
				} 
				else 
				{
					if (!MyFileUtils.ensureDirectoryExists(f)) 
					{
						throw new IOException("Could not create directory: "
								+ f.getAbsolutePath());
					}
				}
			}
		}
		return true;
	}

	public static boolean copyResourcesRecursively(final URL originUrl, final File destination) 
	{
		try 
		{
			final URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection) 
			{
				return MyFileUtils.copyJarResourcesRecursively(destination,
						(JarURLConnection) urlConnection);
			} 
			else 
			{
				return MyFileUtils.copyFilesRecusively(new File(originUrl.getPath()), destination);
			}
		} 
		catch (final IOException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final File f) 
	{
		try 
		{
			return MyFileUtils.copyStream(is, new FileOutputStream(f));
		} 
		catch (final FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final OutputStream os) 
	{
		try 
		{
			final byte[] buf = new byte[1024];

			int len = 0;
			while ((len = is.read(buf)) > 0) 
			{
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
			return true;
		} 
		catch (final IOException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	private static boolean ensureDirectoryExists(final File f) 
	{
		return f.exists() || f.mkdir();
	}
}
