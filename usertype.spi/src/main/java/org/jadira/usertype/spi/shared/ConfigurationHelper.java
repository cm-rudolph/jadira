/*
 *  Copyright 2011 Christopher Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.usertype.spi.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;

public final class ConfigurationHelper {

	private static ThreadLocal<SessionFactory> currentSessionFactory = new ThreadLocal<SessionFactory>();
	
	private static final Map<SessionFactory, Properties> DEFAULT_PROPERTIES = new HashMap<SessionFactory, Properties>();
	
	private static final Map<SessionFactory, Boolean> DEFAULT_USEJDBC42 = new HashMap<SessionFactory, Boolean>();
	
    private ConfigurationHelper() {
    }
    
    public static String getProperty(String key) {
    	
    	SessionFactory current = currentSessionFactory.get();
    	if (current != null) {
    		Properties defaults = DEFAULT_PROPERTIES.get(current);
    		if (defaults != null) {
    			return defaults.getProperty(key);
    		}
    	}
    	return null;
    }
    
    public static boolean getUse42Api() {
        SessionFactory current = currentSessionFactory.get();
        if (current != null) {
            return DEFAULT_USEJDBC42.get(current);
        }
        return false;        
    }
    
    static void setUse42Api(SessionFactory sessionFactory, boolean use42Api) {
        DEFAULT_USEJDBC42.put(sessionFactory, use42Api);
    }

	static void setCurrentSessionFactory(SessionFactory sessionFactory) {
		currentSessionFactory.set(sessionFactory);
	}
	
	static void configureDefaultProperties(SessionFactory sessionFactory, Properties properties) {
		if (properties == null) {
			DEFAULT_PROPERTIES.remove(sessionFactory);
			DEFAULT_USEJDBC42.remove(sessionFactory);
		} else {
			DEFAULT_PROPERTIES.put(sessionFactory, properties);
		}
	}
}
