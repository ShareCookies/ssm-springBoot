package com.china.hcg.mq;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * @autor hecaigui
 * @date 2022-2-15
 * @description
 */
public class ConsumerRabbitmqJava {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("192.168.0.40");
        factory.setPort(5672);
        //建立到代理服务器到连接
        Connection conn = factory.newConnection();
        //创建信道
        final Channel channel = conn.createChannel();
        //声明交换器
        String exchangeName = "hello-exchange2";
        channel.exchangeDeclare(exchangeName, "direct", true);
        //声明队列
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "hola";
        //绑定队列，通过键 hola 将队列和交换器绑定起来
        //？消费者只能绑定一个队列
        channel.queueBind(queueName, exchangeName, routingKey);

        while(true) {
            //消费消息
            boolean autoAck = false;
            //标识当前消费者吗？
            String consumerTag = "";
            //基础消费
            //？不断的进行消费 那是任意一个消费请求接收到消息 然后进行处理吗 那其他的了
            //所以具体消费与服务器是怎么进行的了?
            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                //接收到消息的回调处理
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,//？
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("消费的路由键：" + routingKey);
                    //?
                    System.out.println("消费的内容类型：" + contentType);
                    //ack确认消息
                    long deliveryTag = envelope.getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    System.out.println("消费的消息体内容：");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println(bodyStr);
                }
            });
        }
    }
}
