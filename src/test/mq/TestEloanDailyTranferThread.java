package test.cs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.cs.batch.tasks.IABatchIntAccTask;
import com.cs.batch.tasks.IABatchPostingTask;
import com.cs.batch.utility.TaskManager;
import com.cs.hsbc.ee.ap.mmm.AttachTp6SettAmt4Report;
import com.cs.hsbc.ee.ap.mmm.util.MoveMoneyDBUtil;

public class TestEloanDailyTranferThread 
{
	
	private static String realPath = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
//		EECacheHook.getInstance().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@Test
	public void test() throws Exception{
		String[] dataArray={"AIINAP1603000002","2016-04-01","2016-04-04"};
 		List<HashMap<String, String>> dataList=MoveMoneyDBUtil.getEloanVoucherDtal(dataArray, "HSBC", "IN", "2016-04-04", "INAP");
 		dataList=sortVoucherDataList(dataList);
 		addMMMMsgAddtionalData(dataList,"eppid","buyerId");
	}
	
	
	
	private List<HashMap<String, String>> sortVoucherDataList(List<HashMap<String, String>> dataList) {
		if (dataList.size() > 2) {
			List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> debitEntry = findVoucherEntry(dataList, "D", null);
			while (debitEntry != null) {
				retList.add(debitEntry);
				retList.add(findVoucherEntry(dataList, "C", debitEntry));
				debitEntry = findVoucherEntry(dataList, "D", null);
			}
			return retList;
		}else{
			return dataList;
		}
	}
	
	
	private HashMap<String, String> findVoucherEntry(List<HashMap<String, String>> dataList, String entryType,
			HashMap<String, String> debitMap) {
		HashMap<String, String> entryMap = null;
		for (int i = 0; i < dataList.size(); i++) {
			HashMap<String, String> map = dataList.get(i);
			String entryTypeTemp = map.get("C_DC_FLAG");
			if ("D".equals(entryType) && "D".equals(entryTypeTemp)) {
				entryMap = map;
				dataList.remove(i);
				break;
			}
			if ("C".equals(entryType) && "C".equals(entryTypeTemp)) {
				String cAmt = map.get("N_AMT");
				String cEventTime = map.get("I_EVENT_TIMES");
				String dAmt = debitMap.get("N_AMT");
				String dEventTime = debitMap.get("I_EVENT_TIMES");
				if (cAmt.equals(dAmt) && cEventTime.equals(dEventTime)) {
					entryMap = map;
					dataList.remove(i);
					break;
				}
			}
		}
		return entryMap;
	}
	
	
	private void addMMMMsgAddtionalData(List<HashMap<String, String>> dataList, String eppId, String buyerId) {
		String dcFlag = null;
		String eloanEventTime = null;
		String eLoanType = null;
		String lstrAISPID = "AI_SP_ID";
		String lstrBuBrId = "BU_BR_ID";
		for (Map<String, String> recMap : dataList) {
			dcFlag = recMap.get("C_DC_FLAG");
			eloanEventTime = recMap.get("I_EVENT_TIMES");
			eLoanType = "eLoanType";
			if ("D".equalsIgnoreCase(dcFlag)) {
				recMap.put("C_VCH_SPECIAL1", "GL");
				recMap.put("C_VCH_SPECIAL2", "10251");
				recMap.put("C_VCH_SPECIAL4", "GL_NTRY_TP");
				recMap.put("C_VCH_SPECIAL6", "");
			} else {
				recMap.put("C_VCH_SPECIAL1", "IE");
				recMap.put("C_VCH_SPECIAL2", "20251");
				recMap.put("C_VCH_SPECIAL4", "");
				recMap.put("C_VCH_SPECIAL6", "INCM_EXPNSS_ITM_CD");
			}
			recMap.put("C_VCH_SPECIAL5", lstrAISPID);
			recMap.put("C_VCH_SPECIAL11", eppId);
			recMap.put("C_VCH_SPECIAL14", lstrBuBrId);
		}
	}
}
