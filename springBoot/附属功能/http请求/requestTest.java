
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @autor hecaigui
 * @date 2020-1-19
 * @description
 */
public class requestTest {
    public static void get(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity("http://www.baidu.com/s?ie=UTF-8&wd=1", String.class);
        HttpStatus statusCode = entity.getStatusCode();
        System.out.println("statusCode.is2xxSuccessful()"+statusCode.is2xxSuccessful());
        String body = entity.getBody();
        System.out.println("entity.getBody()"+body);
    }
    public static void main(String[] args){
        requestTest.get();
    }
}
