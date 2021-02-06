package com.china.hcg.mvctest;

import com.china.hcg.test.BeanLife;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @autor hecaigui
 * @date 2021-2-3
 * @description
 */
@Controller
public class controller {
	@Resource
	BeanLife beanLife;

	@GetMapping("/test")
	public String login() {
		System.err.println(beanLife.getUserName());
		System.err.println(beanLife.getBeanProperty());
		return "";
	}
}
