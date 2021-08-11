package com.china.hcg.eas.business.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.util.concurrent.*;

/**
 * @autor hecaigui
 * @date 2020-5-30
 * @description
 */
@Service
public class EmailUtils {
    //
    static ExecutorService emailThreadPool = new ThreadPoolExecutor(1, 100, 0L, TimeUnit.MICROSECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("邮件发送用的线程池");
            System.out.println("创建线程："+t.getName()+t.getId());
            return  t;
        }
    });
    class mailRunnable implements Runnable{
       JavaMailSender mailSender;
       SimpleMailMessage simpleMailMessage;
       mailRunnable(JavaMailSender mailSender, SimpleMailMessage simpleMailMessage){
            this.mailSender = mailSender;
            this.simpleMailMessage = simpleMailMessage;
        }
        @Override
        public void run(){
            mailSender.send(simpleMailMessage);
        }
    }
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String emailFrom;
    /**
     * @description 发送简易邮件
     * @return
     */
    public void sendSimpleMail(String receiverEamil , String emailContent , String emailSubject) throws MessagingException {
        if (StringUtils.isBlank(emailSubject)){
            emailSubject = "来自学家系统的回复";
        }
        //简单邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setTo(receiverEamil);
        simpleMailMessage.setSubject(emailSubject);
        simpleMailMessage.setText(emailContent);
        EmailUtils.emailThreadPool.execute(new mailRunnable(mailSender,simpleMailMessage));
    }
}
