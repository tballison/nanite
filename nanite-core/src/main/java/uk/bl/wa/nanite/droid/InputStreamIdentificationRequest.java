/**
 * 
 */
package uk.bl.wa.nanite.droid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.CloseShieldInputStream;

import net.byteseek.io.reader.InputStreamReader;
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
    private InputStreamReader isReader;
    private String fileName;
    private String extension;
    private RequestMetaData metaData;
    private RequestIdentifier identifier;
    private int size;
	
	public InputStreamIdentificationRequest(RequestMetaData metaData,
            RequestIdentifier identifier, InputStream in) throws IOException {
		this.metaData = metaData;
        this.fileName = metaData.getName();
        this.extension = ResourceUtils.getExtension(fileName);
		this.identifier = identifier;
		try {
			this.size = in.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Init the reader:
        this.open(in);
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getByte(long)
	 */
	@Override
    public byte getByte(long position) throws IOException {
        return (byte) this.isReader.readByte(position);
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
        return this.in;
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
        this.in = new CloseShieldInputStream(in);
        this.isReader = new InputStreamReader(this.in);
    }

    @Override
    public RequestIdentifier getIdentifier() {
        // TODO Auto-generated method stub
        return null;
    }
	
	
}
