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

public class TestPrematurityForAI 
{
	
	private static Document doc = null;
	private static String realPath = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		doc=XMLManager.xmlFileToDom("E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA\\EE_SYS\\TASK\\T00000000032.xml");
		EECacheHook.getInstance().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@Test
	public void test() throws Exception{
		TaskEntity taskInfoEntity = new TaskEntity();
		taskInfoEntity.setStrBankGroupID("HSBC");
		taskInfoEntity.setStrCountryCode("HK");
		taskInfoEntity.setStrUnitCode("HBAP");
		taskInfoEntity.setStrDBType("OR");
		taskInfoEntity.setStrTrxDS("jdbc/eximt");
		taskInfoEntity.setStrTrxSchema("IDE1_EXIMTRX");
		taskInfoEntity.setTaskName("PreMaturityForAITask");
		taskInfoEntity.setTaskId("T00000000032");
		taskInfoEntity.setTaskDesc("prematurity for AI task");
		
		PrematurityProcesser prematurityProcesser=new PrematurityProcesser(taskInfoEntity);
		prematurityProcesser.process();
	}
	
//	public List<HashMap<String, String>> getData() throws Exception{
//		CSSQLPrepObj<Integer,Object> prepObj = new CSSQLPrepObj<Integer, Object>();
//		prepObj.setDs("jdbc/eximt");
//		prepObj.addPrepareSql(genEPPSQL());
//		List<Integer> listType = new ArrayList<Integer>();
//		listType.add(Types.VARCHAR);
//		prepObj.addPrepareTypeObjList(listType);
//		List<Object>listValue = new ArrayList<Object>();
//		listValue.add("HSBC");
//		prepObj.addPrepareValueObjList(listValue);
//		List<HashMap<String, String>> resList=new HSBCDAOExec().executeQueryAsList(prepObj);
//		return resList;
//	}
//	
//	private String genEPPSQL() {
//		String strEppmTable = "IDE1_EXIMTRX.EPPM_MASTER";
//		String strCustTable ="IDE1_EXIMTRX.CUST_MASTER";
//		StringBuffer sbSQL = new StringBuffer();
//		sbSQL.append("SELECT T1.C_UNIT_CODE,T1.AI_SP_ID,T1.ACC_BUYER_SETL,T1.ACC_COLL_BK,T1.BUYR_ID,T1.CCY,T1.C_MAIN_REF,T1.EPP_NM,T1.EPP_REF,");
//		sbSQL.append("T1.IF_DIR_DEBIT,T1.PRFL_DFLT_BUYR,T2.CUST_NM,T2.PRE_MATURITY_DAYS,T2.CUST_HUB_BOS_ID,T2.GRAC_OVRDUE_INT_DAY FROM ");
//		sbSQL.append(strEppmTable);
//		sbSQL.append(" T1 , ");
//		sbSQL.append(strCustTable);
//		sbSQL.append(" T2 WHERE ");
//		sbSQL.append("T1.C_BK_GROUP_ID =? ");
//		sbSQL.append(" AND T1.C_UNIT_CODE ='HBAP' ");
//		sbSQL.append(" AND T1.BUYR_ID = T2.C_MAIN_REF");
//		//@ISC-EE-0500 @author  aine @date 2014-02-21 _S
////		sbSQL.append(" AND (T1.EPP_STAT = 'A' OR T1.EPP_STAT = 'S') AND T1.ACC_BUYER_SETL IS NOT NULL AND T1.ACC_COLL_BK IS NOT NULL AND T1.PRFL_DFLT_BUYR IS NOT NULL");
//		sbSQL.append(" AND (T1.EPP_STAT = 'A' OR T1.EPP_STAT = 'S') AND T1.ACC_COLL_BK IS NOT NULL AND T1.PRFL_DFLT_BUYR IS NOT NULL");
//		//@ISC-EE-0500 @author  aine @date 2014-02-21 _E
//		return sbSQL.toString();
//	}
}
