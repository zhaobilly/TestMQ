package test.cs;

import static org.junit.Assert.assertEquals;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.cs.base.xml.XMLManager;
import com.cs.core.dao.DSManager;
import com.cs.core.dao.HSBCDAOExec;
import com.cs.core.utility.CSEEDAOHelper;
import com.cs.core.utility.SchemaUtil;
import com.cs.ee.core.cache.EECacheHook;
import com.cs.eximap.log.LogEnv;
import com.cs.eximap.utility.CSSQLStatement;
import com.cs.eximap.utility.sqlprepare.CSSQLPrepObj;
import com.cs.hsbc.ee.ap.compliance.util.ComplianceCnvtUtil;
import com.cs.hsbc.ee.ap.report.ReportService;

public class TestPrepareStatement 
{
	
	public static ComplianceCnvtUtil complianceCnvtUtil=null;
	private static String realPath = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
//		EECacheHook.getInstance().start();
		
	}

	private static HashMap<String, String> fldMap = new HashMap<String,String>();
	static{
		fldMap.put("BI", "BUYR_ID");
		fldMap.put("SR", "SUPLR_REF");
		fldMap.put("CY", "CCY");
		fldMap.put("AA", "AI_ACT_AMT");
		fldMap.put("AD", "AI_DUE_DT");
		fldMap.put("AS", "AI_STAT_STAFF_CD");
		fldMap.put("AN", "AI_NUM");
		fldMap.put("CA", "CN_TTL_AMT");
		fldMap.put("CN", "CN_NUM");
		fldMap.put("CJ", "CN_REJ_FLG");
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDoTask() throws Exception{
		char char1 = 1;
		String c1 = String.valueOf(char1);
		String strGroupKey="BI=SCSHBAP160000001"+c1+"AA=500500.00"+c1+"AD=20160720"+c1+"AS=RJ";
		
		CSSQLStatement sqlstatement = new CSSQLStatement(CSSQLStatement.I_SQL_TYPE_SELECT, "IDE1_EXIMTRX.INVC_MASTER", "jdbc/eximt");
		sqlstatement.addField("COUNT(C_MAIN_REF) AS NUM", null, null);
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" WHERE ");

		String[] splictList=getParseRes(strGroupKey);
		Object[] iType = new Object[splictList.length];
		Object[] sValue = new Object[splictList.length];
		String opFld = null;
		String opFldName = null;
		String opFldValue = null;
		
		for(int i=0;i<splictList.length;i++){
			opFld = splictList[i];
			opFldName = fldMap.get(opFld.substring(0, 2));
			opFldValue = opFld.substring(3);
			sbSql.append(opFldName);
			if("AI_STAT_STAFF_CD".equals(opFldName)){
				sbSql.append("<>?");
			}else{
				sbSql.append("=?");
			}
			if(i!=splictList.length-1){
				sbSql.append(" AND ");
			}
			
			if("AI_ACT_AMT".equals(opFldName)||"CN_TTL_AMT".equals(opFldName)){
				iType[i]=Types.NUMERIC;
			}else if("AI_DUE_DT".equals(opFldName)){
				opFldValue=opFldValue.substring(0, 4)+"-"+opFldValue.substring(4, 6)+"-"+opFldValue.substring(6, 8);
				iType[i]=Types.DATE;
			}else{
				iType[i]=Types.VARCHAR;
			}
			sValue[i]=opFldValue;
		}
		sqlstatement.setClause(sbSql.toString(), sValue, iType);
		/**@Ref ISC-EE-0777 @author LOUIS @date 2015-10-12_S**/
		new HSBCDAOExec().executeQueryAsList(sqlstatement); 
		
	}

	private String[] getParseRes(String strGroupKey){
		char as1 = 1;
		String[] res=strGroupKey.split(String.valueOf(as1));
		return res;
	}
	
}
