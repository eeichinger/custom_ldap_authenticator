package ldapsupport;

import com.unboundid.ldap.sdk.*;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
public class UnboundIDLdapAuthenticator implements LdapAuthenticator
{
//	private final ServerSet servers;
	private final LDAPThreadLocalConnectionPool connectionPool;

	private String userDnPattern = "{0}";

	public String getUserDnPattern()
	{
		return userDnPattern;
	}

	public void setUserDnPattern(String userDnPattern)
	{
		if (userDnPattern == null) {
			this.userDnPattern = "{0}";
		} else {
			this.userDnPattern = userDnPattern;
		}
	}

	public UnboundIDLdapAuthenticator(String serverName, int port)
	{
		this(new SingleServerSet(serverName, port));
	}

	public UnboundIDLdapAuthenticator(ServerSet servers)
	{
		this.connectionPool = new LDAPThreadLocalConnectionPool(servers, null);
	}

	public void close() {
		connectionPool.close();
	}

	@Override
	public DirContextOperations authenticate(Authentication authentication) {
		try {
			final String dn = userDnPattern.replace("{0}", (String)authentication.getPrincipal());
			final String password = (String) authentication.getCredentials();

			LDAPResult result = authenticate(this.connectionPool, dn, password);

			if (result.getResultCode() == ResultCode.SUCCESS) {
				return new DirContextAdapter(dn);
			}
			throw new BadCredentialsException(result.toString());
		}
		catch (LDAPException e) {
			throw new AuthenticationServiceException("", e);
		}
	}

	private LDAPResult authenticate(LDAPThreadLocalConnectionPool connectionPool, String dn, String password) throws LDAPException {
		LDAPConnection connection = connectionPool.getConnection();
		try
		{
			return connection.bind(dn, password);
		}
		catch (LDAPException le)
		{
			return le.toLDAPResult();
		}
		finally {
			connectionPool.releaseConnection(connection);
		}
	}
}
