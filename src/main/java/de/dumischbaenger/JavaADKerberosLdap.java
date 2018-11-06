package de.dumischbaenger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.Security;
import java.util.Iterator;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dumischbaenger.kerberos.KerberosCallBackHandler;
import de.dumischbaenger.kerberos.LdapAction;
import de.dumischbaenger.kerberos.Role;

public class JavaADKerberosLdap {
  static private Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


  public static void main(String[] args) {

    LOG.info("program started");

    Security.setProperty("auth.login.defaultCallbackHandler", "de.dumischbaenger.kerberos.KerberosCallBackHandler");
    Security.setProperty("login.configuration.provider", "de.dumischbaenger.kerberos.KerberosConfig");

    try {
      // load a properties file from class path, inside static method
      InputStream is = JavaADKerberosLdap.class.getResourceAsStream("/config.properties");
      System.getProperties().load(is);
    } catch (IOException ex) {
      ex.printStackTrace();
      LOG.info("cannot read config file",ex);
      System.exit(1);

    }

    String props[] = { "javax.security.auth.useSubjectCredsOnly", "java.security.krb5.realm", "java.security.krb5.kdc",
        "auth.login.defaultCallbackHandler", "login.configuration.provider", "java.security.auth.login.config",
        "login.config.url.1", "login.config.url.2", "login.config.url.3",
        "username", "ldapURL", "ldapBindDn", "ldapSearchBase", "ldapFilter", "ldapReturnAttr",
    };

    for (int i = 0; i < props.length; i++) {
      LOG.info(props[i] + ": " + System.getProperty(props[i]));
    }

    LoginContext lc = null;

    try {
      lc = new LoginContext("JavaKerberos", new KerberosCallBackHandler());
    } catch (LoginException le) {
      LOG.info("Cannot create LoginContext. " + le.getMessage());
      System.exit(-1);
    } catch (SecurityException se) {
      LOG.info("Cannot create LoginContext. " + se.getMessage());
      System.exit(1);
    }

    try {
      lc.login();

    } catch (LoginException le) {

      LOG.info("Error at Authentication:",le);
      System.exit(1);

    }

    LOG.info("Authentication succeed!");
    
    Subject subject=lc.getSubject();
    
    PrivilegedAction<Object> ldapAction=new LdapAction();
    NamingEnumeration<String> roles = (NamingEnumeration<String>) subject.doAs(subject, ldapAction);

    Set<Principal> principals=subject.getPrincipals();
    //Iterator<Principal> i=principals.iterator();
    //Principal principal=i.next();
    addRoleToSubject(subject,roles);
    printPrincipals(principals);

    try {
      lc.logout();
    } catch (LoginException le) {
      LOG.info("Error at Logout:");
      le.printStackTrace();
    }

    LOG.info("program stopped");
  }

  private static void printPrincipals(Set<Principal> myPrincipalsSet) {
     Principal principal;
     Iterator<Principal> myIt = myPrincipalsSet.iterator();
     while (myIt.hasNext()) {
     principal = (Principal) myIt.next();
     LOG.info(
     "principal classname: "
     + principal.getClass().getName()
     + " - value: "
     + principal.getName());
     }
    
  }

  private static void addRoleToSubject(Subject subject,NamingEnumeration<String> roles) {

    if (roles != null) {
      String fullRoleName;
      String roleName;
      while (roles.hasMoreElements()) {
        fullRoleName = roles.nextElement().toString();

        if (fullRoleName.indexOf("CN") != -1) {
          roleName = fullRoleName.substring(fullRoleName.indexOf("CN") + 3, fullRoleName.indexOf(","));
           Role jrp = new Role(roleName);
           subject.getPrincipals().add(jrp);
        }

      }
    } else {
      LOG.info("user has no roles");
    }
  }


}
