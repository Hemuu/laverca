package fi.laverca.samples;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etsi.uri.TS102204.v1_1_2.Service;

import fi.laverca.DTBS;
import fi.laverca.FiComAdditionalServices;
import fi.laverca.FiComClient;
import fi.laverca.FiComRequest;
import fi.laverca.FiComResponse;
import fi.laverca.FiComResponseHandler;
import fi.laverca.JvmSsl;

public class FiComSigReqCaller {
    private static final Log log = LogFactory.getLog(FiComSigReqCaller.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

        log.info("setting up ssl");
        JvmSsl.setSSL("etc/laverca-truststore",
                      "changeit",
                      "etc/laverca-keystore",
                      "changeit",
                      "JKS");

        String apId  = "http://laverca-eval.fi";
        String apPwd = "pfkpfk";

        String aeMsspIdUri = "http://dev-ae.mssp.dna.fi";
        //TODO: TeliaSonera
        //TODO: Elisa

        String msspSignatureUrl    = "https://dev-ae.mssp.dna.fi/soap/services/MSS_SignaturePort";
        String msspStatusUrl       = "https://dev-ae.mssp.dna.fi/soap/services/MSS_StatusQueryPort";
        String msspReceiptUrl      = "https://dev-ae.mssp.dna.fi/soap/services/MSS_ReceiptPort";

        log.info("creating FiComClient");
        FiComClient fiComClient = new FiComClient(apId, 
                                                  apPwd, 
                                                  aeMsspIdUri, 
                                                  msspSignatureUrl, 
                                                  msspStatusUrl, 
                                                  msspReceiptUrl); 

        String apTransId = "A"+System.currentTimeMillis();
        String phoneNumber = "+35847001001";
        byte[] authnChallenge = new DTBS(apTransId, "UTF-8").toBytes();

        Service noSpamService = FiComAdditionalServices.createNoSpamService("A12", false);
        
        try {
            log.info("calling authenticate");
                fiComClient.authenticate(apTransId, 
                                         authnChallenge, 
                                         phoneNumber, 
                                         noSpamService, 
                                         null, // additionalServices, 
                                         new FiComResponseHandler() {
                                             @Override
                                             public void onResponse(FiComRequest req, FiComResponse resp) {
                                                 log.info("got resp");
                                                 log.info(resp.getPkcs7Signature().getSignerCn());
                                             }

                                             @Override
                                             public void onError(FiComRequest req, Throwable throwable) {
                                                 log.info("got error", throwable);
                                             }
                                         });
        }
        catch (IOException e) {
            log.info("error establishing connection", e);
        }

        fiComClient.shutdown();
        
    }

}