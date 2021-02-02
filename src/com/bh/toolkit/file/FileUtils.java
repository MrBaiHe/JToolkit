package com.bh.toolkit.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

public class FileUtils {
	public static final String SEPARATOR_LINUX = "/";
	public static final String SEPARATOR_WINDOWS = "\\";
	private static String SEPARATOR = System.getProperty("file.separator");

	public static String getSeparator() {
		return SEPARATOR;
	}

	public static void setSeparator(String separator) {
		SEPARATOR = separator;
	}

	public static void setSeparatorAsLinux() {
		SEPARATOR = SEPARATOR_LINUX;
	}

	public static void setSeparatorAsWindows() {
		SEPARATOR = SEPARATOR_WINDOWS;
	}

	public static boolean isFile(String absPath) {
		return new File(absPath).isFile();
	}

	public static boolean isDir(String absPath) {
		return new File(absPath).isDirectory();
	}

	public static String getPath(String dir, String name) {
		if (dir == null || name == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(dir);
		if (!dir.endsWith(SEPARATOR)) {
			builder.append(SEPARATOR);
		}
		builder.append(name);
		return builder.toString();
	}

	private static String _getParentPath(String absPathOfFOD,
			boolean withSuffixSeparator) {
		if (absPathOfFOD.endsWith(SEPARATOR)) {
			absPathOfFOD = absPathOfFOD.substring(0, absPathOfFOD.length() - 1);
		}
		int index = absPathOfFOD.lastIndexOf(SEPARATOR);
		if (index == -1) {
			return null;
		}

		return absPathOfFOD.substring(0, withSuffixSeparator ? (index + 1)
				: index);
	}

	public static String getParentPathWithSuffixSeparator(String absPathOfFOD) {
		return _getParentPath(absPathOfFOD, true);
	}

	public static String getParentPath(String absPathOfFOD) {
		return _getParentPath(absPathOfFOD, false);
	}

	public static String getName(String absPathOfFOD) {
		if (absPathOfFOD.endsWith(SEPARATOR)) {
			absPathOfFOD = absPathOfFOD.substring(0, absPathOfFOD.length() - 1);
		}
		int index = absPathOfFOD.lastIndexOf(SEPARATOR);
		if (index == -1) {
			return null;
		}

		return absPathOfFOD.substring(index + 1);
	}

	public static String getNameWithoutSuffix(String absPathOfFOD) {
		absPathOfFOD = getName(absPathOfFOD);
		int end = absPathOfFOD.lastIndexOf(".");
		return absPathOfFOD.substring(0, end == -1 ? absPathOfFOD.length()
				: end);
	}

	public static String getSuffix(String absPathOfFile) {
		int begin = absPathOfFile.lastIndexOf(".");
		if (begin == -1) {
			return null;
		}

		return absPathOfFile.substring(begin + 1);
	}

	public static String getSuffixWithDot(String absPathOfFile) {
		int begin = absPathOfFile.lastIndexOf(".");
		if (begin == -1) {
			return null;
		}

		return absPathOfFile.substring(begin);
	}

	public static Date getLastModifiedTime(String fileName) {
		return getLastModifiedTime(new File(fileName));
	}

	public static Date getLastModifiedTime(File file) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(file.lastModified());
		return calendar.getTime();
	}

	public static int getSteps(String absPath) {
		int step = 0;
		int fromIndex = -1;
		while ((fromIndex = absPath.indexOf(SEPARATOR, fromIndex + 1)) != -1) {
			++step;
		}
		return step;
	}

	// 0:path1 = path2
	// 1:path1 is parent path of path2
	// -1:path2 is parent path of path1
	// other:no parent relation
	public static int pathCompare(final String dir1, final String dir2) {
		if (dir1 == null || dir1.length() == 0 || dir2 == null
				|| dir2.length() == 0) {
			return -2;
		}

		String pathTmp1 = dir1;
		String pathTmp2 = dir2;
		if (!pathTmp1.endsWith(SEPARATOR)) {
			pathTmp1 = pathTmp1 + SEPARATOR;
		}
		if (!pathTmp2.endsWith(SEPARATOR)) {
			pathTmp2 = pathTmp2 + SEPARATOR;
		}
		if (pathTmp1.equals(pathTmp2)) {
			return 0;
		} else if (pathTmp2.startsWith(pathTmp1)) {
			return 1;
		} else if (pathTmp1.startsWith(pathTmp2)) {
			return -1;
		} else {
			return -2;
		}
	}

	public static int pathCompare2(final String dir1EndsWithSeparator,
			final String dir2EndsWithSeparator) {
		if (dir1EndsWithSeparator == null
				|| dir1EndsWithSeparator.length() == 0
				|| dir2EndsWithSeparator == null
				|| dir2EndsWithSeparator.length() == 0) {
			return -2;
		}
		if (dir1EndsWithSeparator.equals(dir2EndsWithSeparator)) {
			return 0;
		} else if (dir2EndsWithSeparator.startsWith(dir1EndsWithSeparator)) {
			return 1;
		} else if (dir1EndsWithSeparator.startsWith(dir2EndsWithSeparator)) {
			return -1;
		} else {
			return -2;
		}
	}

	public static void createFileIfNeed(String absPathOfFile) {
		File file = new File(absPathOfFile);
		if (!file.exists()) {
			createDirIfNeed(getParentPath(absPathOfFile));

			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createDirIfNeed(String strDir) {
		createDirIfNeed(new File(strDir));
	}

	public static void createDirIfNeed(File dir) {
		if (!dir.exists()) {
			File parent = dir.getParentFile();
			if (parent != null && parent.exists()) {
			} else {
				createDirIfNeed(parent);
			}
			dir.mkdir();
		}
	}

	public static void createDirIfNeedF(String absPathOfFile) {
		createDirIfNeed(getParentPath(absPathOfFile));
	}

	public static boolean delete(String absPathOfFOD) {
		return delete(new File(absPathOfFOD));
	}

	public static boolean delete(File file) {
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = delete(new File(file, children[i]));
				if (!success) {
					return false;
				}
			}

			return file.delete();
		} else if (file.isFile()) {
			if (file.exists()) {
				return file.delete();
			}
		}

		return false;
	}

	public static void copyDir(String sourceDir, String destDir,
			boolean isKeepDir) throws IOException {
		File sourceFile = new File(sourceDir);
		createDirIfNeed(isKeepDir ? destDir + sourceFile.separator
				+ sourceFile.getName() : destDir);
		String[] names = sourceFile.list();
		for (int i = 0; i < names.length; i++) {
			File sourceFilex = new File(sourceDir + sourceFile.separator
					+ names[i]);
			String path = destDir
					+ sourceFilex.separator
					+ (isKeepDir ? sourceFile.getName() + sourceFilex.separator
							: "") + names[i];
			if (sourceFilex.isDirectory()) {
				copyDir(sourceFilex.getAbsolutePath(), path, false);
			} else if (sourceFilex.isFile()) {
				copyFile(sourceFilex.getAbsolutePath(),
						destDir
								+ sourceFilex.separator
								+ (isKeepDir ? sourceFile.getName()
										+ sourceFilex.separator : "")
								+ names[i]);
			}
		}
	}

	public static void copyFile(String sourcePath, String destPath)
			throws IOException {
		FileInputStream in = new FileInputStream(new File(sourcePath));
		FileOutputStream out = new FileOutputStream(new File(destPath));
		byte[] bytes = new byte[1024 * 1024];
		int len = 0;
		while ((len = in.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		in.close();
		out.close();
	}

	public static void copy(String sourcePath, String destPath)
			throws IOException {
		File file = new File(sourcePath);
		if (file.isDirectory()) {
			copyDir(sourcePath, destPath, true);
		} else if (file.isFile()) {
			copyFile(sourcePath,
					new File(destPath, getName(sourcePath)).getAbsolutePath());
		}
	}

	public static RandomAccessFile createRandomAccessFile(String absPath,
			boolean createIfNeed) {
		try {
			if (createIfNeed) {
				createFileIfNeed(absPath);
			} else if (!new File(absPath).exists()) {
				return null;
			}

			return new RandomAccessFile(absPath, "rwd");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void releaseRandomAccessFile(RandomAccessFile randomAccessFile) {
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Closeable c) {
		if (c == null) {
			return;
		}

		try {
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}