
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

exchange:
        String resultJsonStr = "";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json;charset=UTF-8");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, requestHeaders);

        ResponseEntity<String> responseEntity=restTemplate.exchange(url, HttpMethod.GET,requestEntity, String.class);
        resultJsonStr=responseEntity.getBody();
post：
		private JSONObject  postForObj (String postUrl , JSONObject postObj){
			RestTemplate restTemplate = new RestTemplate();
			String remoteHost = ExCommon.getRequestUrl(this.rmsParamDao, "remoteHost");
			String url = remoteHost+postUrl;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			HttpEntity request = new HttpEntity<>(postObj, headers);
			ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
			System.out.println(response.getBody());
			JSONObject jsonResult = JSON.parseObject(response.getBody());
			return jsonResult;
		}
		请求参数格式：
		post headers{Content-Type：application/json}
		参数为对象：
			{
				"fileUrl":"http://192.168.210.171:6051/egovAtt/downloadEgovAttFile?id=Xp1CAeD_o7WsO1N9",
				"fileName":"417督办类型数据修改",
				"ext":"doc",
				"docId":"XpaEO4SuklmVoZFO"
			}