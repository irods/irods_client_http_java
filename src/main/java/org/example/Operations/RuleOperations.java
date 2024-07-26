package org.example.Operations;

import org.example.Wrapper;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RuleOperations {
    private final Wrapper client;
    private String baseUrl;

    public RuleOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/rules";
    }

    public Response list_rule_engines(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "list_rule_engines");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response execute(String token, String ruleText, Optional<String> repInstance) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute");
        formData.put("rule-text", ruleText);
        if (repInstance.isPresent()) {
            formData.put("rep-instance", repInstance);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_delay_rule(String token, int ruleId) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_delay_rule");
        formData.put("rule-id", String.valueOf(ruleId));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}
