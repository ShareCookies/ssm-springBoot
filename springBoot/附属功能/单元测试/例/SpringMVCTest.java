package ;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DocCommonWebConfiguration.class, DocCommonBusinessConfiguration.class})
public class SpringMVCTest {

    private MockMvc mvc;

    /**
     * web项目上下文
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        //获取mockmvc对象实例
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 根据条件查询模板
     * @throws Exception
     */
    @Test
    @WithUserDetails("test")
    public void getEgovTemplates() throws Exception {
        EgovTemplate egovTemplate = new EgovTemplate();
        egovTemplate.setModuleId("DISPATCH");
        egovTemplate.setType("MAIN");
        String requestJson = JSONObject.toJSONString(egovTemplate);
        mvc.perform(MockMvcRequestBuilders.post("/egovCommonContacts/getEgovCommonContactsGroup")
                //.content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
