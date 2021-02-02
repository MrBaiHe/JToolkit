package com.bh.toolkit.log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ToString {
	public static <T> String toStringS(T... ts) {
		if (ts == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		for (T t : ts) {
			if (builder.length() != 0) {
				builder.append(" ");
			}
			builder.append(toString(t));
			if (ts.length > 1) {
				builder.append("#");
			}
		}

		return builder.toString();
	}

	// T can be base type,base type array,class object,class object
	// array,collection,other...
	public static <T> String toString(T t) {
		if (t == null) {
			return null;
		}

		if (t instanceof byte[]) {
			return toString((byte[]) t);
		} else if (t instanceof char[]) {
			return toString((char[]) t);
		} else if (t instanceof int[]) {
			return toString((int[]) t);
		} else if (t instanceof double[]) {
			return toString((double[]) t);
		} else if (t instanceof Object[]) {
			return toString((Object[]) t);
		} else if (t instanceof List<?>) {
			return toString((List<?>) t);
		} else if (t instanceof Map<?, ?>) {
			return toString((Map<?, ?>) t);
		} else if (t instanceof HashSet<?>) {
			return toString((HashSet<?>) t);
		} else if (t instanceof Byte) {
			return toString((Byte) t);
		} else if (t instanceof Integer) {
			return toString((Integer) t);
		} else {
			return t.toString();
		}
	}

	public static String toString(Object[] objs) {
		return toString(objs, null);
	}

	public static String toString(Object[] objs, String seperator) {
		if (objs == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", objs.length));
		for (int i = 0; i < objs.length; i++) {
			if (seperator != null && builder.length() != 0) {
				builder.append(seperator);
			}
			builder.append(String.format("%s;",
					objs[i] != null ? objs[i].toString() : "null"));
		}
		return builder.toString();
	}

	public static String toString(byte[] data) {
		if (data == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", data.length));
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				builder.append(" ");
			}
			builder.append(String.format("%02X", data[i]));
		}
		return builder.toString();
	}

	public static String toString(byte[] data, int offset, int len) {
		if (data == null || offset < 0 || offset >= data.length) {
			return null;
		}

		int end = offset + len;
		if (len == -1 || end > data.length) {
			end = data.length;
		}

		byte[] buf = new byte[end - offset];
		System.arraycopy(data, offset, buf, 0, end - offset);
		return toString(buf);
	}

	public static String toString(char[] data) {
		if (data == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", data.length));
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				builder.append(" ");
			}
			builder.append(String.format("%04X", data[i] & 0x0000FFFF));
		}
		return builder.toString();
	}

	public static String toString(int[] data) {
		if (data == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", data.length));
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				builder.append(" ");
			}
			builder.append(String.format("%d[0x%08X]", data[i], data[i]));// %08X
		}
		return builder.toString();
	}

	public static String toString(double[] data) {
		if (data == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", data.length));
		for (int i = 0; i < data.length; i++) {
			if (i > 0) {
				builder.append(" ");
			}
			builder.append(String.format("%f", data[i]));
		}
		return builder.toString();
	}

	public static String toString(List<?> list) {
		if (list == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:\n", list.size()));
		int size = list.size();
		for (int i = 0; i < size; i++) {
			builder.append(size);
			builder.append("/");
			builder.append(i);
			builder.append(":");
			builder.append(list.get(i).toString());
			builder.append(i != size - 1 ? "; " : ". ");
			if (i != size - 1) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	public static String toString(Map<?, ?> hashMap) {
		if (hashMap == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", hashMap.size()));
		Iterator it = hashMap.entrySet().iterator();
		Map.Entry entry;
		while (it.hasNext()) {
			entry = (Map.Entry) it.next();
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(toString(entry.getValue()));
			builder.append("; ");
		}
		return builder.toString();
	}

	public static String toString(Set<?> set) {
		if (set == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d:", set.size()));
		Iterator it = set.iterator();
		while (it.hasNext()) {
			builder.append(it.next());
			builder.append("; ");
		}
		return builder.toString();
	}

	public static String toString(Byte b) {
		return String.format("0x%X=%d=%d", b.byteValue() & 0x000000FF,
				(int) b.byteValue(), b.byteValue() & 0x000000FF);
	}

	public static String toString(Integer i) {
		return String.format("%d[0x%X]", i.intValue(), i.intValue());
	}

	public static <T> String repeat(T t) {
		return repeat(t, 8);
	}

	public static <T> String repeat(T t, int repeatcnt) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < repeatcnt; i++) {
			builder.append(t);
		}
		return builder.toString();
	}
}