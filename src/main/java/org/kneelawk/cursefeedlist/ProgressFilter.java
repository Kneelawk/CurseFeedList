package org.kneelawk.cursefeedlist;

import java.math.BigInteger;

public class ProgressFilter implements ProgressListener {

	protected ProgressListener listener;
	protected long offset, length;

	public ProgressFilter(ProgressListener listener, long offset, long length) {
		this.listener = listener;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public void progress(long current, long max) {
		// avoid any overflows due to current * length
		BigInteger bic = BigInteger.valueOf(current);
		long newCurrent = bic.multiply(BigInteger.valueOf(length)).divide(BigInteger.valueOf(max)).longValue();
		listener.progress(offset + newCurrent, offset + length);
	}

}
