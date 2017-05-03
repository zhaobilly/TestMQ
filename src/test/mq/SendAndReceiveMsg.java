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
		MQEnvironment.hostname = "10.39.101.104";// MQ服务器IP
		MQEnvironment.channel = "CHAN_EE_SOA"; // 队列管理器对应的服务器连接通道
		MQEnvironment.CCSID = 1381; // 字符编码
		MQEnvironment.port = 1414; // 队列管理器的端口号
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY,MQC.TRANSPORT_MQSERIES_BINDINGS);
		MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
		try {
			qMgr = new MQQueueManager("MQ_EE_SOA");// 队列管理器名称
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
			// 建立Q1通道的连接
			queue = qMgr.accessQueue("TEST_R2EAI_FORWARD_RESPONSE", openOptions, null, null, null);
			MQMessage msg = new MQMessage();// 要写入队列的消息
			msg.format = MQC.MQFMT_STRING;
			msg.characterSet = 1381;
			msg.writeObject(msgStr); // 将消息写入消息对象中
			MQPutMessageOptions pmo = new MQPutMessageOptions();
			msg.expiry = -1; // 设置消息用不过期
			queue.put(msg, pmo);// 将消息放入队列
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

			System.out.println("该队列当前的深度为:" + queue.getCurrentDepth());
			System.out.println("===========================");
			int depth = queue.getCurrentDepth();
			// 将队列的里的消息读出来
			while (depth-- > 0) {
				MQMessage msg = new MQMessage();// 要读的队列的消息
				MQGetMessageOptions gmo = new MQGetMessageOptions();
				queue.get(msg, gmo);
				System.out.println("消息的大小为：" + msg.getDataLength());
				System.out.println("消息的内容：\n" + msg.readObject());
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
//			SRMsg.sendMsg("test第二条测试信息");
			SRMsg.receiveMsg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SRMsg.closeConnMQmanager();
	}
}
