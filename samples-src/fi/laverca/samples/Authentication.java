package fi.laverca.samples;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.util.encoders.Base64;
import org.etsi.uri.TS102204.v1_1_2.Service;

import fi.laverca.DTBS;
import fi.laverca.FiComAdditionalServices;
import fi.laverca.FiComAdditionalServices.PersonIdAttribute;
import fi.laverca.FiComClient;
import fi.laverca.FiComRequest;
import fi.laverca.FiComResponse;
import fi.laverca.FiComResponseHandler;
import fi.laverca.JvmSsl;

public class Authentication {
	private static final Log log = LogFactory.getLog(Authentication.class);
	
	private static JTextArea responseBox = new JTextArea();
	
	/**
	 * Connects to MSSP using SSL and waits for response.
	 * @param phoneNumber
	 */
	private static void estamblishConnection(final String phoneNumber) {
		
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
        byte[] authnChallenge = new DTBS(apTransId, "UTF-8").toBytes();

        Service noSpamService = FiComAdditionalServices.createNoSpamService("A12", false);
        LinkedList<Service> additionalServices = new LinkedList<Service>();
        LinkedList<String> attributeNames = new LinkedList<String>(); 
 
        attributeNames.add(FiComAdditionalServices.PERSON_ID_VALIDUNTIL);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_ADDRESS);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_EMAIL);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_AGE);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_SUBJECT);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_SURNAME);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_GIVENNAME);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_HETU);
        attributeNames.add(FiComAdditionalServices.PERSON_ID_SATU);
        Service personIdService = FiComAdditionalServices.createPersonIdService(attributeNames);
        additionalServices.add(personIdService);
        
        try {
            log.info("calling authenticate");
            fiComClient.authenticate(apTransId, 
            		authnChallenge, 
            		phoneNumber, 
            		noSpamService, 
            		additionalServices, 
            		new FiComResponseHandler() {
		            	@Override
		            	public void onResponse(FiComRequest req, FiComResponse resp) {
		            		log.info("got resp");
		            		log.info(resp.getPkcs7Signature().getSignerCn());
		            		
		            		try {
		            			responseBox.setText("MSS Signature: " + 
		            					new String(Base64.encode(resp.getMSS_StatusResp().
		            					getMSS_Signature().getBase64Signature()), "ASCII") +
		            					"\n\n" + responseBox.getText());
		            		} catch (UnsupportedEncodingException e) {
		            			log.info("Unsupported encoding", e);
		            		}
		            		for(PersonIdAttribute a : resp.getPersonIdAttributes()) {
		            			log.info(a.getName() + " " + a.getStringValue());
		            			responseBox.setText(a.getStringValue() + "\n" + responseBox.getText());
		            		}
		            	}
		
		            	@Override
		            	public void onError(FiComRequest req, Throwable throwable) {
		            		log.info("got error", throwable);
		            		responseBox.setText("ERROR, " + phoneNumber + "\n\n" + responseBox.getText());
		            	}
		            });
            
        }
        catch (IOException e) {
            log.info("error establishing connection", e);
        }

        fiComClient.shutdown();
	}
	
	/**
	 * Authenticates user
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Authentication");
		frame.setSize(300, 380);
		
		Container pane = frame.getContentPane();
		
		pane.add(new JLabel("Phone number"), BorderLayout.PAGE_START);
		final JTextField number = new JTextField("+35847001001");
		number.setPreferredSize(new Dimension(230, 10));
		pane.add(number, BorderLayout.CENTER);
		
		JButton send = new JButton("Send");
		send.setPreferredSize(new Dimension(70, 10));
		pane.add(send, BorderLayout.EAST);
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				estamblishConnection(number.getText());
			}
		});
		
		responseBox.setPreferredSize(new Dimension(200, 300));
		pane.add(responseBox, BorderLayout.PAGE_END);
		
		frame.setVisible(true);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}