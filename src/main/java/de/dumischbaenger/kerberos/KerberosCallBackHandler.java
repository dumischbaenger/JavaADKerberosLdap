package de.dumischbaenger.kerberos;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KerberosCallBackHandler implements CallbackHandler {
  Logger LOG=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      for(int i=0;i<callbacks.length;i++){
      Callback cb=callbacks[i];
        if(cb instanceof PasswordCallback) {
          LOG.info("PasswordCallback");
          PasswordCallback pwcb=(PasswordCallback)cb;
          pwcb.setPassword(System.getProperty("password").toCharArray());
        } else if (cb instanceof NameCallback){
          LOG.info("NameCallback: " + System.getProperty("username"));
        	NameCallback ncb=(NameCallback)cb;
        	ncb.setName(System.getProperty("username"));
        }
    }   
  }
}

