package ldapsupport;

import org.junit.Test;
import org.springframework.ldap.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.Assert.assertEquals;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
public class UnboundIDLdapAuthenticatorIT
{
	private UsernamePasswordAuthenticationToken[] tokens = {
		new UsernamePasswordAuthenticationToken("uid=achassin,ou=People,dc=example,dc=com", "duopolist"),
		new UsernamePasswordAuthenticationToken("uid=btalbot,ou=People,dc=example,dc=com", "trident"),
		new UsernamePasswordAuthenticationToken("uid=achassin,ou=People,dc=example,dc=com", "duopolist"),
		new UsernamePasswordAuthenticationToken("uid=achassin,ou=People,dc=example,dc=com", "duopolist"),
		new UsernamePasswordAuthenticationToken("uid=achassin,ou=People,dc=example,dc=com", "wrong"),
	};

	@Test
	public void canBind() throws Exception
	{
		UnboundIDLdapAuthenticator provider = new UnboundIDLdapAuthenticator("localhost", 10389);

		final long startTimeMillis = System.currentTimeMillis();
		System.out.println("Start:" + startTimeMillis);
		int failures = 0;
		for(int i=0; i<tokens.length; i++)
		{
			UsernamePasswordAuthenticationToken token = tokens[i] ;
			try
			{
				provider.authenticate(token);
			} catch (BadCredentialsException e)
			{
				failures++;
			}
		}
		System.out.println("Duration:" + (System.currentTimeMillis() - startTimeMillis));

		provider.close();

		assertEquals(1, failures);
	}
}
