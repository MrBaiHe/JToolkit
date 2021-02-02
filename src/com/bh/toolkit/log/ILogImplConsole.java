package com.bh.toolkit.log;

import com.bh.toolkit.log.Logs.EnPriority;

public final class ILogImplConsole extends ILogImpl {
	private static final boolean ONLY_MESSAGE = 1 == 0;

	@Override
	protected void out(EnPriority enPriority, String tag, String message) {
		console(ONLY_MESSAGE ? message : (enPriority.toString() + "\t")
				+ (tag != null ? (tag + " :") : "") + message + "#");
	}

	public static void console(String message) {
		System.out.println(message);
	}
}