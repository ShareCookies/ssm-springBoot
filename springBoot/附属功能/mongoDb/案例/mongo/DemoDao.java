package com.china.hcg.eas.business.mongo;

/**
 * @autor hecaigui
 * @date 2020-5-7
 * @description
 */
public interface DemoDao {

    void saveDemo(DemoEntity demoEntity);

    void removeDemo(Long id);

    void updateDemo(DemoEntity demoEntity);

    DemoEntity findDemoById(Long id);
}