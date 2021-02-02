package com.bh.toolkit;

import javax.swing.filechooser.FileSystemView;

public class Utils {
	public static String getDesktopPath() {
		return FileSystemView.getFileSystemView().getHomeDirectory()
				.getAbsolutePath();
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
}