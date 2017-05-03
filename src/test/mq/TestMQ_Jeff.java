package test.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs.base.xml.XMLManager;
import com.cs.batch.thread.BatchThreadEngine;
import com.cs.batch.thread.IBatchThreadLogEngine;
import com.cs.batch.thread.IProcessSingleObject;
import com.cs.core.utility.StringUtil;
import com.cs.eximap.log.APLogExec;
import com.ibm.mq.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.mq.MQC;

public class TestMQ_Jeff {
	private static String realPath = null;
	private MQQueueManager qMgr;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		realPath = System.getProperty("user.dir");
		System.setProperty("CURRENT_RUNNING_MODE", "JUNIT");
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		// EECacheHook.getInstance().start();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.setProperty("user.dir", realPath);
	}

	@Test
	public void testDoTask() throws Exception {
		long bTime = 0;
		long eTime=0;
		
		bTime = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			SendMsg(getTestMsg());
		}
		eTime = System.currentTimeMillis();
		System.out.println(eTime - bTime);
		
		bTime = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
		    getMsg();
		}
		eTime = System.currentTimeMillis();
		System.out.println(eTime - bTime);
		//BSTDispatcher 	bstd=new BSTDispatcher();
		//bstd.start();
	}

	public String getTestMsg() {
		return "<bts><id>123456789</id><type>sssss</type><data><![CDATA[<jobs GAPIRule='' IIOPPort='2810' IPAddr='127.0.0.1'><job id='J00000000014'>J00000000014</job><job id='J00000000007'>J00000000007</job><job id='J00000000027'>J000000000027</job><job id='J00000000028'>J000000000028</job><job id='J00000000029'>J000000000029</job></jobs>]]></data></bts>";
	}

	public static int BNUM = 1;

	public void SendMsg(String msg) throws MQException {
		MQEnvironment.hostname = "127.0.0.1";
		MQEnvironment.channel = "CHAN_EE_SOA";
		MQEnvironment.CCSID = 1381;
		qMgr = new MQQueueManager("MQ_EE_SOA");
		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
		MQQueue queue = qMgr.accessQueue("TEST_R2EAI_SIMULATOR_REQUEST", openOptions, null, null, null);
		MQMessage hello = new MQMessage();
		try {
			hello.format = MQC.MQFMT_STRING;
			hello.characterSet = 1381;
			hello.writeString(msg);
			hello.expiry = -1;
			for (int i = 0; i < BNUM; i++) {
				queue.put(hello);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			queue.close();
			qMgr.disconnect();
		}
	}
	
	public String getMsg() throws Exception{
		MQEnvironment.hostname = "127.0.0.1";
		MQEnvironment.channel = "CHAN_EE_SOA";
		MQEnvironment.CCSID = 1381;
		qMgr = new MQQueueManager("MQ_EE_SOA");
		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
		MQQueue queue = qMgr.accessQueue("TEST_R2EAI_SIMULATOR_REQUEST", openOptions, null, null, null);
		MQMessage msg = new MQMessage();
		MQGetMessageOptions gmo = new MQGetMessageOptions( ); 
		String resMsg="";
		int i=0;
		try{
			queue.get(msg, gmo);
//			resMsg=msg.readString(msg.getDataLength());
//			while(StringUtil.isNotEmpty(resMsg)&&i++<1000){
//				proReqmsg(resMsg);
//				queue.get(msg, gmo);
//				resMsg=msg.readString(msg.getDataLength());
//			}
			queue.close();
			qMgr.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			queue.close();
			qMgr.disconnect();
		}
		return resMsg;
	}
	
	private void proReqmsg(String req){
		
		try {
			List<HashMap<String, String>> recs = new ArrayList<HashMap<String, String>>();
			Document  dom = XMLManager.xmlStrToDom(req);
			Element eRoot = dom.getDocumentElement();
			String jobId = XMLManager.getChildNodeValue(eRoot, "id", true);
			String type =  XMLManager.getChildNodeValue(eRoot, "type", true);
			String data =  XMLManager.getChildNodeValue(eRoot, "data", true);
			dom = null;
			dom = XMLManager.xmlStrToDom(data);
			HashMap<String, String> hm = XMLManager.convertChildElementsToMap(null, dom.getDocumentElement());
			recs.add(hm);
			
		//	process(recs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void process(List<HashMap<String, String>> recs) throws Exception {
		if (recs == null) {
			return;
		}
		BatchThreadEngine bte = new BatchThreadEngine(1, recs);
		bte.setLog(new IBatchThreadLogEngine() {
			@Override
			public void error(Object paramObject) {
				APLogExec.writeEEError(String.valueOf(paramObject));
			}

			@Override
			public void debug(Object paramObject) {
				// APLogExec.writeEEDebug(String.valueOf(paramObject));
			}
		});
		bte.run(new IProcessSingleObject() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(Object obj) throws Exception {
				HashMap<String, String> hmRec = (HashMap<String, String>) obj;
//				String JOB_ID = hmRec.get("JOB_ID");
//				System.out.println(JOB_ID);
//				String BTS_TYPE = hmRec.get("BTS_TYPE");
//				System.out.println(BTS_TYPE);
//				String JOB_DATA = hmRec.get("JOB_DATA");
//				System.out.println(JOB_DATA);
			}
		});
	}
}
