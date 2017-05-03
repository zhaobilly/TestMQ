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

import com.cs.thread.TaskContainer;
import com.cs.thread.ThreadManager;
import com.ibm.mq.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.mq.MQC;

public class TestThread {
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
        for(int i=0;i<10;i++){
      	  TaskContainer task =  ThreadManager.getContainer();
      	  try {
				ThreadManager.execute(task);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      	  
        }
	}
	
}
