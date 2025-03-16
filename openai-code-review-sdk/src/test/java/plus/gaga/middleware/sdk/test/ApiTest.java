package plus.gaga.middleware.sdk.test;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.junit.Test;
import plus.gaga.middleware.sdk.domain.model.ChatCompletionSyncResponse;
import plus.gaga.middleware.sdk.types.utils.BearerTokenUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApiTest {

    public static void main(String[] args) {
        String apiKeySecret = "c78fbacd3e10118ad5649d7a54a3a163.UunYDBxpzeClvSKZ";
        String token = BearerTokenUtils.getToken(apiKeySecret);
        System.out.println(token);
    }


    @Test
    public void use_offical_sdk_test(){
        ObjectMapper mapper = new ObjectMapper();
        ClientV4 client = new ClientV4.Builder("6a0b70d50b68451da5f4995f7fc18128.xgAUpWMFT50Kt9wt").build();
        String apiKeySecret = "6a0b70d50b68451da5f4995f7fc18128.xgAUpWMFT50Kt9wt";
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "我正在做api调用测试，如果你收到我的消息，请告诉我你已经收到消息");
        messages.add(chatMessage);
        String requestId = String.format("codeAutoReveiwTest", System.currentTimeMillis());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4Flash)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);

        try {
            System.out.println("model output:" + invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test_http() throws IOException {
        String apiKeySecret = "6a0b70d50b68451da5f4995f7fc18128.xgAUpWMFT50Kt9wt";
        String token = apiKeySecret;
//        String token = BearerTokenUtils.getToken(apiKeySecret);

        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        String code = "1+1";

        String jsonInpuString = "{"
                + "\"model\":\"glm-4-flash\","
                + "\"messages\": ["
                + "    {"
                + "        \"role\": \"user\","
                + "        \"content\": \"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: " + code + "\""
                + "    }"
                + "]"
                + "}";


        try(OutputStream os = connection.getOutputStream()){
            byte[] input = jsonInpuString.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        ChatCompletionSyncResponse response = JSON.parseObject(content.toString(), ChatCompletionSyncResponse.class);
        System.out.println(response.getChoices().get(0).getMessage().getContent());

    }

}
