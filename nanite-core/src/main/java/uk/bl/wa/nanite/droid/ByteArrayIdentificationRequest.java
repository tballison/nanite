/**
 * 
 */
package uk.bl.wa.nanite.droid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.byteseek.io.reader.ByteArrayReader;
import net.byteseek.io.reader.WindowReader;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.RequestIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.resource.RequestMetaData;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class ByteArrayIdentificationRequest
        implements IdentificationRequest<byte[]> {

	protected RequestMetaData metaData;
	protected RequestIdentifier identifier;
	private byte[] data;
	protected int size;

	protected ByteArrayIdentificationRequest() { }
	
	public ByteArrayIdentificationRequest(RequestMetaData metaData,
			RequestIdentifier identifier, byte[] data ) {
		this.metaData = metaData;
		this.identifier = identifier;
		// Set up the byte array based on the 
		this.data = data;
        this.size = data.length;
        //cachedBinary.setSourceFile(null);
	}
	
	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getByte(long)
	 */
	@Override
	public byte getByte(long position) {
        if (position > Integer.MAX_VALUE) {
            throw new RuntimeException(
                    "ByteArrayIdentificationRequest does not support objects > 2GB in size!");
        }
        return data[(int) position];
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getFileName()
	 */
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#size()
	 */
	@Override
	public long size() {
		return size;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getExtension()
	 */
	@Override
	public String getExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getSourceInputStream()
	 */
	@Override
	public InputStream getSourceInputStream() throws IOException {
		return new ByteArrayInputStream(data);
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getRequestMetaData()
	 */
	@Override
	public RequestMetaData getRequestMetaData() {
		return this.metaData;
	}

	/* (non-Javadoc)
	 * @see uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest#getIdentifier()
	 */
	@Override
	public RequestIdentifier getIdentifier() {
		return this.identifier;
	}

    @Override
    public WindowReader getWindowReader() {
        return new ByteArrayReader(this.data);
    }

    @Override
    public void open(byte[] bytesource) throws IOException {
        this.data = bytesource;
    }
	
}
