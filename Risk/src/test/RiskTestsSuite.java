/**
 * This Package contains the testcases and test suite
 */
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * @author hcanta
 *Class That a test case runner uses to automatically run test cases
 */
@RunWith(Suite.class)
@SuiteClasses({test.risk.game.cards.CardTest.class,
			   test.risk.game.GameEngineTest.class,
			   test.risk.utils.MapUtilsTest.class,
			   test.risk.model.RiskBoardTest.class,
			   test.risk.model.PlayerModelTest.class})
public class RiskTestsSuite {

}
