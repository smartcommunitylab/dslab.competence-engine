package it.smartcommunitylab.scoengine.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletionException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

import it.smartcommunitylab.scoengine.model.esco.EscoResponse;

@Component
public class HttpUtils {
	HttpClient client = HttpClient.newHttpClient();
	UncheckedObjectMapper uncheckedObjectMapper = new UncheckedObjectMapper();
	
	/**
	 * Http GET.
	 * 
	 * @param url
	 * @param contentType
	 * @param accept
	 * @param auth
	 * @param secure
	 * @return
	 * @throws Exception
	 */
	public EscoResponse sendGET(String url, String text, int limit, int offset)
			throws Exception {
		
		uncheckedObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		URI uri = new URIBuilder(url)
				.addParameter("text", text)
				.addParameter("language", "it")
				.addParameter("limit", String.valueOf(limit))
				.addParameter("offset", String.valueOf(offset))
				.addParameter("type", "occupation")
				.build();
		var request = HttpRequest.newBuilder(uri).header("Content-Type", "application/json").GET().build();

		EscoResponse response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				  .thenApply(HttpResponse::body)
	              .thenApply(uncheckedObjectMapper::readValue)
	              .get();

		return response;
	}
	
	class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
		/**
		 * Parses the given JSON string into a Map.
		 */
		EscoResponse readValue(String content) {
			try {
				return this.readValue(content, new TypeReference<EscoResponse>() {
				});
			} catch (IOException ioe) {
				throw new CompletionException(ioe);
			}
		}
	}
	
}
