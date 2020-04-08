package cn.newhope.batch.util;



import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

    private static Logger logger= LoggerFactory.getLogger(HttpUtil.class);

    public static String postJson(String url, String body) throws Exception {
        return post(url, body, "application/json");
    }

    public static String postForm(String url, String body) throws Exception {
        return post(url, body, "application/x-www-form-urlencoded");
    }

    public static String post(String url, String body, String contentType) throws Exception {

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(1000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", contentType);
        httpPost.setEntity(new StringEntity(body, Charset.forName("UTF-8")));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, "UTF-8");

        response.close();
        httpClient.close();
        return responseContent;
    }

    public static String sendHttpPost(String url, String body,String authorization) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization",authorization);
        httpPost.setEntity(new StringEntity(body, Charset.forName("UTF-8")));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        logger.info(response.getStatusLine().getStatusCode() + "\n");
        String responseContent="";
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            logger.info(responseContent);
        }
        response.close();
        httpClient.close();
        return responseContent;
    }
}
