# JavaADKerberosLdap

<<<<<<< HEAD
This sample application authenticates against AD Kerberos and querries the LDAP directory.

JavaKerberosAuthenticationMSSQL is a sample app that demonstrates how to connect to an MSSQL Server with Kerberos authentication with Java.


## Prerequisites

Install the JCE Unlimited Strength Jurisdiction Policy Files in your Java directory. This is necessary to use stronger Kerberos encryption types. In my case AD offered me _aes256-cts-hmac-sha1-96_ that was not compatible with Java 8 without this Java add-on.

If you just want to *test* something you can perhaps use a historical encryption type like _RC4-HMAC_. 

## Authentication


The authentication procedure uses JAAS. That said you need a module configuration file and a `CallbackHandler`.

In my case I replaced the module configuration by the class `KerberosConfig`. The configuration name is _"JavaKerberos"_.

To inform JAAS about my classes I set two system properties:

```
Security.setProperty("auth.login.defaultCallbackHandler", "de.dumischbaenger.kerberos.KerberosCallBackHandler");
Security.setProperty("login.configuration.provider", "de.dumischbaenger.kerberos.KerberosConfig");
```

Now run the gradle task _run_. 


As you can see in the log files JAAS uses both `KerberosCallBackHandler` and `KerberosConfig`. The configuration name is _"JavaKerberos"_.

```
2018-11-06 15:57:46:068 +0100 [main] INFO KerberosConfig - AppConfigurationEntry with name JavaKerberos gets used
2018-11-06 15:57:46:071 +0100 [main] INFO KerberosCallBackHandler - NameCallback: myuser
2018-11-06 15:57:46:073 +0100 [main] INFO KerberosCallBackHandler - PasswordCallback
```

If the authentication is successful a `LdapAction` instance is created and launched via the subjects _doAs()_ method.


## Query AD directory server (LDAP)

The `LdapAction` object `run()` method connects to the directory server via JNDI API starts a query and displays the result. 
=======
This sample application authenticates against AD Kerberos and querries the AD LDAP directory.
>>>>>>> 9055fab49543ddec4cd546349a9786d393d1712f
