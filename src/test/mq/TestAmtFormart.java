package test.cs;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cs.base.log.CSLogger;
import com.cs.base.xml.XMLManager;
import com.cs.core.dao.DSManager;
import com.cs.core.utility.CSEEDAOHelper;
import com.cs.core.utility.DataType;
import com.cs.core.utility.SchemaUtil;
import com.cs.ee.core.cache.EECache;
import com.cs.ee.core.cache.EECacheHook;
import com.cs.ee.core.cache.EECacheManager;
import com.cs.eximap.log.LogEnv;
import com.cs.eximap.utility.CSSQLStatement;
import com.cs.eximap.utility.TransactionUtil;
import com.cs.hsbc.cache.CCYDecimalCache;
import com.cs.hsbc.cache.HSBCCommonCacheMar;
import com.cs.hsbc.ee.ap.compliance.util.ComplianceCnvtUtil;
import com.cs.hsbc.ee.ap.report.ReportService;
import com.cs.hsbc.ee.utility.CommServiceUtil;

public class TestAmtFormart 
{
	
	public static ComplianceCnvtUtil complianceCnvtUtil=null;
	private static String realPath = null;
	private static CCYDecimalCache ccyCache = null;
	private static String strBankId=null;
	private static String strCntryCode=null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		strBankId="HSBC";
		strCntryCode="US";
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		ccyCache = HSBCCommonCacheMar.getInstance().getCCYDecimalCache(strBankId, strCntryCode);
		EECacheHook.getInstance().start();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testDoTask() throws Exception{
		Document trxDoc =XMLManager.xmlFileToDom("C:\\Users\\Administrator\\Desktop\\EPR.xml");
		Document dom=amtFormat(trxDoc);
		System.out.println(XMLManager.convertToString(dom));
	}

	
	public Document amtFormat(Document trxDoc) throws Exception {
		Node functionInfo = XMLManager.selectSingleNode(trxDoc, "/root/pubInfo/functionInfo");
		Node elFuncInfo = XMLManager.findChildNode(functionInfo, "funcInfo");
		String strFuncID = XMLManager.getChildNodeValue(elFuncInfo, "ID", true);
		String strModule = XMLManager.getChildNodeValue(elFuncInfo, "MODULE", true);
		EECache amtCache = EECacheManager.getCache(EECacheManager.CACHE_WEB_AP_AMTFMT);
		Document amt = (Document) amtCache.getAPAMTFormatCache(strBankId, strCntryCode, strFuncID);
		Document dmMdlAmt = (Document) amtCache.getWebAMTFormatCache(strBankId, strCntryCode, strModule);
		if (amt == null && dmMdlAmt == null)
			return trxDoc;
		Node domData = XMLManager.findChildNode(trxDoc.getDocumentElement(), "domData");
		HashMap<String, String> amtMap = new HashMap<String, String>();
		if (dmMdlAmt != null) {
			amtMap = XMLManager.convertToHashMap(dmMdlAmt);
		}
		HashMap<String, String> tempamtMap = XMLManager.convertToHashMap(amt);
		Iterator iterAmt = tempamtMap.entrySet().iterator();
		while (iterAmt.hasNext()) {
			Map.Entry entry = (Map.Entry) iterAmt.next();
			String strKey = (String) entry.getKey();
			String strValue = (String) entry.getValue();
			amtMap.put(strKey, strValue);
		}
		HashMap amtDOMap = TransactionUtil.getDOAmtFormat(trxDoc);
		NodeList domNodes = domData.getChildNodes();
		int length = domNodes.getLength();
		Node domNode = null;
		String nodeName = null;
		String valueName = null;
		String nodeValue = null;
		String ccyName = null;
		for (int count = 0; count < length; count++) {
			domNode = domNodes.item(count);
			if (domNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			nodeName = domNode.getNodeName();
			String isDOFlag = XMLManager.getNodeAttribute(domNode, "isDO");
			if ("T".equals(isDOFlag)) {
				if (amtDOMap.containsKey(nodeName)) {
					HashMap amtDOMapForField = (HashMap) amtDOMap.get(nodeName);
					Iterator iter = amtDOMapForField.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						String strAmtName = (String) entry.getKey();
						String strCCYName = (String) entry.getValue();
						NodeList ndAmtList = ((Element) domNode).getElementsByTagName(strAmtName);
						String strCCYValue = XMLManager.getChildNodeValue(domNode, strCCYName, true);
						for (int ndAmtCount = 0; ndAmtCount < ndAmtList.getLength(); ndAmtCount++) {
							Node nodeAmtTemp = ndAmtList.item(ndAmtCount);
							String nodeAmtTempVaule = XMLManager.getNodeValue(nodeAmtTemp, true);
							nodeAmtTempVaule = outputFormat(nodeAmtTempVaule, strCCYValue);
							XMLManager.setNodeValue(trxDoc, nodeAmtTemp, nodeAmtTempVaule);
						}

					}
				}
			}
			Node nodeTemp = domNode.getFirstChild();
			if (nodeTemp == null) {
				continue;
			}
			nodeValue = nodeTemp.getNodeValue();
			if (amtMap.containsKey(nodeName)) {
				valueName = (String) amtMap.get(nodeName);
				ccyName = XMLManager.getChildNodeValue(domData, valueName, true);
				nodeValue = outputFormat(nodeValue, ccyName);
				domNode.getFirstChild().setNodeValue(nodeValue);
			}
		}
		
		return trxDoc;
	}
	
	
	
	private String outputFormat(String amtValue, String accyValue) throws Exception {
		try {
			if (CommServiceUtil.isEmpty(amtValue)) {
				amtValue = "0";
			}
			amtValue = amtValue.trim();
			amtValue = DataType.clearNumberChar(amtValue);
			int amtDecimal = this.ccyCache.getCCYAmtDecimal(accyValue, "HBUS", this.strCntryCode);
			BigDecimal dbAmt = new BigDecimal(amtValue);
			return String.valueOf(dbAmt.setScale(amtDecimal, RoundingMode.HALF_UP));
		} catch (Exception e) {
			throw e;
		}
	}
	
}
