package aop;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Config.class)
public class JUnitTest {
    @Autowired
    private Target target;

    @Test
    public void test() {
        target.m2(666,777);
    }
}