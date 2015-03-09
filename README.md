
# Overview

This is a little project I wrote to evaluate options for using LDAP to authenticate webservice clients. The challenge came up with a project, where the usage profile resulted in lots of different clients performing only a few requests each. Standard LDAP client technologies, which rely on caching e.g. binds, don't help much in such a case. A more direct and faster approach is needed.

see using_spring+ldap_for_webservice_authentication.docx for details of the problem

This sample code shows the difference between a standard out-of-the-box Spring LDAP configuration for authenticating web clients and the use of a custom implementation of an *org.springframework.security.ldap.authentication.LdapAuthenticator* based on the UnboundID LDAP SDK (https://www.ldap.com/unboundid-ldap-sdk-for-java)

Run time differences on my machine give

```shell
[INFO] Starting UnboundID server
[INFO] Started UnboundID server
[INFO] Waiting for command from client
[o.e.f.c.StandardSecurityTestConfiguration$1] -  URL 'ldap://localhost:10389/dc=example,dc=com', root DN is 'dc=example,dc=com'
class org.eeichinger.fastldapauth.StandardAuthenticationPerformanceIT Start:1425898900450
class org.eeichinger.fastldapauth.StandardAuthenticationPerformanceIT Duration:7708
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.292 sec
Running org.eeichinger.fastldapauth.UnboundIDAuthenticationPerformanceIT
[o.e.f.c.UnboundIDSecurityTestConfiguration$1] -  URL 'ldap://localhost:10389/dc=example,dc=com', root DN is 'dc=example,dc=com'
class org.eeichinger.fastldapauth.UnboundIDAuthenticationPerformanceIT Start:1425898908235
class org.eeichinger.fastldapauth.UnboundIDAuthenticationPerformanceIT Duration:2091
```

# TODO

* ldap-maven-plugin doesn't wait until ldap server startup complete, causing intermittent test failures
  this is currently solved by having the UnboundIDLdapAuthenticator do a connect retry at startup
