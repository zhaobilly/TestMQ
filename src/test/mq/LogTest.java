package test.cs;

import com.cs.base.log.CSLogger;
import com.cs.eximap.log.LogEnv;

public class LogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("user.dir", "E:\\HSBC_DEVE_T16R1_DEV_20151109_R16.1\\EE_Parameter\\EEPARA");
		CSLogger logger = LogEnv.getLogger("BATCH_TASK");
	}

}
