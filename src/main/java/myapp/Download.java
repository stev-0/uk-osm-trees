/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myapp;

import myapp.XMLNetworkReader;

import java.util.HashMap;
import java.util.HashSet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

//import org.openstreetmap.osmosis.xml.common.CompressionActivator;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriter;
import org.openstreetmap.osmosis.tagfilter.v0_6.TagFilter;


import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Set;
import java.util.Map;

public class Download extends HttpServlet {

  private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
  XMLNetworkReader xmlReader;

    @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    URL url = new URL("http://download.geofabrik.de/asia/maldives-latest.osm.bz2");
    HttpURLConnection uc = (HttpURLConnection) url.openConnection();
    int contentLength = uc.getContentLength();

    InputStream raw = uc.getInputStream();
    //BZip2CompressorInputStream unzippedstream = new BZip2CompressorInputStream(raw);
    
    XmlWriter xmlWriter;
    TagFilter rejectWayTagFilter, rejectRelationTagFilter, nodeTagFilter;
    DBWriter dbwriter;
    
    xmlReader = new XMLNetworkReader(raw, true, CompressionMethod.BZip2);
    xmlWriter = new XmlWriter(new File("-"),CompressionMethod.None);
    dbwriter = new DBWriter();

    
    Map emptymap = new HashMap<String,Set<String>>();
    Set emptyset = new HashSet<String>();
    rejectWayTagFilter = new TagFilter("reject-ways",emptyset,emptymap);
    rejectRelationTagFilter = new TagFilter("reject-relations",emptyset,emptymap);
    
      Map tagKVs = new HashMap<String,Set<String>>();
//      Set tagset = new HashSet<String>();

      Set valueSet = new HashSet<String>();
//      tagset.add("shop");
      valueSet.add("greengrocer");
      tagKVs.put("shop",valueSet);
      
      nodeTagFilter = new TagFilter("accept-nodes",emptyset, tagKVs);
    
    xmlReader.setSink(rejectWayTagFilter);
    rejectWayTagFilter.setSink(rejectRelationTagFilter);
    rejectRelationTagFilter.setSink(nodeTagFilter);
    nodeTagFilter.setSink(dbwriter);
    xmlReader.run();
    
    
    // rejectWayTagFilter.process();
    
    
    // rejectRelationTagFilter.process();
    
    
    // nodeTagFilter.process();
    
    // InputStream in = new BufferedInputStream(raw);
    // byte[] data = new byte[contentLength];
    // int bytesRead = 0;
    // int offset = 0;
    // while (offset < contentLength) {
    //   bytesRead = in.read(data, offset, data.length - offset);
    //   if (bytesRead == -1) {
    //     break;
    //   }
    //   offset += bytesRead;
    // }

    // if (offset != contentLength) {
    //   throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
    // }
    
    
  // add file to cloud storage
  // List<Acl> acls = new ArrayList();
  //   acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
  //   // the inputstream is closed by default, so we don't need to close it here
  // Blob blob = storage.create(BlobInfo.builder(BUCKET_NAME, "jhjg").acl(acls).build(),
  //           unzippedstream)
  
  // new SinkSourceManager(
  //           "tag filter",
  //           new TagFilter(getDefaultStringArgument(taskConfig, ""), keys, keyValues),
  //           taskConfig.getPipeArgs()
  //       );
  
  // Map<String, Set<String>> tags = new HashMap<String, Set<String>>;
  // Set setA = new HashSet();
  // String element = "supermarket";
  // setA.add(element);
  // tags.put("shop",setA)
  // new TagFilter("accept-nodes", null, tags)
            
    resp.setContentType("text/plain");
    resp.getWriter().println("{ \"download\": true }");
            
   // in.close();
  }
  


}
