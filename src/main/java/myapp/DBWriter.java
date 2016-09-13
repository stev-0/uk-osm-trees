// This software is released into the Public Domain.  See copying.txt for details.
package myapp;

import java.io.BufferedWriter;
import java.io.File;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import java.util.Map;
import java.util.Collection;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.DateTime;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;


/**
 * An OSM data sink for storing all data to an xml file.
 * 
 * @author Stephen Knox
 */
public class DBWriter implements Sink {

	private static Logger log = Logger.getLogger(DBWriter.class.getName());

		Datastore datastore = DatastoreOptions.defaultInstance().service();
		KeyFactory keyFactory = datastore.newKeyFactory().kind("keyKind");
		Key key = keyFactory.newKey("keyName");
	

	public void process(EntityContainer entityContainer) {
		
		log.log(Level.INFO, "what a bor");

		Collection<Tag> tags = entityContainer.getEntity().getTags();
		for (Tag tag: tags) {
			Entity entity = Entity.builder(key)
		    .set("key", tag.getKey())
		    .set("value", tag.getValue())
		    .set("access_time", DateTime.now())
		    .build();
		datastore.put(entity);
		}
		
	}

	public void initialize(Map<String, Object> metaData) {
	}

	public void complete() {
	}
	
	
	/**
	 * Cleans up any open file handles.
	 */
	public void close() {
	}

	public void release() {
	}
	
}