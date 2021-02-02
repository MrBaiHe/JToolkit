package com.bh.toolkit.file;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class FileFilterImpl implements FileFilterExt {
	private boolean mAcceptDir = true;
	private boolean mAcceptFile = true;
	private ArrayList<String> mFilterDir;
	private ArrayList<String> mRegexDir;
	private ArrayList<String> mRegexFile;
	private ArrayList<String> mSuffixFile;
	private boolean mRecursive = false;

	public FileFilterImpl acceptDir(boolean accept) {
		mAcceptDir = accept;
		return this;
	}

	public FileFilterImpl acceptFile(boolean accept) {
		mAcceptFile = accept;
		return this;
	}

	public FileFilterImpl filterDir(ArrayList<String> filterDir_LowerCase) {
		mFilterDir = filterDir_LowerCase;
		return this;
	}

	public FileFilterImpl regexDir(ArrayList<String> regexs) {
		mRegexDir = regexs;
		return this;
	}

	public FileFilterImpl regexFile(ArrayList<String> regexs) {
		mRegexFile = regexs;
		return this;
	}

	public FileFilterImpl suffixFile(ArrayList<String> suffix_LowerCase) {
		mSuffixFile = suffix_LowerCase;
		return this;
	}

	public FileFilterImpl recursive(boolean recursive) {
		mRecursive = recursive;
		return this;
	}

	@Override
	public boolean recursiveDir(File file) {
		if (mFilterDir != null
				&& mFilterDir.contains(file.getName().toLowerCase())) {
			return false;
		}

		return mRecursive;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return acceptDir(file);
		} else {
			return acceptFile(file);
		}
	}

	@Override
	public boolean acceptDir(File file) {
		if (!mAcceptDir
				|| (mFilterDir != null && mFilterDir.contains(file.getName()
						.toLowerCase()))) {
			return false;
		}

		if (mRegexDir != null) {
			for (String regex : mRegexDir) {
				if (Pattern.matches(regex, file.getName())) {
					return true;
				}
			}
			return false;
		}

		return true;
	}

	@Override
	public boolean acceptFile(File file) {
		if (!mAcceptFile) {
			return false;
		}

		if (mRegexFile != null) {
			for (String string : mRegexFile) {
				if (Pattern.matches(string, file.getName())) {

					if (mSuffixFile != null) {
						return _checkSuffix(file.getAbsolutePath());
					}

					return true;
				}
			}
			return false;
		}

		if (mSuffixFile != null) {
			return _checkSuffix(file.getAbsolutePath());
		}

		return true;
	}

	private boolean _checkSuffix(String string) {
		return (string = FileUtils.getSuffixWithDot(string)) != null
				&& mSuffixFile.contains(string.toLowerCase());
	}
}