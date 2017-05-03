package test.cs;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
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

public class TestEloanDailyTransfer 
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
		 doTest();
	}
	
	
	public void doTest(){
		try{
			startAccTask();
			startPostTask();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void startAccTask(){
		try{
			IABatchIntAccTask accTask = new IABatchIntAccTask();
			accTask.setBaseInfo("HSBC", "HK", "HBAP");
			accTask.setBusinessDay(null);
			Document taskInfo = TaskManager.getInstance().getTaskInfo("T00000000058");
			accTask.setTaskInfo(taskInfo);
			accTask.run();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void startPostTask(){
		try{
			IABatchPostingTask accTask = new IABatchPostingTask();
			accTask.setBaseInfo("HSBC", "HK", "HBAP");
			accTask.setBusinessDay(null);
			Document taskInfo = TaskManager.getInstance().getTaskInfo("T00000000059");
			accTask.setTaskInfo(taskInfo);
			accTask.run();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
