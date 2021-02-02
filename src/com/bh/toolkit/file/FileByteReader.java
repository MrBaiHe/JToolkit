package com.bh.toolkit.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class FileByteReader {
	private FileInputStream mFileInputStream;

	public boolean open(String fileName) {
		if (mFileInputStream == null) {
			try {
				mFileInputStream = new FileInputStream(fileName);
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public byte[] read(int nRead) {
		if (mFileInputStream == null || nRead < 1) {
			return null;
		}

		try {
			byte[] bys = new byte[nRead];
			int len = mFileInputStream.read(bys);
			if (len == nRead) {
				return bys;
			} else if (len > 0 && len < nRead) {
				return Arrays.copyOfRange(bys, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int read(byte[] buffer) {
		int ret = -1;
		if (mFileInputStream == null || buffer == null) {
			return ret;
		}

		try {
			return mFileInputStream.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public long skip(long byteCount) {
		int ret = -1;
		if (mFileInputStream == null) {
			return ret;
		}

		try {
			return mFileInputStream.skip(byteCount);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public void close() {
		if (mFileInputStream != null) {
			FileUtils.close(mFileInputStream);
			mFileInputStream = null;
		}
	}
}