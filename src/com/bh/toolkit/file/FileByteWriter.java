package com.bh.toolkit.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileByteWriter {
	private FileOutputStream mFileOutputStream;

	public boolean open(String fileName, boolean append) {
		FileUtils.createDirIfNeed(FileUtils.getParentPath(fileName));

		if (mFileOutputStream == null) {
			try {
				mFileOutputStream = new FileOutputStream(fileName, append);
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean write(byte[] bys) {
		return write(bys, 0, bys.length);
	}

	public boolean write(byte[] bys, int start, int len) {
		if (mFileOutputStream == null || bys == null || bys.length < 1) {
			return false;
		}

		try {
			mFileOutputStream.write(bys, start, len);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void close() {
		if (mFileOutputStream != null) {
			FileUtils.close(mFileOutputStream);
			mFileOutputStream = null;
		}
	}
}