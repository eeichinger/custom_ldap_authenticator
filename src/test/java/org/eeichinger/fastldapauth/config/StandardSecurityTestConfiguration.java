package org.eeichinger.fastldapauth.config;

import org.eeichinger.fastldapauth.PoolingLdapPathContextSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
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
public class StandardSecurityTestConfiguration {
    private BaseLdapPathContextSource createLdapContextSource() {
        return new DefaultSpringSecurityContextSource( "ldap://localhost:10389/dc=example,dc=com" ) {{
            setUserDn( "cn=Directory Manager" );
            setPassword( "password" );
            setPooled( true ); // use system pooling
            setAuthenticationStrategy( new SimpleDirContextAuthenticationStrategy() );
        }};
    }

    @Bean
    public BaseLdapPathContextSource ldapBindContextSource() {
        return createLdapContextSource();
    }

    @Bean
    public BaseLdapPathContextSource ldapQueryContextSource() {
        return new PoolingLdapPathContextSource(ldapBindContextSource());
    }

    public LdapAuthenticator ldapAuthenticator() {
        return new BindAuthenticator( ldapBindContextSource() ) {{
            setUserDnPatterns( new String[]{"uid={0},ou=people"} );
        }};
    }

    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        return new DefaultLdapAuthoritiesPopulator( ldapQueryContextSource(), "ou=groups" ) {{
            setGroupSearchFilter( "(&(uniqueMember={0})(objectClass=groupOfUniqueNames))" );
            setGroupRoleAttribute( "cn" );
            setRolePrefix( "" );
        }};
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new LdapAuthenticationProvider( ldapAuthenticator(), ldapAuthoritiesPopulator() ) {{
            setUserDetailsContextMapper( new LdapUserDetailsMapper() );
        }};
    }

}
