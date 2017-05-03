package test.cs;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.cs.batch.thread.BatchThreadEngine;
import com.cs.batch.thread.IBatchThreadLogEngine;
import com.cs.batch.thread.IProcessSingleObject;
import com.cs.core.dao.HSBCDAOExec;
import com.cs.eximap.log.APLogExec;
import com.cs.eximap.utility.sqlprepare.CSSQLPrepMultiObj;
import com.cs.eximap.utility.sqlprepare.CSSQLPrepObj;

public class BSTDispatcher {

	public void start() {
		try {
			long l = System.currentTimeMillis();
			CSSQLPrepMultiObj multipleSQL = this.generateSQLObj();
			HSBCDAOExec hsbcDao = new HSBCDAOExec(200, true);
			boolean hasNextKey = true;
			while (hasNextKey) {
				List<HashMap<String, String>> recs = hsbcDao.queryForUpdateMultiRecord(multipleSQL);
				if (recs == null || recs.size() == 0) {
					hasNextKey = false;
				} else {
					//process(recs);
				}
				recs.clear();
				recs = null;
			}
			System.out.println(System.currentTimeMillis() - l);
		} catch (Exception e) {
			APLogExec.writeEEError(e);
		}

	}

	private void process(List<HashMap<String, String>> recs) throws Exception {
		if (recs == null) {
			return;
		}
		BatchThreadEngine bte = new BatchThreadEngine(10, recs);
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

	private CSSQLPrepMultiObj generateSQLObj() {
		CSSQLPrepMultiObj multiObj = new CSSQLPrepMultiObj();
		multiObj.setDs("jdbc/eximt");
		CSSQLPrepObj<Integer, String> selUpdSql = new CSSQLPrepObj<Integer, String>();
		selUpdSql
				.addPrepareSql("SELECT JOB_ID,BTS_TYPE,JOB_PROCESS_STATUS,JOB_DATA FROM IDE1_EXIMSYS.SYS_BTS_JOBS WHERE JOB_PROCESS_STATUS='I' FOR UPDATE SKIP LOCKED ");
		multiObj.addSqlIndiviObjList(selUpdSql);

		CSSQLPrepObj<Integer, String> updSql = new CSSQLPrepObj<Integer, String>();
		String UPD_SQL = "UPDATE IDE1_EXIMSYS.SYS_BTS_JOBS SET JOB_PROCESS_STATUS ='P' WHERE JOB_ID=? ";
		updSql.addPrepareSql(UPD_SQL.toString());
		List<String> listV = new ArrayList<String>();
		listV.add("JOB_ID");
		updSql.addPrepareValueObjList(listV);
		List<Integer> listT = new ArrayList<Integer>();
		listT.add(Types.VARCHAR);
		updSql.addPrepareTypeObjList(listT);

		multiObj.addSqlIndiviObjList(updSql);
		return multiObj;
	}

}
