package com.bh.toolkit.file;

import java.io.File;
import java.util.ArrayList;

public final class FileRenameUtils {
	public interface IRename {
		void rename(String sourcePath, String... args);

		void rename(ArrayList<String> list, String... args);
	}

	private static boolean renameTo(String path, String oldname, String newname) {
		boolean result = false;
		if (path == null || oldname == null || newname == null
				|| oldname.equals(newname)) {
			return result;
		}

		return new File(path, oldname).renameTo(new File(path, newname));
	}

	public static abstract class IRenameImpl implements IRename {
		@Override
		public void rename(String sourcePath, String... args) {
			ArrayList<String> list = new ArrayList<String>();

			FileFilterImpl fileFilterImpl = new FileFilterImpl()
					.acceptDir(false).acceptFile(true).recursive(true);
			if (ListFileUtils.availableProcessors() <= 1) {
				ListFileUtils.listFiles(new File(sourcePath), fileFilterImpl,
						true, list);
			} else {
				ArrayList<File> listTmp;
				listTmp = ListFileUtils.listFiles(new File(sourcePath),
						fileFilterImpl, true);
				for (File file : listTmp) {
					list.add(file.getAbsolutePath());
				}
			}

			rename(list, args);
		}

		@Override
		public void rename(ArrayList<String> list, String... args) {
			File file;
			String fileName, fileNameWithoutSuffix, suffix;
			for (int i = 0; i < list.size(); i++) {
				file = new File(list.get(i));
				fileName = file.getName();
				fileNameWithoutSuffix = FileUtils.getNameWithoutSuffix(file
						.getAbsolutePath());
				suffix = FileUtils.getSuffix(file.getAbsolutePath());
				if (fileNameWithoutSuffix != null) {
					renameTo(file.getParent(), fileName,
							newName(fileNameWithoutSuffix, suffix, args));
				}
			}
		}

		protected abstract String newName(String fileNameWithoutSuffix,
				String suffix, String... args);
	}

	public static class IRenameImplToLowerCase extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			fileNameWithoutSuffix = fileNameWithoutSuffix.toLowerCase();
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToUpperCase extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			fileNameWithoutSuffix = fileNameWithoutSuffix.toUpperCase();
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToAddPrefix extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			if (args == null || args.length == 0) {
				return null;
			}

			fileNameWithoutSuffix = args[0] + fileNameWithoutSuffix;
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToAddSuffix extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			if (args == null || args.length == 0) {
				return null;
			}

			fileNameWithoutSuffix = fileNameWithoutSuffix + args[0];
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToDeletePrefix extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			if (args == null || args.length == 0) {
				return null;
			}

			if (fileNameWithoutSuffix.startsWith(args[0])
					&& fileNameWithoutSuffix.length() > args[0].length()) {
				fileNameWithoutSuffix = fileNameWithoutSuffix.substring(args[0]
						.length());
			}
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToDeleteSuffix extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			if (args == null || args.length == 0) {
				return null;
			}

			if (fileNameWithoutSuffix.endsWith(args[0])
					&& fileNameWithoutSuffix.length() > args[0].length()) {
				fileNameWithoutSuffix = fileNameWithoutSuffix.substring(0,
						fileNameWithoutSuffix.length() - args[0].length());
			}
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}

	public static class IRenameImplToModifyDotSuffix extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			return (args != null && args.length != 0 && args[0].trim().length() != 0) ? (fileNameWithoutSuffix
					+ "." + args[0])
					: (fileNameWithoutSuffix);
		}
	}

	// public static class IRenameImplToRemoveSpace extends IRenameImpl {
	// @Override
	// protected String newName(String fileNameWithoutSuffix, String suffix,
	// String... args) {
	// if (1 == 0) {
	// fileNameWithoutSuffix = fileNameWithoutSuffix.replaceAll(" +",
	// "");
	// return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
	// : (fileNameWithoutSuffix);
	// } else {
	// return new IRenameImplToReplace().newName(
	// fileNameWithoutSuffix, suffix, " +", "");
	// }
	// }
	// }

	public static class IRenameImplToReplace extends IRenameImpl {
		@Override
		protected String newName(String fileNameWithoutSuffix, String suffix,
				String... args) {
			if (args == null || args.length < 2) {
				return null;
			}

			fileNameWithoutSuffix = fileNameWithoutSuffix.replaceAll(args[0],
					args[1]);
			return suffix != null ? (fileNameWithoutSuffix + "." + suffix)
					: (fileNameWithoutSuffix);
		}
	}
}