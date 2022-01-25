package it.smartcommunitylab.scoengine.common;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class HttpUtils {
	HttpClient client = HttpClient.newHttpClient();

	public CompletableFuture<String> get(String uri) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
		return client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body);
	}

	public CompletableFuture<String> postJSON(URI uri, Map<String, String> map) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(requestBody)).build();
		return HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body);
	}

}
