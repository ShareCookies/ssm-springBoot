
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.charset.Charset;

/**
 * @author 
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
@MapperScan({"com.ttt.egov.zjsgy.platform.mapper"})
@Import({UserWebConfiguration.class, DocWebConfiguration.class, UrgerWebConfiguration.class,
        InfoWebConfiguration.class, MessageClientBusinessConfiguration.class, MessageWebConfiguration.class,
        MeetingWebConfiguration.class, RequestWebConfiguration.class, NoticeWebConfiguration.class, ApprovalWebConfiguration.class, AuditConfiguration.class,
        SolrDataWebConfiguration.class, ReportWebConfiguration.class, ElasticJobConfiguration.class, FlowEventListenerApplication.class, AuditLogBusinessConfiguration.class
})
public class Application {

    public static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("application default encoding: {}", System.getProperty("file.encoding"));
        logger.info("application default character encoding: {}", Charset.defaultCharset().name());
        logger.info("application default language: {}", System.getProperty("user.language"));
        SpringApplication.run(Application.class, args);
    }

    @Bean
    IgnoredPathsWrapper oaServiceWebIgnoredPathsWrapper() {
        String[] paths = {"/sync/getDatas", "/egovAtt/downloadEgovAttFile", "/openApi/wflow/getTodoAndToRead",
                "/openApi/wflow/getTodoAndToReadCountList", "/openApi/receival/receiveDispatchCountWithUnit", "/openApi/receival/receiveDispatchWithUnit",
                "/openApi/receival/getReceiveSignForPermission", "/userCenter/createTokenid", "/userCenter/userAuthorization",
                "/openApi/urgerTaskSignUnit/getUrgerTaskSignByMap4Page", "/openApi/urger/setAppointmenJusticeMsg"};
        return new IgnoredPathsWrapper(paths);
    }

    @Bean
    IgnoredPathsWrapper mobileThemeIgnoredPathsWrapper() {
        return new IgnoredPathsWrapper("/mobileTheme/getMobileHomeData", "/userCenter/getZzjgByTime");
    }

    /**
     * 注意 initMethod = "init" 参数是必须的
     * 方法名为 小写开头job名称 + Scheduler
     *
     * @param registryCenter
     * @param todoOverdueJob 更新
     * @return
     */
    @Bean(initMethod = "init")
    public SpringJobScheduler todoOverdueJobScheduler(CoordinatorRegistryCenter registryCenter, TodoOverdueJob todoOverdueJob) {
        // 定义作业核心配置 newBuilder的第1个参数为 小写开头job名称， 第2个参数为cron表达式， 第3个参数为分片数量， 同步骤4简单用法用1即可，高级用法见官方文档
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder("todoOverdueJob", "0 0/30 * * * ? *", 1).build();
        // 定义SIMPLE类型配置 固定写法， XxlJob换成你的Job类名
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, TodoOverdueJob.class.getCanonicalName());
        // 定义Lite作业根配置 固定写法，高级用法见官方文档
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        return new SpringJobScheduler(todoOverdueJob, registryCenter, liteJobConfiguration);
    }

    @Bean(initMethod = "init")
    public SpringJobScheduler todoReminJobScheduler(CoordinatorRegistryCenter registryCenter, TodoReminJob todoReminJob) {
        // 定义作业核心配置 newBuilder的第1个参数为 小写开头job名称， 第2个参数为cron表达式， 第3个参数为分片数量， 同步骤4简单用法用1即可，高级用法见官方文档
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder("todoReminJob", "0 0/30 * * * ? *", 1).build();
        // 定义SIMPLE类型配置 固定写法， XxlJob换成你的Job类名
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, TodoReminJob.class.getCanonicalName());
        // 定义Lite作业根配置 固定写法，高级用法见官方文档
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        return new SpringJobScheduler(todoReminJob, registryCenter, liteJobConfiguration);
    }

    @Bean(initMethod = "init")
    public SpringJobScheduler todoCountRemindJobScheduler(CoordinatorRegistryCenter registryCenter, TodoCountRemindJob todoCountRemindJob) {
        // 定义作业核心配置 newBuilder的第1个参数为 小写开头job名称， 第2个参数为cron表达式， 第3个参数为分片数量， 同步骤4简单用法用1即可，高级用法见官方文档
        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder("todoCountRemindJob", "0 * * * * ? *", 1).build();
        // 定义SIMPLE类型配置 固定写法， XxlJob换成你的Job类名
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, TodoCountRemindJob.class.getCanonicalName());
        // 定义Lite作业根配置 固定写法，高级用法见官方文档
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(true).build();
        return new SpringJobScheduler(todoCountRemindJob, registryCenter, liteJobConfiguration);
    }


}
