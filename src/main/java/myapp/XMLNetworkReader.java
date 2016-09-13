// This software is released into the Public Domain.  See copying.txt for details.
package myapp;

import org.openstreetmap.osmosis.core.OsmosisRuntimeException;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionActivator;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.common.SaxParserFactory;
import org.openstreetmap.osmosis.xml.v0_6.impl.OsmHandler;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * An OSM data source reading from an xml file. The entire contents of the file
 * are read.
 * 
 * @author Brett Henderson
 */
public class XMLNetworkReader implements RunnableSource {
	
	private static Logger log = Logger.getLogger(XMLNetworkReader.class.getName());
	
	private Sink sink;
	private InputStream is;
	private boolean enableDateParsing;
	private CompressionMethod compressionMethod;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param is
	 *            The inputstream to read.
	 * @param enableDateParsing
	 *            If true, dates will be parsed from xml data, else the current
	 *            date will be used thus saving parsing time.
	 * @param compressionMethod
	 *            Specifies the compression method to employ.
	 */
	public XMLNetworkReader(InputStream is, boolean enableDateParsing, CompressionMethod compressionMethod) {
		this.is = is;
		this.enableDateParsing = enableDateParsing;
		this.compressionMethod = compressionMethod;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setSink(Sink sink) {
		this.sink = sink;
	}
	
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void run() {
		InputStream inputStream = null;
		
		try {
			SAXParser parser;
			
			sink.initialize(Collections.<String, Object>emptyMap());

			inputStream =
				new CompressionActivator(compressionMethod).
					createCompressionInputStream(is);

			parser = SaxParserFactory.createParser();
			log.log(Level.INFO, "JHjhJ");
			parser.parse(inputStream, new OsmHandler(sink, enableDateParsing));
			
			sink.complete();
			
		} catch (SAXParseException e) {
// 			throw new OsmosisRuntimeException(
// 				"Unable to parse xml file " + file
// 				+ ".  publicId=(" + e.getPublicId()
// 				+ "), systemId=(" + e.getSystemId()
// 				+ "), lineNumber=" + e.getLineNumber()
// 				+ ", columnNumber=" + e.getColumnNumber() + ".",
// 				e);
		} catch (SAXException e) {
			throw new OsmosisRuntimeException("Unable to parse XML.");
		} catch (IOException e) {
//			throw new OsmosisRuntimeException("Unable to read XML file " + file + ".");
		} finally {
			sink.release();
			
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.log(Level.SEVERE, "Unable to close input stream.", e);
				}
				inputStream = null;
			}
		}
	}
}