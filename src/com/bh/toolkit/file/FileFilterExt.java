package com.bh.toolkit.file;

import java.io.File;
import java.io.FileFilter;

public interface FileFilterExt extends FileFilter {

	boolean acceptDir(File file);

	boolean acceptFile(File file);

	boolean recursiveDir(File file);
}