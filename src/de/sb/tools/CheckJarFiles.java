package de.sb.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;

public class CheckJarFiles {
	
	private static boolean correct = true;
	private static int count = 0;
	private static List<String> invalidFiles = new ArrayList<>();

	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("No directory given!");
			return;
		}

		String pathname = args[0];

		File dir = new File(pathname);

		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			checkFiles(files);
		}
		
		if (correct) {
			System.out.println("All " + count + " files are correct!");
		}
		else {
			System.out.println("Following files are not valid zip files: ");
			for (String s : invalidFiles) {
				System.out.println(s);
			}
		}
	}

	private static void checkFiles(File[] files) {
		for (File file : files) {
			
			if (!file.isDirectory() && 
					(FilenameUtils.getExtension(file.getName()).equals("jar")
							|| FilenameUtils.getExtension(file.getName()).equals("zip")))
				
				try (ZipFile zipFile = new ZipFile(file))
				{
					Enumeration<? extends ZipEntry> e = zipFile.entries();
					while(e.hasMoreElements()) {
						ZipEntry entry = e.nextElement();
						entry.toString();
					}
					count++;
					System.out.println(file.getName() + " is ok");
				}
				catch(Exception ex) {
					ex.printStackTrace();
					invalidFiles.add(file.getPath());
					System.out.println("Invalid File: " + file.getName());
					correct = false;
				}

			else if (file.isDirectory()) {
				checkFiles(file.listFiles());
			}
		}

	}
}
