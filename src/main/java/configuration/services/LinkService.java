package configuration.services;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.uri.UriBuilder;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

@Singleton
public class LinkService {

    private UriBuilder getBasicUri(HttpRequest httpRequest) {

        String host = httpRequest.getServerAddress().getHostName();
        Integer port = httpRequest.getServerAddress().getPort();

        // TODO find scheme somewhere
        return UriBuilder.of("").scheme("http")
                .host(host).port(port)
                .path(httpRequest.getPath());

    }

    private UriBuilder addNonNullQueryParams(Map<String, String> paramMap, UriBuilder uriBuilder) {

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (entry.getValue() != null) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return uriBuilder;

    }

    /**
     * @return the string URL of the previous link based on the query parameters given in the request.
     * If no context is found for the request, returns null.
     *
     * Any and all parameters in the request will be echoed in the response, regardless of their utility in the request.
     */
    public String getPreviousLink() {

        // offset = Math.max(offset - limit, 0)
        Function<Map<String, String>, Map<String, String>> paramMapMutator = map -> {
            map.put("offset", String.valueOf(Math.max(Integer.parseInt(map.get("offset")) - Integer.parseInt(map.get("limit")), 0)));
            return map;
        };

        return assemblePageLink(paramMapMutator);
    }

    /**
     * @return the string URL of the next link based on the query parameters given in the request.
     * If no context is found for the request, returns null.
     *
     * Any and all parameters in the request will be echoed in the response, regardless of their utility in the request.
     */
    public String getNextLink() {

        // offset = offset + limit
        Function<Map<String, String>, Map<String, String>> paramMapMutator = map -> {
            map.put("offset", String.valueOf(Integer.parseInt(map.get("offset")) + Integer.parseInt(map.get("limit"))));
            return map;
        };

        return assemblePageLink(paramMapMutator);
    }

    private String assemblePageLink(Function<Map<String, String>, Map<String, String>> paramMapFunction) {

        Optional<HttpRequest<Object>> requestOptional = ServerRequestContext.currentRequest();

        if (requestOptional.isPresent()) {

            HttpRequest request = requestOptional.get();

            Map<String, String> paramMap = request.getParameters().asMap(String.class, String.class);

            paramMapFunction.apply(paramMap);

            // convert to TreeMap for guaranteed consistent parameter ordering in the link output
            TreeMap<String, String> sortedMap = new TreeMap<>();
            sortedMap.putAll(paramMap);

            return addNonNullQueryParams(paramMap, getBasicUri(request))
                    .build().toString();

        } else {
            return null;
        }
    }

}
