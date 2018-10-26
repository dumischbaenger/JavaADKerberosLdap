package de.dumischbaenger.kerberos;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.Configuration;

public class KerberosConfig extends Configuration {
  static final Logger LOGGER=LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static private AppConfigurationEntry ACEKerberosJava=null;
	{
		//Debug is  true storeKey false useTicketCache false useKeyTab false doNotPrompt false 
		//ticketCache is null isInitiator true KeyTab is null refreshKrb5Config is false principal is null 
		//tryFirstPass is false useFirstPass is false storePass is false clearPass is false
		Map<String, String> options=new HashMap<String, String>();
		options.put("Debug", "true");
		options.put("storeKey", "true");
		options.put("useTicketCache", "false");
		options.put("useKeyTab", "false");
		options.put("doNotPrompt", "false");
		options.put("ticketCache", null);
		options.put("isInitiator", "true");
		options.put("KeyTab", null);
		options.put("refreshKrb5Config", "false");
		options.put("principal", null);
		
		options.put("tryFirstPass", "false");
		options.put("useFirstPass", "false");
		options.put("storePass", "false");
		options.put("clearPass", "false");
		ACEKerberosJava=new AppConfigurationEntry(
		  "com.sun.security.auth.module.Krb5LoginModule", LoginModuleControlFlag.REQUIRED, options 
		);
	}


	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
	  LOGGER.info("AppConfigurationEntry with name " + name + " gets used");
	  
	  AppConfigurationEntry[] result=null;
	  
	  switch (name) {
	  case "SQLJDBCDriver": //BD MS uses this name for JAAS Config, name can not be changed in MS JDBC 6.1
    case "JavaKerberos":
      result=new AppConfigurationEntry[]{ACEKerberosJava};
      break;
    default:
      LOGGER.error("unknown AppConfigurationEntry " + name);
      System.exit(1);
      break;
    }
	  
		return result;
	}

}
