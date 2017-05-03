package test.mq;
import com.ibm.mq.*;
public class SendMsg {
	private MQQueueManager qMgr;
	
	public static void main(String args[]){
		new SendMsg();
	}
	
   public SendMsg()
   {
	   MQEnvironment.hostname="127.0.0.1";//����IP
	   MQEnvironment.channel="CHAN_EE_SOA";//����ͨ�ŵ�ͨ��
	   MQEnvironment.CCSID =1381;
	   try{
		   qMgr=new MQQueueManager("MQ_EE_SOA");
		   int openOptions=MQC.MQOO_INPUT_AS_Q_DEF|MQC.MQOO_OUTPUT|MQC.MQOO_INQUIRE;
		   MQQueue queue=qMgr.accessQueue("CE_2_EE_RESP",openOptions,null,null,null);
		   MQMessage hello=new MQMessage();
		   try{
				hello.format=MQC.MQFMT_STRING ;
				hello.characterSet=1381 ;
				hello.writeString("���ǲ��ԣ�");
			}catch(java.io.IOException ex){}
		   finally{};
		   hello.expiry=-1;
		   queue.put(hello);
		   queue.close() ;
		   qMgr.disconnect() ;
	   }catch(Exception e){
		   
	   }
   }
}
