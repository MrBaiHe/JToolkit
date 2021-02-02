package com.bh.toolkit.log;

import com.bh.toolkit.log.Logs.EnPriority;

public interface ILog {
	<T> void log(EnPriority enPriority, String tag, boolean stack,
			T... messages);

	void limit(EnPriority enPriority);

	void up(int step);
}