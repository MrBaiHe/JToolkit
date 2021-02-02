package com.bh.toolkit.log;

import java.util.ArrayList;

import com.bh.toolkit.DateTime;
import com.bh.toolkit.log.Logs.EnPriority;

public abstract class ILogImpl implements ILog {
	private static final String PREFIX = "ILog***";
	private static final String STACK_SEPARATOR = "\n";
	protected EnPriority mLimitPriority = Logs.EnPriority.enPriorityVerbose;
	private byte[] mLock = new byte[0];
	private int mUpStep = 0;

	@Override
	public <T> void log(EnPriority enPriority, String tag, boolean stack,
			T... messages) {
		if (enPriority.compareTo(mLimitPriority) < 0) {
			return;
		}

		ArrayList<StackTraceElement> es = getStackTraceElementList(mUpStep);
		if (es == null || es.size() == 0) {
			stack = false;
		} else {
			if (tag == null || tag.length() == 0) {
				tag = getPrefixInfo(es.get(0));
			}
		}

		synchronized (mLock) {
			if (!stack) {
				out(enPriority, tag, ToString.toStringS(messages));
			} else {
				out(enPriority, tag,
						getStackInfo(es) + "#" + ToString.toStringS(messages));
			}
		}
	}

	@Override
	public void limit(EnPriority enPriority) {
		mLimitPriority = enPriority;
	}

	@Override
	public void up(int step) {
		mUpStep = step;
	}

	protected abstract void out(EnPriority enPriority, String tag,
			String message);

	private static ArrayList<StackTraceElement> getStackTraceElementList(
			int upStep) {
		ArrayList<StackTraceElement> es = new ArrayList<StackTraceElement>();
		StackTraceElement[] stackTraceElements = Thread.currentThread()
				.getStackTrace();
		if (stackTraceElements == null) {
			return es;
		}

		StackTraceElement e = null;
		int i;
		for (i = 0; i < stackTraceElements.length; i++) {
			if (e == null) {
				if (stackTraceElements[i].getClassName().equals(
						Logs.class.getName())) {
					e = stackTraceElements[i];
				}
			} else {
				if (!stackTraceElements[i].getClassName().equals(
						Logs.class.getName())) {
					e = stackTraceElements[i];
					break;
				}
			}
		}
		if (e == null || e.getClassName().equals(Logs.class.getName())) {
			return es;
		}

		i += upStep;
		for (; i < stackTraceElements.length; i++) {
			es.add(stackTraceElements[i]);
		}
		return es;
	}

	protected static String getStackTraceElementClassName(StackTraceElement e) {
		if (e == null) {
			return null;
		}

		String className = e.getClassName();
		if (className != null) {
			String[] classNames = className.split("\\.");
			if (classNames != null && classNames.length != 0) {
				className = classNames[classNames.length - 1];
			}
		}
		return className;
	}

	protected static String getPrefixInfo(StackTraceElement e) {
		if (e == null) {
			return null;
		}

		return String.format("%s %s|%s|%s|%d|%d-%s|", DateTime
				.toString_DateTime("/", " ", ":"), PREFIX,
				getStackTraceElementClassName(e), e.getMethodName(), e
						.getLineNumber(), Thread.currentThread().getId(),
				Thread.currentThread().getName());
	}

	protected static String getStackInfo(ArrayList<StackTraceElement> es) {
		StringBuilder builder = new StringBuilder();
		if (es == null) {
			return builder.toString();
		}

		StackTraceElement e;
		for (int i = 0; i < es.size(); i++) {
			e = es.get(i);
			if (builder.length() != 0) {
				builder.append(STACK_SEPARATOR);
			}
			builder.append(String.format("|%s|%s|%d|",
					getStackTraceElementClassName(e), e.getMethodName(),
					e.getLineNumber()));
		}

		return builder.toString();
	}
}