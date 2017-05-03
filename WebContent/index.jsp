<%@page import="javax.jms.BytesMessage"%>
<%@page import="javax.jms.Message"%>
<%@page import="javax.jms.ObjectMessage"%>
<%@page import="javax.jms.Queue"%>
<%@page import="javax.jms.QueueConnection"%>
<%@page import="javax.jms.QueueConnectionFactory"%>
<%@page import="javax.jms.QueueSender"%>
<%@page import="javax.jms.QueueSession"%>
<%@page import="javax.jms.TextMessage"%>
<%@page import="javax.naming.Context"%>
<%@page import="com.ibm.mq.MQC"%>
<%@page import="java.util.Properties"%>
<%@page import="javax.naming.InitialContext"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>test MQ</title>
<%
String proInfo="Test success!!!!!!!!!"
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
	proInfo = e.getMessage();
	e.printStackTrace();
}
%>
</head>
<body>
<h1>Test MQ v8.0(JNDI) Result:::</h1><br>
<%=proInfo %>
</body>
</html>