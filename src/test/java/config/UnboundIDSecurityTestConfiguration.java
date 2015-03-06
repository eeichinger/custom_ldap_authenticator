package config;

import ldapsupport.LdapPoolingContextSource;
import ldapsupport.UnboundIDLdapAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

/**
* @author Erich Eichinger
* @since 02/02/13
*/
@Configuration
public class UnboundIDSecurityTestConfiguration
{
	@Bean
	public BaseLdapPathContextSource ldapContextSource()
	{
		return new DefaultSpringSecurityContextSource("ldap://localhost:10389/dc=example,dc=com") {{
			setUserDn("cn=Directory Manager");
			setPassword("password");
			setPooled(false); // don't use system pooling
			setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
		}};
	}

	@Bean
	public BaseLdapPathContextSource ldapQueryContextSource() {
		return new LdapPoolingContextSource( ) {{
			setContextSource(ldapContextSource());
		}};
	}

	public LdapAuthenticator ldapAuthenticator() {
		return new UnboundIDLdapAuthenticator("localhost", 10389) {{
			setUserDnPattern("uid={0},ou=people,dc=example,dc=com");
		}};
	}

	public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
		return new DefaultLdapAuthoritiesPopulator(ldapQueryContextSource(), "ou=groups") {{
			setGroupSearchFilter("(&(uniqueMember={0})(objectClass=groupOfUniqueNames))");
			setGroupRoleAttribute("cn");
			setRolePrefix("");
		}};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new LdapAuthenticationProvider(ldapAuthenticator(), ldapAuthoritiesPopulator()) {{
			setUserDetailsContextMapper(new LdapUserDetailsMapper());
		}};
	}

}
