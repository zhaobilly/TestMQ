package test.mq;

import java.io.IOException;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class SendAndReceiveMsg {
	private MQQueueManager qMgr;

	private void getConnMQmanager() {
		MQEnvironment.hostname = "10.39.101.104";// MQ������IP
		MQEnvironment.channel = "CHAN_EE_SOA"; // ���й�������Ӧ�ķ���������ͨ��
		MQEnvironment.CCSID = 1381; // �ַ�����
		MQEnvironment.port = 1414; // ���й������Ķ˿ں�
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,MQC.TRANSPORT_MQSERIES_BINDINGS);
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
		try {
			qMgr = new MQQueueManager("MQ_EE_SOA");// ���й���������
		} catch (MQException e) {
			e.printStackTrace();
		}
	}

	private void closeConnMQmanager() {
		if (qMgr != null) {
			try {
				qMgr.close();
			} catch (MQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendMsg(String msgStr) {
		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
		MQQueue queue = null;
		try {
			// ����Q1ͨ��������
			queue = qMgr.accessQueue("TEST_R2EAI_FORWARD_RESPONSE", openOptions, null, null, null);
			MQMessage msg = new MQMessage();// Ҫд����е���Ϣ
			msg.format = MQC.MQFMT_STRING;
			msg.characterSet = 1381;
			msg.writeObject(msgStr); // ����Ϣд����Ϣ������
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			msg.expiry = -1; // ������Ϣ�ò�����
			queue.put(msg, pmo);// ����Ϣ�������
		} catch (MQException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (queue != null) {
				try {
					queue.close();
				} catch (MQException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void receiveMsg() {
		int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_INQUIRE;
		MQQueue queue = null;
		try {
			queue = qMgr.accessQueue("TEST_R2EAI_SIMULATOR_REQUEST", openOptions, null, null, null);

			System.out.println("�ö��е�ǰ�����Ϊ:" + queue.getCurrentDepth());
			System.out.println("===========================");
			int depth = queue.getCurrentDepth();
			// �����е������Ϣ������
			while (depth-- > 0) {
				MQMessage msg = new MQMessage();// Ҫ���Ķ��е���Ϣ
				MQGetMessageOptions gmo = new MQGetMessageOptions();
				queue.get(msg, gmo);
				System.out.println("��Ϣ�Ĵ�СΪ��" + msg.getDataLength());
				System.out.println("��Ϣ�����ݣ�\n" + msg.readObject());
				System.out.println("---------------------------");
			}
		} catch (MQException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (queue != null) {
				try {
					queue.close();
				} catch (MQException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		SendAndReceiveMsg SRMsg = new SendAndReceiveMsg();
		SRMsg.getConnMQmanager();
		try {
//			SRMsg.sendMsg("aaaaaaa");
//			SRMsg.sendMsg("test�ڶ���������Ϣ");
			SRMsg.receiveMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SRMsg.closeConnMQmanager();
	}
}
