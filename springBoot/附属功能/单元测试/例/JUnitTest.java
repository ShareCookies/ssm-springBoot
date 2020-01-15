import com.china.hcg.eas.business.EasBusinessConfiguration;
import com.china.hcg.eas.business.base.security.user.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasBusinessConfiguration.class)
//@ContextConfiguration(classes = {UserMngImpl.class, UserMapper.class})
public class JUnitTest {
    @Autowired
    private UserMng userMng;

    @Test
    public void test() {
        System.out.println(("----- method test ------"));
        User user = new User();
        user.setName("1");
        user.setPassword("111");
        user.setEmail("11111");
        user.setDuty("0");
        userMng.registerByEmail(user);
//        //断言测试
//        String name = "yunfan";
//        Assert.assertEquals("yunfan22", name);
    }
}