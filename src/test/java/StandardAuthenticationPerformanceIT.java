import config.StandardSecurityTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Erich Eichinger
 * @since 02/02/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = StandardSecurityTestConfiguration.class)
public class StandardAuthenticationPerformanceIT extends AbstractAuthenticationPerformanceTests
{
}
