package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.RavelryApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.util.Scanner;

public final class RavelryExample {
    private static final String PROTECTED_RESOURCE_URL = "https://api.ravelry.com/current_user.json";

    private RavelryExample() {}

    public static void main(String... args) throws Exception {
        final OAuth10aService service = new ServiceBuilder()
                .apiKey("your client id")
                .apiSecret("your client secret")
                .debug()
                .build(RavelryApi.instance(RavelryApi.Scope.PROFILE_ONLY));
        final Scanner in = new Scanner(System.in);

        System.out.println("=== Meetup's OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        System.out.println("Got the Request Token! " + requestToken.getToken());
        System.out.println();

        System.out.println("Now go and authorize Ravelry here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        final String oauthVerifier = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
            + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getBody());

        System.out.println();
        System.out.println("Thats it! Go and build something awesome with Ravelry! :)");
    }
}
