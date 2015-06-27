/* ==========================================
 * Laverca Project
 * https://sourceforge.net/projects/laverca/
 * ==========================================
 * Copyright 2014 Laverca Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.laverca.etsi;

import org.etsi.uri.TS102204.v1_1_2.Description;
import org.etsi.uri.TS102204.v1_1_2.Service;

/**
 * ETSI specific AdditionalServices
 *
 */
public class EtsiAdditionalServices {
    
    /**
     * Create a basic ETSI Service element
     * @param uri AdditionalService URI
     * @return Created Service
     */
    public static Service createService(final String uri) {
        Service     s = new Service();
        Description d = new Description();
        d.setMssURI(uri);
        s.setDescription(d);
        
        return s;
    }
    
}
