package services;

import interfaces.ShortenInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import models.ShortenRequest;
import models.URLError;
import models.URLShortenResponse;
import org.assertj.core.util.Lists;
import utils.Base62;

import java.util.HashMap;
import java.util.Map;

public class URLShortenService implements ShortenInterface {

    HashMap<Pair, Integer> longToShortURL;

    private Integer totalCount;

    @Override
    public URLShortenResponse convertToShortUrl(ShortenRequest request) {
        // Add the logic to conver long url to short URL
        if (longToShortURL == null) {
            longToShortURL = new HashMap<>();
            totalCount = 0;
        }

        Pair pair = new Pair(request.getLongUrl().getUrlString(), request.getClientId());


        if (longToShortURL.containsKey(pair)) {
            //Return already existing response
            return URLShortenResponse.builder()
                    .statusCode(200)
                    .shortUrl(longToShortURL.get(new Pair(request.getLongUrl().getUrlString(), request.getClientId())))
                    .build();
        }

        // Prepare Short URL
        /**
         * A simple technique for conversion of long to short url could be storing a url against an incremental id (bigint)
         * and encoding this incremental id to base 62 (Using characters: 0-9,a-z,A-Z).
         * This encoded id in base 62 could be simply used as a short url returned in output (with leading padding as relevant to make it 8 characters).
         * An 8 character shortened URL should be sufficient for any practical purposes.
         * Though simply returning raw id encoded as base 62 has security concerns and preferable way would be to encode this using a hash function, this that can be excluded from current scope given the limited time.
         */
        Integer shortURL = Base62.toBase62(request.getLongUrl().getUrlString() + request.getClientId());

        // Make Sure It is not present in the existing ones
        // TODO ADD A VALIDATION CHECK
        totalCount++;
        longToShortURL.put(pair, shortURL);

        return URLShortenResponse.builder()
                .shortUrl(shortURL)
                .statusCode(201)
                .build();
    }

    @Override
    public URLShortenResponse getShortenedURL(String shortURL) {
        // Iterate through short urls and return if it is found
        if (longToShortURL == null) {
            return getNotFoundResponse();
        }
        Integer shortURLInt;
        try {
            shortURLInt = Integer.parseInt(shortURL);

        } catch (Exception e) {
            return getNotFoundResponse();
        }
        // O(N)
        // Reverse O(1)
        // Iterate through all short URLs return back
        for (Map.Entry<Pair, Integer> entry : longToShortURL.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(shortURLInt)) {
                return URLShortenResponse.builder()
                        .statusCode(200)
                        .longUrl(entry.getKey().fst)
                        .build();
            }
        }

        return getNotFoundResponse();

    }

    private URLShortenResponse getNotFoundResponse() {
        URLError urlError = URLError.builder()
                .message("No URL Found")
                .build();
        // Return not found response
        return URLShortenResponse.builder()
                .statusCode(404)
                .error(Lists.newArrayList(urlError))
                .build();
    }
}
