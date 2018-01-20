/**
 * This Package contains the test cases and test suite
 */
package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * 
 * 
 * @author hcanta
 * This class contains a test case runner which runs all the test cases.
 */
@RunWith(Suite.class)
@SuiteClasses({test.risk.game.cards.CardTest.class,
			   test.risk.game.GameEngineTest.class,
			   test.risk.utils.UtilsTest.class,
			   test.risk.model.RiskBoardTest.class,
			   test.risk.model.PlayerModelTest.class})
public class RiskTestsSuite {

}
