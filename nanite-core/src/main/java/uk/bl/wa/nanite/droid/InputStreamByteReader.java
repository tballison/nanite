/**
 * 
 */
package uk.bl.wa.nanite.droid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import net.byteseek.io.reader.AbstractReader;
import net.byteseek.io.reader.cache.NoCache;
import net.byteseek.io.reader.windows.HardWindow;
import net.byteseek.io.reader.windows.Window;

/**
 * A ByteReader wrapped around an InputStream, using a large buffer to simulate
 * random access.
 * 
 * TODO Shift to some kind of file-backed stream buffer,
 * {@see CachedSeekableStream} or {@see FileCacheSeekableStream}.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class InputStreamByteReader extends AbstractReader {
    private static Logger log = Logger.getLogger(InputStreamByteReader.class);

    private long nextpos = 0;
    private InputStream in = null;
    static int BUFFER_SIZE = 10 * 1024 * 1024; // Items larger than this likely
                                               // to fail identification;

    /**
     * @param in
     *            Force use of a CloseShieldInputStream so we can safely dispose
     *            of any buffers we create
     */
    public InputStreamByteReader(InputStream in) {
        super(BUFFER_SIZE, NoCache.NO_CACHE);
        this.in = in;
        // Set up a large buffer for the input stream, wrapping for random
        // access if mark/reset are not supported:
        if (!this.in.markSupported()) {
            this.in = new BufferedInputStream(in, BUFFER_SIZE);
        }
        // The 'reset' logic will fail if the buffer is not big enough.
        this.in.mark(BUFFER_SIZE);
        this.nextpos = 0;
    }

    @Override
    public int readByte(long position) {
        // System.err.println("@" + nextpos + " Reading " + position);
        try {
            // If skipping back, reset then skip forward:
            if (position < this.nextpos) {
                // System.out.println("@"+nextpos+"Reset and skip to
                // "+position);
                in.reset();
                in.skip(position);
            } else if (position > this.nextpos) {
                // System.out.println("@"+nextpos+"Skipping to "+position);
                in.skip(position - this.nextpos);
            }
            int b = in.read();
            // System.out.println("Got byte: "+ Integer.toHexString(0xFF & b) );
            // Increment the internal position, unless EOF?:
            this.nextpos = position + 1;
            if (b == -1)
                this.nextpos = position;
            // System.out.println("NOW @"+nextpos+"\n");
            // Return the byte:
            return (byte) b;
        } catch (IOException e) {
            log.error("IOException in readByte: " + e);
            e.printStackTrace();
            log.error("Throwing as RuntimeException...");
            throw new RuntimeException(e);
        }
    }

    InputStream getInputStream() {
        return this.in;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        // Close buffers
        if (this.in != null) {
            this.in.close();
        }
        // Discard the buffer:
        this.in = null;
    }

    @Override
    public long length() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected Window createWindow(long windowStart) throws IOException {
        byte[] bytes = IOUtils.toByteArray(this.in);
        return windowStart == 0 ? new HardWindow(bytes, 0, bytes.length) : null;
    }

}
