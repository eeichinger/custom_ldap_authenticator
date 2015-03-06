import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.Assert.assertEquals;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
public abstract class AbstractAuthenticationPerformanceTests {

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Before
	public void before()
	{
		System.setProperty("com.sun.jndi.ldap.connect.pool", "true");
		System.setProperty("com.sun.jndi.ldap.connect.pool.initsize", "2");
		System.setProperty("com.sun.jndi.ldap.connect.pool.maxsize", "2");
		System.setProperty("com.sun.jndi.ldap.connect.pool.prefsize", "2");
		System.setProperty("com.sun.jndi.ldap.connect.pool.timeout", "600000");
		System.setProperty("com.sun.jndi.ldap.connect.pool.debug", "off");
	}

	private UsernamePasswordAuthenticationToken[] tokens = {
		new UsernamePasswordAuthenticationToken("achassin", "duopolist"),
		new UsernamePasswordAuthenticationToken("achassin", "duopolist"),
		new UsernamePasswordAuthenticationToken("achassin", "duopolist"),
		new UsernamePasswordAuthenticationToken("achassin", "duopolist"),
		new UsernamePasswordAuthenticationToken("achassin", "wrong"),
	};

	@Test
	public void test()
	{
		final long startTimeMillis = System.currentTimeMillis();
		System.out.println(getClass() + " Start:" + startTimeMillis);
		int failures = 0;
		for(int i=0; i<5000; i++)
		{
			UsernamePasswordAuthenticationToken token = tokens[i % tokens.length] ;
			boolean ok;
			try
			{
				Authentication authentication =  authenticationProvider.authenticate(token);
				ok = authentication.isAuthenticated();
			} catch (BadCredentialsException e)
			{
				ok = false;
			}
			if (!ok) failures++;
		}
		System.out.println(getClass() + " Duration:" + (System.currentTimeMillis() - startTimeMillis));

		assertEquals(1000, failures);
	}
}
