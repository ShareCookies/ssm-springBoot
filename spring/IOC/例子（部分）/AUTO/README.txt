���ܣ�
	�ֱ�ʵ���������������ܣ�
		�Զ�����spring�����ļ���
		�Զ�װ�����ԡ�
����ʵ�ֲ��裺
	1.�Զ�����spring�����ļ�:
		���ܣ�
			��web.xml������ContextLoaderListener��
			ʵ��web��Ŀ����ʱ�Զ�װ��ApplicationContext��������Ϣ�������У�
			��ֱ������Spring���������bean�������ֶ�����ʵ����������
		ʵ�ֲ��裺
			web.xml���ã�
				<listener>
					<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				</listener>
				<context-param>
					<param-name>contextConfigLocation</param-name>
					<param-value>classpath:applicationContext.xml</param-value>
				</context-param>
		��ȡbean��
			���ܣ������м��������·�ʽ��ȡָ��beanֵ
			User user=(User)ContextLoaderListener.getCurrentWebApplicationContext().getBean("user")��
	2.�Զ�װ�䣺
		https://blog.csdn.net/heyutao007/article/details/5981555
		����:
			ʹ��ע��@Autowired ��
			�����Ա���������������캯�����б�ע��
			����Զ�װ��Ĺ�����
			�������������ļ�����bean������ע�룬
			��bean��������setget���ɽ������Ե�ע�롣
		ʵ�ֲ��裺
		applicationContext.xml��
			<!-- �� BeanPostProcessor ���Զ��Ա�ע @Autowired �� Bean ����ע�� -->     
			<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/> 
			<!-- ����Ҫ��������ע���bean��ע�������ֶ�����ע�룬spring���Զ�ע�� -->     
			<bean id="boss" class="com.baobaotao.Boss"/>    
		boss�ࣺ
			//Ҫע��������ø�ע�ͣ�spring�ͻ��Զ���������ע��ֵ
			@Autowired    
			private Car car;   
ע��*Test��βΪ�������