/*
 * Copyright (c) 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.libermundi.theorcs.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {
	private ObjectUtils(){}
	
	private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);
	
	@SuppressWarnings("unchecked")
	public static <T> T safeDeepCopy(T orig){
		T copy=null;
		ObjectOutputStream out=null;
		ObjectInputStream in=null;
		FastByteArrayOutputStream fbos=null;
        try {
            // Write the object out to a byte array
            fbos = new FastByteArrayOutputStream();
            out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
        } catch(IOException e) {
        	throw new RuntimeException(e.getMessage());
        } finally {
        	try {
        		if(out != null){
        			out.close();
        		}
        	} catch (IOException e) {
				logger.warn(e.getMessage());
        	}
        }

        try {
        	// Retrieve an input stream from the byte array and read
        	// a copy of the object back in.
			in = new ObjectInputStream(fbos.getInputStream());
			copy = (T)in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
        		if(in != null){
        			in.close();
        		}
			} catch (IOException e) {
				logger.warn(e.getMessage());
			}
		}

		return copy;
	}
}
