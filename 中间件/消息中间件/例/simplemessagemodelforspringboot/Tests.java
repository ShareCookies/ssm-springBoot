package com.china.hcg.mq.simplemessagemodelforspringboot;


import com.china.hcg.ApplicationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



/**
 * @autor hecaigui
 * @date 2020-12-21
 * @description
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfiguration.class})
public class Tests {
	@Autowired
	private Sender sender;
	@Test
	public void hello() throws Exception {
		sender.send();
	}
}