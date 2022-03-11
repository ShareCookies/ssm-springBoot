package com.china.hcg.test;

import org.springframework.stereotype.Component;

/**
 * @autor hecaigui
 * @date 2021-2-3
 * @description
 */
@Component
public class BeanLifeProp {

	public BeanLifeProp() {
		System.out.println("BeanLifePropg构造器");
	}
}
