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

public class TestMQ_JNDI {
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
//			SendMsg(getTestMsg());
//		}
//		long eTime = System.currentTimeMillis();
//		System.out.println(eTime - bTime);
//		getMsg();
//		BSTDispatcher 	bstd=new BSTDispatcher();
//		bstd.start();
		sendMSG2();
	}

	
	public void sendMSG2(){
		try{
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
			Context context = new InitialContext(props);
			QueueConnectionFactory qcf = (QueueConnectionFactory) context.lookup("jms/qcf/EE.Receive.QCF");//jms/q/EE.Receive.Q
			Queue queue = (Queue) context.lookup("jms/q/EE.Receive.Q");
			QueueConnection connection = qcf.createQueueConnection();
			QueueSession session1=connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			QueueSender queueSend =session1.createSender(queue);
			
			connection.start();
			Message message = session1.createTextMessage("test MQ v8.0");
			message.setStringProperty("JMS_IBM_Format", MQC.MQFMT_STRING);
			queueSend.send(message);
			if (connection != null) {
				connection.close();
				connection = null;
			}
			if (session1 != null) {
				session1.close();
				session1 = null;
			}
			if (queueSend != null) {
				queueSend.close();
				queueSend = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
