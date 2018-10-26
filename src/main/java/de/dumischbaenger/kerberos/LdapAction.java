package de.dumischbaenger.kerberos;

import java.lang.invoke.MethodHandles;
import java.security.PrivilegedAction;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LdapAction implements PrivilegedAction<Object> {
  static private Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public Object run() {
    NamingEnumeration<String> role = null;

    
    String ldapBindDn = System.getProperty("ldapBindDn");

    String ldapURL =  System.getProperty("ldapURL");
    String ldapSearchBase =  System.getProperty("ldapSearchBase");
    String ldapFilter =  System.getProperty("ldapFilter");
    String ldapReturnAttr =  System.getProperty("ldapReturnAttr");

  
    Hashtable<String,Object> env = new Hashtable<String, Object>(11);
    env.put(
      Context.INITIAL_CONTEXT_FACTORY,
      "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL,ldapURL);
  
    env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
    env.put(Context.SECURITY_PRINCIPAL, ldapBindDn);
    
    //turn on ldap trace
    //env.put("com.sun.jndi.ldap.trace.ber", System.err);

  
    try {
  
      DirContext ctx = new InitialDirContext(env);
      
      SearchControls ctrls = new SearchControls();
      ctrls.setReturningAttributes(ldapReturnAttr.split(","));
      ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

      NamingEnumeration<javax.naming.directory.SearchResult> answers = ctx.search(ldapSearchBase, ldapFilter, ctrls);
      
      
  
      while (answers.hasMoreElements()) {
        SearchResult sr = (SearchResult) answers.next();
        Attributes userAttributes = sr.getAttributes();
        
        NamingEnumeration<String> ids=userAttributes.getIDs();
        while(ids.hasMoreElements()) {
          String id=ids.next();
          LOG.info("AttributName="+id + " AttributValue="+userAttributes.get(id));
          
        }
        Attribute at = userAttributes.get("memberOf");
  
        if (at != null) {
          role = (NamingEnumeration<String>) at.getAll();
        } else {
          role = null;
        }
  
      }
  
    } catch (NamingException e) {
      LOG.info("naming exception: ",e);
      System.exit(1);
  
    }


    return role;
  }
}
