package org.irods.operations;

import org.irods.IrodsHttpClient;
import org.irods.properties.Query.QueryExecuteGenQueryParams;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Class provides methods to interact with the rules endpoint.
 */
public class RuleOperations {
    private final IrodsHttpClient client;
    private String baseUrl;

    /**
     * Constructs a {@code RuleOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public RuleOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/rules";
    }

    /**
     * Lists the available rule engine plugin instances of the connected iRODS server.
     *
     * @param token The authentication token for the iRODS user.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response listRuleEngines(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "list_rule_engines");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Executes rule code.
     *
     * <p>
     *     If {@code repInstance} is not passed, the rule text will be tried on ALL rule engine plugins. Any errors that
     *     occur will be ignored. Setting rep-instance is highly recommended.
     * </p>
     *
     * @param token The authentication token for the iRODS user.
     * @param ruleText The rule code to execute.
     * @param repInstance An optional parameter to set the rule engine plugin to run the rule-text against. This is
     *                    wrapped in an {@link Optional}. If present, it will be included in the request; if absent,
     *                    the {@code context} parameter will be omitted.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response execute(String token, String ruleText, Optional<String> repInstance) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute");
        formData.put("rule-text", ruleText);
        repInstance.ifPresent(val -> formData.put("rep-instance", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a delay rule from the catalog.
     *
     * @param token The authentication token for the iRODS user.
     * @param ruleId The ID of delay rule to remove.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeDelayRule(String token, int ruleId) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_delay_rule");
        formData.put("rule-id", String.valueOf(ruleId));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}