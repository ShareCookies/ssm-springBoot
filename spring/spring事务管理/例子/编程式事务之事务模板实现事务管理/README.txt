���ܣ�
	ʹ��TransactionTemplate�����б��ʽ�������
����ʵ�ֲ��裺
	1.��spring��������bean���壺
		1.�����������2.TransactionTemplateģ��
	2.����һ����
		����:
			��������transactionTemplate.execute()���������񲢶����ݿ������
		����:
			DataSource,TransactionManager,TransactionTemplate
		����:
			transactionTemplate.execute()
				�÷�����Ҫ����:
					ʵ����TransactionCallback�ص��ӿڻ�TransactionCallbackWithoutResult�ص��ӿڵ�����������
					�ڽӿڹ涨�ķ����в������ݿ⡣
				
	3.�ù�������2���ִ࣬����ͷ��������ʵ������
ע��
	1.*Test��βΪ�������
	
	2.����ع�ʧ�ܣ��ֶ��ع�������ʾ�ظ��ύ�����Ϊ����Ļع����ύȨ�����������ϣ�
	���ǲ���catchס������������ģ��Ͳ�׽���������޷��ع��ˡ������catch�����ˣ�
	https://blog.csdn.net/liaohaojian/article/details/70139151
	https://www.ibm.com/developerworks/cn/education/opensource/os-cn-spring-trans/