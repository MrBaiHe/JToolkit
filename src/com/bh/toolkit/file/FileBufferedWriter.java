package com.bh.toolkit.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class FileBufferedWriter {
	private BufferedWriter mBufferedWriter;

	// Charset StandardCharsets.UTF_8;
	public boolean open(String fileName, String charset, boolean append) {
		if (mBufferedWriter == null) {
			try {
				FileUtils.createFileIfNeed(fileName);
				mBufferedWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(fileName, append), charset));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public void write(String str) {
		try {
			mBufferedWriter.write(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if (mBufferedWriter != null) {
			FileUtils.close(mBufferedWriter);
			mBufferedWriter = null;
		}
	}
}