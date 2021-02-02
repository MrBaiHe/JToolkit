package com.bh.toolkit.file;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ListFileUtils {
	// if file is dir,root=true,out will not contain file
	// if file is dir,root=false,recursive=true,out will contain file
	public static void listFiles(File file, FileFilterExt fileFilterExt,
			boolean root, ArrayList<String> out) {
		if (file == null || fileFilterExt == null || out == null) {
			return;
		}

		if (file.isDirectory()) {
			if (fileFilterExt.acceptDir(file) && !root) {
				out.add(file.getAbsolutePath());
			}

			if (root || fileFilterExt.recursiveDir(file)) {
				File[] files = file.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						listFiles(files[i], fileFilterExt, false, out);
					}
				}
			}
		} else {
			if (fileFilterExt.acceptFile(file)) {
				out.add(file.getAbsolutePath());
			}
		}
	}

	public static ArrayList<File> listFiles(File file,
			FileFilterExt fileFilterExt, boolean root) {
		ArrayList<File> fileList = new ArrayList(1024 * 100);
		ExecutorService executorService = Executors
				.newFixedThreadPool(availableProcessors());
		try {
			CallableResult callableResult = new CallableImpl(file,
					executorService, fileFilterExt, root).call();
			recursive(callableResult, fileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		fileList.trimToSize();
		return fileList;
	}

	public static int availableProcessors() {
		int n = Runtime.getRuntime().availableProcessors();
		return n <= 1 ? 1 : n;
	}

	private static void recursive(CallableResult callableResult,
			ArrayList<File> fileList) {
		fileList.addAll(callableResult.fileList);
		for (Future<CallableResult> future : callableResult.futureList) {
			try {
				recursive(future.get(), fileList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static class CallableResult {
		public ArrayList<File> fileList;
		public ArrayList<Future<CallableResult>> futureList;

		public CallableResult(ArrayList<File> fileList,
				ArrayList<Future<CallableResult>> futureList) {
			this.fileList = fileList;
			this.futureList = futureList;
		}
	}

	private static class CallableImpl implements Callable<CallableResult> {
		private File mFile;
		private ExecutorService mExecutorService;
		private FileFilterExt mFileFilterExt;
		private boolean mRoot;

		public CallableImpl(File file, ExecutorService executorService,
				FileFilterExt fileFilterExt, boolean root) {
			mFile = file;
			mExecutorService = executorService;
			mFileFilterExt = fileFilterExt;
			mRoot = root;
		}

		public CallableResult call() throws Exception {
			ArrayList<File> fileList = new ArrayList();
			ArrayList<Future<CallableResult>> futureList = new ArrayList<Future<CallableResult>>();

			if (mFile.isDirectory()) {
				if (mFileFilterExt.acceptDir(mFile) && !mRoot) {
					fileList.add(mFile);
				}

				if (mRoot || mFileFilterExt.recursiveDir(mFile)) {
					File[] files = mFile.listFiles();
					if (files != null) {
						for (File file : files) {
							futureList.add(mExecutorService
									.submit(new CallableImpl(file,
											mExecutorService, mFileFilterExt,
											false)));
						}
					}
				}
			} else {
				if (mFileFilterExt.acceptFile(mFile)) {
					fileList.add(mFile);
				}
			}

			fileList.trimToSize();
			futureList.trimToSize();
			return new CallableResult(fileList, futureList);
		}
	}
}