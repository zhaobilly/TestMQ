package test.cs;

import static org.junit.Assert.assertEquals;

import java.sql.Types;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.cs.base.log.CSLogger;
import com.cs.base.xml.XMLManager;
import com.cs.core.dao.DSManager;
import com.cs.core.utility.CSEEDAOHelper;
import com.cs.core.utility.SchemaUtil;
import com.cs.ee.core.cache.EECacheHook;
import com.cs.eximap.log.LogEnv;
import com.cs.eximap.utility.CSSQLStatement;
import com.cs.hsbc.ee.ap.compliance.util.ComplianceCnvtUtil;
import com.cs.hsbc.ee.ap.report.ReportService;

public class TestAsyncWolf 
{
	
	public static ComplianceCnvtUtil complianceCnvtUtil=null;
	private static Document gapiDoc = null;
	private static String strSucResp = null;
	private static String strFaiResp = null;
	private static String realPath = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		gapiDoc=XMLManager.xmlFileToDom("C:/Users/Administrator/Desktop/bundle/gapiDoc.xml");
		strSucResp=XMLManager.convertToString(XMLManager.xmlFileToDom("C:/Users/Administrator/Desktop/bundle/strSucResp.xml"));
		strFaiResp=XMLManager.convertToString(XMLManager.xmlFileToDom("C:/Users/Administrator/Desktop/bundle/strFaiResp.xml"));
//		EECacheHook.getInstance().start();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDoTask() throws Exception{
		complianceCnvtUtil.checkAsyncWolfHit(gapiDoc, strFaiResp);
//		CSLogger logger = LogEnv.getLogger("BTSMGR");
//		logger.debug("aaaaaa");
	}

}
