package ldapsupport;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.BaseLdapPathSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
public class LdapPoolingContextSource extends PoolingContextSource implements BaseLdapPathContextSource, InitializingBean
{
	private BaseLdapPathSource baseLdapPathSource;

	public void setBaseLdapPathSource(BaseLdapPathSource baseLdapPathSource)
	{
		this.baseLdapPathSource = baseLdapPathSource;
	}

	protected BaseLdapPathSource getLdapContextSource()
	{
		if (baseLdapPathSource != null)
			return baseLdapPathSource;

		return (BaseLdapPathSource) super.getContextSource();
	}

	@Override
	public DistinguishedName getBaseLdapPath()
	{
		return baseLdapPathSource.getBaseLdapPath();
	}

	@Override
	public String getBaseLdapPathAsString()
	{
		return baseLdapPathSource.getBaseLdapPathAsString();
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (baseLdapPathSource == null
				&& getContextSource() instanceof BaseLdapPathSource)
		{
			baseLdapPathSource = (BaseLdapPathSource) getContextSource();
		}
		if (baseLdapPathSource == null)
		{
			throw new IllegalArgumentException("either set an explicit or BaseLdapPathSource or a ContextSource that implements BaseLdapPathSource");
		}
	}
}
