package org.kneelawk.cursefeedlist;

import java.math.BigInteger;

import com.google.common.primitives.UnsignedLong;

public class UnsignedDownloadProgressFilter extends ProgressFilter implements ProgressListener {

	public UnsignedDownloadProgressFilter(ProgressListener listener, long offset, long length) {
		super(listener, offset, length);
	}

	@Override
	public void progress(long current, long max) {
		// the download uncertainty part
		if (max == -1) {
			// max is meaningless so we can't interpret current
			listener.progress(offset + length / 2, offset + length);
		} else {
			BigInteger bic = UnsignedLong.valueOf(current).bigIntegerValue();
			BigInteger bim = UnsignedLong.valueOf(max).bigIntegerValue();
			long newCurrent = bic.multiply(BigInteger.valueOf(length)).divide(bim).longValue();
			listener.progress(offset + newCurrent, offset + length);
		}
	}

}
