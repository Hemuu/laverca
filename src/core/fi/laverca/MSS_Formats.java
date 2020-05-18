/* ==========================================
 * Laverca Project
 * https://sourceforge.net/projects/laverca/
 * ==========================================
 * Copyright 2017 Laverca Project
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

package fi.laverca;

/**
 * MSS Format URIs
 * <p>As per FiCom Implementation Guideline v2.0
 */
public class MSS_Formats {

    /**
     * http://uri.etsi.org/TS102204/v1.1.2#PKCS7
     */
    public final static String PKCS7 = "http://uri.etsi.org/TS102204/v1.1.2#PKCS7";

    /**
     * http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature
     */
    public final static String CMS = "http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature";

    /**
     * http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1
     */
    public final static String KIURU_PKCS1 = "http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1";
    
    /**
     * http://mss.ficom.fi/TS102204/v1.0.0#PKCS1
     */
    public final static String FICOM_PKCS1 = "http://mss.ficom.fi/TS102204/v1.0.0#PKCS1";
    
    /**
     * http://mss.ficom.fi/TS102204/v1.0.0#PKCS1
     * @deprecated Use {@link MSS_Formats#FICOM_PKCS1} instead.
     */
    @Deprecated
    public final static String PKCS1 = FICOM_PKCS1;

}