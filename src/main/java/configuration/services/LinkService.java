package configuration.services;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.uri.UriBuilder;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
     * @param offset the offset parameter (this might be a default that overrides the user-supplied parameter)
     * @param limit  the limit parameter (this might be a default that overrides the user-supplied parameter)
     * @return the string URL of the previous link based on the query parameters given in the request.
     * If no context is found for the request, returns null.
     * <p>
     * Any and all parameters in the request will be echoed in the response, regardless of their utility in the request.
     */
    public String getPreviousLink(Integer offset, Integer limit) {

        Optional<HttpRequest<Object>> requestOptional = ServerRequestContext.currentRequest();

        if (requestOptional.isPresent()) {

            HttpRequest request = requestOptional.get();

            Map<String, String> paramMap = request.getParameters().asMap(String.class, String.class);

            paramMap.put("offset", String.valueOf(Math.max(offset - limit, 0)));

            // convert to TreeMap for guaranteed consistent parameter ordering in the link output
            TreeMap<String, String> sortedMap = new TreeMap<>();
            sortedMap.putAll(paramMap);

            return addNonNullQueryParams(paramMap, getBasicUri(request))
                    .build().toString();

        } else {
            return null;
        }
    }

    /**
     * @param offset the offset parameter (this might be a default that overrides the user-supplied parameter)
     * @param limit  the limit parameter (this might be a default that overrides the user-supplied parameter)
     * @return the string URL of the next link based on the query parameters given in the request.
     * If no context is found for the request, returns null.
     * <p>
     * Any and all parameters in the request will be echoed in the response, regardless of their utility in the request.
     */
    public String getNextLink(Integer offset, Integer limit) {

        Optional<HttpRequest<Object>> requestOptional = ServerRequestContext.currentRequest();

        if (requestOptional.isPresent()) {

            HttpRequest request = requestOptional.get();

            Map<String, String> paramMap = request.getParameters().asMap(String.class, String.class);

            paramMap.put("offset", String.valueOf(offset + limit));

            // convert to TreeMap for guaranteed consistent parameter ordering in the link output
            TreeMap<String, String> sortedMap = new TreeMap<>();
            sortedMap.putAll(paramMap);

            return addNonNullQueryParams(paramMap, getBasicUri(request))
                    .build().toString();

        } else {
            return null;
        }
    }

    public URI getSelfUri(HttpRequest request, String resourceId) {

        String path = request.getPath();

        path = path.replaceFirst("(.*[/])(.*)", "$1" + resourceId);
        return getBasicUri(request).replacePath(path).build();

    }

}
