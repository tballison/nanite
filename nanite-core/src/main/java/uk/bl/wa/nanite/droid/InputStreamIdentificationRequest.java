/**
 * 
 */
package uk.bl.wa.nanite.droid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.CloseShieldInputStream;

import net.byteseek.io.reader.WindowReader;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.RequestIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;
import uk.gov.nationalarchives.droid.core.interfaces.resource.ResourceUtils;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class InputStreamIdentificationRequest
        implements IdentificationRequest<InputStream> {

    private InputStream in;
    private InputStreamByteReader isReader;
    private String fileName;
    private String extension;
    private RequestMetaData metaData;
    private RequestIdentifier identifier;
    private long size;
	
	public InputStreamIdentificationRequest(RequestMetaData metaData,
            RequestIdentifier identifier, long size) throws IOException {
		this.metaData = metaData;
        this.fileName = metaData.getName();
        this.extension = ResourceUtils.getExtension(fileName);
		this.identifier = identifier;
        this.size = size;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getByte(long)
	 */
	@Override
    public byte getByte(long position) throws IOException {
        int val = this.isReader.readByte(position);
        // System.err.println("Reading " + val + " at " + position);
        return (byte) val;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#close()
	 */
	@Override
	public void close() throws IOException {
        this.isReader.close();
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getSourceInputStream()
	 */
	@Override
	public InputStream getSourceInputStream() throws IOException {
		InputStream in = this.isReader.getInputStream();
	    in.reset();
		return in;
	}

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public long size() {
        return this.size;
    }

    @Override
    public String getExtension() {
        return this.extension;
    }

    @Override
    public RequestMetaData getRequestMetaData() {
        return this.metaData;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// Shut down any buffering:
        this.isReader = null;
	}

    @Override
    public WindowReader getWindowReader() {
        return this.isReader;
    }

    @Override
    public void open(InputStream bytesource) throws IOException {
        // System.err.println("Opening... " + bytesource);
        this.in = new CloseShieldInputStream(bytesource);
        this.isReader = new InputStreamByteReader(this.in);
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return this.identifier;
    }
	
	
}
