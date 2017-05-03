package test.cs;

import static org.junit.Assert.assertEquals;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.cs.base.xml.XMLManager;
import com.cs.batch.tasks.PreMaturityForAI;
import com.cs.batch.tasks.ReportEODTask;
import com.cs.batch.tasks.UpdatePPIdInEPPTaskAfterEOD;
import com.cs.cluster.task.baseObj.ProcessRecordBaseObj;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.HSBCDAOExec;
import com.cs.core.utility.CSEEDAOHelper;
import com.cs.core.utility.SchemaUtil;
import com.cs.core.utility.SessionContext;
import com.cs.core.utility.SessionUtil;
import com.cs.core.utility.StringUtil;
import com.cs.ee.core.cache.EECacheHook;
import com.cs.eximap.log.LogEnv;
import com.cs.eximap.utility.CSSQLStatement;
import com.cs.eximap.utility.sqlprepare.CSSQLPrepObj;
import com.cs.hsbc.ee.ap.compliance.util.ComplianceCnvtUtil;
import com.cs.hsbc.ee.ap.prematurity.PrematurityProcesser;
import com.cs.hsbc.ee.ap.prematurity.entity.TaskEntity;
import com.cs.hsbc.ee.ap.report.ReportService;

public class TestEodReport 
{
	
	private static String realPath = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		EECacheHook.getInstance().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@Test
	public void test() throws Exception{
		String userSchema=SchemaUtil.getTableNameWithSchema(SchemaUtil.getSchemaByTableName("HSBC", "US", "SEC_BUSINESS_UNIT"),"SEC_BUSINESS_UNIT");//EXIMUSER
		String sysSchema=SchemaUtil.getTableNameWithSchema(SchemaUtil.getSystemSchema("HSBC", "US"), "com_report_maintain");//EXIMSYS
		ReportService reportServ=new ReportService("HBUS",sysSchema,userSchema);
		reportServ.setBaseInfo("HSBC", "US");
		reportServ.doTask(null, true, true);
	}
}
