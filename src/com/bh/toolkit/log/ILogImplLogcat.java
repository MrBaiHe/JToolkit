package com.bh.toolkit.log;

import com.bh.toolkit.log.Logs.EnPriority;

public final class ILogImplLogcat extends ILogImpl {
	@Override
	protected void out(EnPriority enPriority, String tag, String message) {
		if (message == null || message.length() == 0) {
			message = "#";
		}
		switch (enPriority) {
		case enPriorityVerbose:
			Log.v(tag, message);
			break;

		case enPriorityDebug:
			Log.d(tag, message);
			break;

		case enPriorityInfo:
			Log.i(tag, message);
			break;

		case enPriorityWarn:
			Log.w(tag, message);
			break;

		case enPriorityError:
			Log.e(tag, message);
			break;
		}
	}
}