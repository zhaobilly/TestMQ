package test.cs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.cs.advice.TestAdviceSuite;
import test.cs.aqtest.TestAQSuite;
import test.cs.baif.HSBCTestBAIFSuite;
import test.cs.baserate.TestBaseRateSuite;
import test.cs.batch.TestBatchTaskSuite;
import test.cs.cim.HSBCTestCIMSuite;
import test.cs.compliance.TestComplianceSuite;
import test.cs.dms.TestAllDmsServiceSuite;
import test.cs.ee.bizcomponent.TestBusinessComponentSuite;
import test.cs.limits.TestAllLimitServiceSuite;
import test.cs.mmm.TestMMMServiceSuite;
import test.cs.notification.TestAllNotificationAndAQServiceSuite;
import test.cs.rdm.Rdm4Suite;
import test.cs.report.ReportTestSuit;
import test.cs.scheduler.TestSchedulerSuite;
import test.cs.task.Task4Suite;


/**
 * @author King
 * @date 2011-03-05
 */
//Define this class is test suite
@RunWith(Suite.class)
//Define test case be included in suite
@SuiteClasses({
	TestComplianceSuite.class,
	TestAllLimitServiceSuite.class,
	HSBCTestCIMSuite.class,
	TestBaseRateSuite.class,
	TestAQSuite.class,
	TestBatchTaskSuite.class,
	TestBusinessComponentSuite.class,
	TestMMMServiceSuite.class,
	Rdm4Suite.class,
	ReportTestSuit.class,
	Task4Suite.class,
	TestSchedulerSuite.class,
	HSBCTestBAIFSuite.class,
	TestAllDmsServiceSuite.class,
	TestAllNotificationAndAQServiceSuite.class,
	/** @author jancy.jin @date 2013-12-11 @Ref PF-0015 edit_S*/
	TestAdviceSuite.class
	/** @author jancy.jin @date 2013-12-11 @Ref PF-0015 edit_E*/
})
public class HSBCCS4Suite {


}
