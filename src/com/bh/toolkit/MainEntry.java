package com.bh.toolkit;

import com.bh.toolkit.log.ILogImplConsole;
import com.bh.toolkit.log.ILogImplLogcat;
import com.bh.toolkit.log.Logs;

public class MainEntry {
	public static void main(String[] args) {
		Logs.clearILog();
		Logs.addILog(new ILogImplConsole());
		Logs.addILog(new ILogImplLogcat());
		// Logs.addILog(ILogImplFile.getInstance(null));

		Logs.v();
		Logs.d();
		Logs.i();
		Logs.w();
		Logs.e();
		Logs.s();
	}
}