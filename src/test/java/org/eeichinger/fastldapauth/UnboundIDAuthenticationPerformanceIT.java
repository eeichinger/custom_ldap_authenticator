package org.eeichinger.fastldapauth;

import org.eeichinger.fastldapauth.config.UnboundIDSecurityTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnboundIDSecurityTestConfiguration.class)
public class UnboundIDAuthenticationPerformanceIT extends AbstractAuthenticationPerformanceTests {
}
