/* ==========================================
 * Laverca Project
 * https://sourceforge.net/projects/laverca/
 * ==========================================
 * Copyright 2015 Laverca Project
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

package fi.laverca.registration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import fi.laverca.jaxb.mreg.RegistrationInput;
import fi.laverca.jaxb.mss.MSSRegistrationReq;
import fi.laverca.jaxb.mss.MSSRegistrationResp;
import fi.laverca.mss.MssClient;
import fi.laverca.mss.MssConf;
import fi.laverca.util.LavercaContext;

/**
 * Client specifically for handling MSS_Registration operations
 * 
 */
public class RegistrationClient {

    private final MssClient client;
   
    /**
     * Default constructor with {@link MssConf}
     * <p>The configuration should contain at least the following options:
     * 
     * <ul>
     * <li>{@link MssConf#REGISTRATION_URL} - where to send the MSS_RegistrationRequests
     * <li>{@link MssConf#AP_ID}            - what AP_ID to use in the requests
     * <li>{@link MssConf#KEYSTORE_FILE}    - keystore to use
     * <li>{@link MssConf#KEYSTORE_PWD}     - keystore password
     * </ul>
     * @param conf Configuration for this client's MSsClient. 
     */
    public RegistrationClient(final MssConf conf) {
        this.client = new MssClient(conf);
    }
    
    /**
     * Send an MSS_RegistrationRequest (with MReg extension)
     * 
     * @param req MReg Request
     * @return parsed MReg response
     * @throws IOException
     */
    public MregResponse send(final MregRequest req) throws IOException {
        req.context = new LavercaContext();
        MSSRegistrationReq  _req  = req.toMSSReq(this.client);
        MSSRegistrationResp _resp = this.client.send(_req, req.context, req.security);
        return new MregResponse(_resp, req.context);
    }
    
    /**
     * Send a multi-operation MSS_RegistrationRequest (with MReg extension)
     * <p>Reads the target and AP_TransID from {@code req}. Adds a new RegistrationInput for each element in {@code additionalRequests}
     *
     * @param req MReg Request
     * @param additionalRequests Additional requests (only operation content is fetched from these - target is ignored
     * @return parsed MReg response
     * @throws IOException
     */
    public MregResponse send(final MregRequest req,
                             final MregRequest ... additionalRequests) throws IOException {
         return this.send(req, Arrays.asList(additionalRequests));
    }
    
    /**
     * Send a multi-operation MSS_RegistrationRequest (with MReg extension)
     * <p>Reads the target and AP_TransID from {@code req}. Adds a new RegistrationInput for each element in {@code additionalRequests}
     * @param req MReg Request
     * @param additionalRequests Additional requests (only operation content is fetched from these - target is ignored
     * @return parsed MReg response
     * @throws IOException
     */
    public MregResponse send(final MregRequest req, 
                             final List<MregRequest> additionalRequests) throws IOException {
        
        req.context = new LavercaContext();
        MSSRegistrationReq _final = req.toMSSReq(this.client);
        
        int i = 100;
        for (MregRequest data : additionalRequests) {
            MSSRegistrationReq _data = data.toMSSReq(this.client);
            RegistrationInput _input = _data.getRegistrationInputs().get(0);
            _input.setInputId("_" + i++);
            
            _final.getRegistrationInputs().add(_input);
        }
        MSSRegistrationResp _resp = this.client.send(_final, req.context, req.security);
        
        return new MregResponse(_resp, req.context);
    }
    
    /**
     * Get the raw {@link MssClient}
     * @return {@link MssClient} instance
     */
    public MssClient getMssClient() {
        return this.client;
    }
    
}
