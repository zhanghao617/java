package com.zh.text;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by Administrator on 2017/11/22.
 */
public class ActiveMQText {

    /**
     * 发送消息
     * @throws JMSException
     */
    @Test
    public void sendMessageToQuere() throws JMSException {
        //创建链接工厂
        String brokerUrl = "tcp://WIN10-711141915:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        //创建链接
        Connection connection = connectionFactory.createConnection();
        //开启链接
        connection.start();
        //创建会话 第一个值表示是否开启事务，第二个表示是否自动签收
        Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
        //创建目的地对象
        Destination destination = session.createQueue("text-Message");
        //创建生产者
        MessageProducer messageProducer = session.createProducer(destination);
        //持久化模式
        //messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        for (int i = 4; i <= 9; i++) {
            //创建消息
            TextMessage textMessage = session.createTextMessage("Hello,ActiveMQ-" + i);
            //发送消息
            messageProducer.send(textMessage,DeliveryMode.PERSISTENT,i,0);

        }
        //提交事务
        session.commit();
        //释放资源
        messageProducer.close();
        session.close();
        connection.close();
    }

    @Test
    public void consumerMessageFromQueue() throws JMSException, IOException {
        //创建链接工厂
        String brokerUrl = "tcp://WIN10-711141915:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        //创建链接
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //创建会话  第一个值表示是否开启事务，第二个表示是否自动签收
        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        //创建目的地对象
        Destination destination = session.createQueue("text-Message");
        //创建消费者
        MessageConsumer messageConsumer = session.createConsumer(destination);
        //消费消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(">>>>>>" +textMessage.getText());
                    //手动签收
                    textMessage.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();
        //释放资源
        messageConsumer.close();
        session.close();
        connection.close();
    }


    /**
     * 演示重试机制：rollback
     * @throws JMSException
     * @throws IOException
     */
    @Test
    public void consumerMessageFromQueue1() throws JMSException, IOException {
        //创建链接工厂
        String brokerUrl = "tcp://WIN10-711141915:61616";
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        //创建链接
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //创建会话  第一个值表示是否开启事务，第二个表示是否自动签收
        Session session = connection.createSession(true,Session.CLIENT_ACKNOWLEDGE);
        //创建目的地对象
        Destination destination = session.createQueue("text-Message");
        //创建消费者
        MessageConsumer messageConsumer = session.createConsumer(destination);
        //消费消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    if("Hello,ActiveMQ-7".equals(text)) {
                        throw new JMSException("故意的异常");
                    }
                    System.out.println(">>>>>>" +text);
                    session.commit();
                } catch (JMSException e) {
                    e.printStackTrace();
                    try {
                        session.rollback();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        System.in.read();
        //释放资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
