package com.bh.toolkit.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileBufferedReader {
	private BufferedReader mBufferedReader;

	// Charset StandardCharsets.UTF_8;
	public boolean open(String fileName, String charset) {
		if (mBufferedReader == null) {
			try {
				mBufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(fileName), charset));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public String readLine() {
		try {
			return mBufferedReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void close() {
		if (mBufferedReader != null) {
			FileUtils.close(mBufferedReader);
			mBufferedReader = null;
		}
	}

	public static String readAll(String fileName, String charset) {
		StringBuilder builder = new StringBuilder();
		FileBufferedReader reader = new FileBufferedReader();
		if (reader.open(fileName, charset)) {
			String line;
			while ((line = reader.readLine()) != null && line.length() != 0) {
				builder.append(line);
			}
			reader.close();
		}
		return builder.toString();
	}
}