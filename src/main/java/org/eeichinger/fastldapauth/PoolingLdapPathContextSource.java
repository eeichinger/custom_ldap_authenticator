package org.eeichinger.fastldapauth;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.BaseLdapPathSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;

/**
 * Oddly enough, PoolingContextSource doesn't implement BaseLdapPathContextSource. Wrap it. spring-ldap-core 2.x.x fixes this.
 * 
 * @author Erich Eichinger
 * @since 02/02/13
 */
public class PoolingLdapPathContextSource extends PoolingContextSource implements BaseLdapPathContextSource {

    public PoolingLdapPathContextSource(BaseLdapPathContextSource baseLdapPathContextSource) {
        if (baseLdapPathContextSource == null) throw new IllegalArgumentException( "baseLdapPathContextSource must not be null" );
        setContextSource( baseLdapPathContextSource );
    }

    private BaseLdapPathContextSource getLdapPathContextSource() {
        return ((BaseLdapPathContextSource) getContextSource());
    }

    @Override
    public DistinguishedName getBaseLdapPath() {
        return getLdapPathContextSource().getBaseLdapPath();
    }

    @Override
    public String getBaseLdapPathAsString() {
        return getLdapPathContextSource().getBaseLdapPathAsString();
    }
}
