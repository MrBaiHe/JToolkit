package com.bh.toolkit.log;

import java.io.File;

import com.bh.toolkit.DateTime;
import com.bh.toolkit.file.FileBufferedWriter;
import com.bh.toolkit.log.Logs.EnPriority;

public final class ILogImplFile extends ILogImpl {
	private String mDir;
	private int mFileIndex;
	private String mFileName;
	private File mFile;
	private static final int SINGLE_FILE_MAXLENGHT = 1024 * 1024;
	private static final String DIR = "ILog";
	private static final String SUBDIR = "Default";
	private static final String SUFFIX = ".ilog";
	private static boolean NEWFILE_FIRST = 1 == 1;

	private static ILogImplFile sThis = null;

	private synchronized static ILogImplFile createInstance(String subDirName) {
		if (sThis == null) {
			sThis = new ILogImplFile(subDirName);
		}
		return sThis;
	}

	public static ILogImplFile getInstance(String subDirName) {
		if (sThis == null) {
			createInstance(subDirName);
		}
		return sThis;
	}

	public static void unInstance() {
		sThis = null;
	}

	private ILogImplFile(String subDirName) {
		if (subDirName == null || subDirName.length() == 0) {
			subDirName = SUBDIR;
		}
		mDir = new StringBuilder()
				.append(com.bh.toolkit.Utils.getDesktopPath())
				.append(com.bh.toolkit.Utils.getFileSeparator())
				.append(DIR)
				.append(com.bh.toolkit.Utils.getFileSeparator())
				.append(DateTime.toString_Date(null))
				.append(com.bh.toolkit.Utils.getFileSeparator())
				.append(subDirName != null ? subDirName : "")
				.append(subDirName != null ? com.bh.toolkit.Utils
						.getFileSeparator() : "").toString();

		int fileIndex = 0;
		File file = new File(mDir);
		String[] fileNames = file.exists() ? file.list() : null;
		if (fileNames != null) {
			int index;
			for (String fileName : fileNames) {
				try {
					int end = fileName.lastIndexOf(".");
					fileName = fileName.substring(0,
							end == -1 ? fileName.length() : end);
					index = Integer.valueOf(fileName).intValue();
					if (index > fileIndex) {
						fileIndex = index;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		mFileIndex = fileIndex + 1;
		write(formatFileName(0), String.format("%s:%06d",
				DateTime.toString_DateTime(null, null, null), mFileIndex));
	}

	@Override
	protected void out(EnPriority enPriority, String tag, String message) {
		write((enPriority.toString() + "\t")
				+ (tag != null ? (tag + " :") : "") + message + "#");
	}

	private String formatFileName(int index) {
		return String.format("%s%06d%s", mDir, index, SUFFIX);
	}

	private static void write(String fileName, String message) {
		FileBufferedWriter w = new FileBufferedWriter();
		if (w.open(fileName, "UTF-8", true)) {
			w.write(message);
			w.write("\n");
			w.close();
		}
	}

	private void write(String message) {
		if (mFile != null) {
			if (mFile.exists() && mFile.length() > SINGLE_FILE_MAXLENGHT) {
				mFileName = null;
				mFile = null;
			}
		}

		if (mFileName == null) {
			while (true) {
				mFileName = formatFileName(mFileIndex);
				if (!(mFile = new File(mFileName)).exists()
						|| (!NEWFILE_FIRST && mFile.length() < SINGLE_FILE_MAXLENGHT)) {
					break;
				} else {
					mFileIndex++;
				}
			}
		}

		write(mFileName, message);
	}
}