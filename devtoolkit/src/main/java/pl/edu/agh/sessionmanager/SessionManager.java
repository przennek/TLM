package pl.edu.agh.sessionmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import pl.edu.agh.messaging.Receiver;
import pl.edu.agh.messaging.Sender;
import pl.edu.agh.models.TokenExchange;
import pl.edu.agh.models.User;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kamil on 22.11.2016.
 */
public class SessionManager {
    public static Map<String, User> sessionIds  = new HashMap<>();

    public static void addId(String json) {
        JSONObject jsonObj = new JSONObject(json);
        SessionManager.sessionIds.put(jsonObj.getString("sessionId"), new User().login(jsonObj.getString("username")).role(jsonObj.getString("role")));
    }

    public static void flush() {
        sessionIds.clear();
    }

    public static void deleteId(String json) {
        JSONObject jsonObj = new JSONObject(json);
        sessionIds.remove(jsonObj.getString("sessionId"));
    }

    public static void initTokens(Map<String, User> sessionIdsToSet) {
        sessionIds = sessionIdsToSet;
    }

    public static void getAuthTokens(Optional<DiscoveryClient> discoveryClient, Sender sender, Receiver receiver, String serviceName) throws IOException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> services = new ArrayList<>();
        if(!discoveryClient.isPresent()) {
            services.add("auth");
        } else  {
            services = discoveryClient.get().getServices();
        }
        Optional<String> service = services.stream().filter(x  -> !x.equals("gateway") && !x.equals(serviceName)).findFirst();
        if(service.isPresent()) {
            TokenExchange tokenExchange = new TokenExchange()
                    .sourceServiceName(serviceName)
                    .targetServiceName(service.get())
                    .action("get")
                    .tokens(new HashMap<>());
            sender.sendOverTopic("auth-exchange", "auth.token.broadcast.tokenExchange", mapper.writeValueAsString(tokenExchange));
        }
        receiver.register("auth-exchange", msg -> {
            TokenExchange tokenExchangeMsg = null;
            try {
                tokenExchangeMsg = mapper.readValue(msg, TokenExchange.class);
                if (tokenExchangeMsg.action().equals("set") && tokenExchangeMsg.targetServiceName().equals(serviceName)) {
                    SessionManager.initTokens(tokenExchangeMsg.tokens());
                } else if (tokenExchangeMsg.action().equals("get") && tokenExchangeMsg.targetServiceName().equals(serviceName)) {
                    TokenExchange tokenExchangeToSend = new TokenExchange()
                            .sourceServiceName(serviceName)
                            .targetServiceName(tokenExchangeMsg.sourceServiceName())
                            .action("set")
                            .tokens(sessionIds);
                    sender.sendOverTopic("auth-exchange", "auth.token.broadcast.tokenExchange", mapper.writeValueAsString(tokenExchangeToSend));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "auth.token.broadcast.tokenExchange");
    }
}
