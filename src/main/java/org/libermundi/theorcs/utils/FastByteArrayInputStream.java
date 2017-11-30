package org.libermundi.theorcs.utils;
import java.io.InputStream;

/**
 * ByteArrayInputStream implementation that does not synchronize methods.
 */
public class FastByteArrayInputStream extends InputStream {
    /**
     * Our byte buffer
     */
    protected byte[] buf = null;

    /**
     * Number of bytes that we can read from the buffer
     */
    protected int count = 0;

    /**
     * Number of bytes that have been read from the buffer
     */
    protected int pos = 0;

    public FastByteArrayInputStream(byte[] buf, int count) {
        this.buf = buf.clone();
        this.count = count;
    }

    @Override
    public final int available() {
        return count - pos;
    }

    @Override
    public final int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }

    @Override
    public final int read(byte[] b, int off, int len) {
    	int lenght = len;
        if (pos >= count) {
        	return -1;
        }

        if ((pos + lenght) > count){
        	lenght = (count - pos);
        }

        System.arraycopy(buf, pos, b, off, lenght);
        pos += lenght;
        return lenght;
    }

    @Override
    public final long skip(long n) {
    	long _n =n;
        if ((pos + n) > count) {
        	_n = count - pos;
        }
        if (n < 0){
        	return 0;
        }
        pos += _n;
        return _n;
    }

}