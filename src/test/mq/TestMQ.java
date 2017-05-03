package test.cs;

import java.util.Properties;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.ibm.mq.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.mq.MQC;

public class TestMQ {
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
//		long bTime = System.currentTimeMillis();
//		for (int i = 0; i < 1; i++) {
//
			SendMsg(getTestMsg());
//		}
//		long eTime = System.currentTimeMillis();
//		System.out.println(eTime - bTime);
//		getMsg();
//		BSTDispatcher 	bstd=new BSTDispatcher();
//		bstd.start();
	}

	public String getTestMsg() {
		return "ssssss";
	}

	public static int BNUM = 5;

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
		try{
			queue.get(msg, gmo);
			queue.close();
			qMgr.disconnect();
			resMsg=msg.readString(msg.getDataLength());
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			queue.close();
			qMgr.disconnect();
		}
		return resMsg;
	}
	
}
