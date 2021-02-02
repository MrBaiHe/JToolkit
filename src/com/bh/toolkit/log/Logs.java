package com.bh.toolkit.log;

import java.util.ArrayList;
import java.util.List;

public class Logs {
	public enum EnPriority {
		enPriorityVerbose, //
		enPriorityDebug, //
		enPriorityInfo, //
		enPriorityWarn, //
		enPriorityError, //
		enPriorityNone; //

		@Override
		public String toString() {
			if (this.compareTo(enPriorityVerbose) == 0) {
				return "V";
			} else if (this.compareTo(enPriorityDebug) == 0) {
				return "D";
			} else if (this.compareTo(enPriorityInfo) == 0) {
				return "I";
			} else if (this.compareTo(enPriorityWarn) == 0) {
				return "W";
			} else if (this.compareTo(enPriorityError) == 0) {
				return "E";
			} else if (this.compareTo(enPriorityNone) == 0) {
				return "N";
			}

			return super.toString();
		}
	}

	// not thread safe!
	private static List<ILog> ILogs = new ArrayList();

	public static void addILog(ILog ilog) {
		if (ilog == null || ILogs.contains(ilog)) {
			return;
		}

		ILogs.add(ilog);
	}

	public static void removeILog(ILog ilog) {
		if (ilog == null || !ILogs.contains(ilog)) {
			return;
		}

		ILogs.remove(ilog);
	}

	public static void clearILog() {
		ILogs.clear();
	}

	private static <T> void logpts(EnPriority enPriority, String tag,
			boolean stack, T... messages) {
		for (ILog ilog : ILogs) {
			ilog.log(enPriority, tag, stack, messages);
		}
	}

	public static <T> void log(EnPriority enPriority, String tag, T... messages) {
		logpts(enPriority, tag, false, messages);
	}

	public static <T> void v(T... messages) {
		logpts(EnPriority.enPriorityVerbose, null, false, messages);
	}

	public static <T> void d(T... messages) {
		logpts(EnPriority.enPriorityDebug, null, false, messages);
	}

	public static <T> void i(T... messages) {
		logpts(EnPriority.enPriorityInfo, null, false, messages);
	}

	public static <T> void w(T... messages) {
		logpts(EnPriority.enPriorityWarn, null, false, messages);
	}

	public static <T> void e(T... messages) {
		logpts(EnPriority.enPriorityError, null, false, messages);
	}

	public static <T> void s(T... messages) {
		logpts(EnPriority.enPriorityVerbose, null, true, messages);
	}

	public static void limit(EnPriority enPriority) {
		for (ILog ilog : ILogs) {
			ilog.limit(enPriority);
		}
	}

	public static void up(int step) {
		for (ILog ilog : ILogs) {
			ilog.up(step);
		}
	}
}