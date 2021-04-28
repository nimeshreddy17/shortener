package impl;

import api.URLShortenResource;
import models.ShortenRequest;
import models.URLShortenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import services.URLShortenService;

import java.util.HashMap;
import java.util.UUID;

public class URLShortenResourceImpl implements URLShortenResource {

    @Autowired
    private URLShortenService urlShortenService;

    HashMap<UUID, ShortenRequest> requestHashMap;

    @Override
    public ResponseEntity<URLShortenResponse> shortenURL(UUID idempotentKey, ShortenRequest request) {

        if (requestHashMap == null) {
            requestHashMap = new HashMap<>();
        }

        /**
         * [{
         * "long_url":"",
         * "short_url": "",
         * "client_id":"",
         * "create_time": "",
         * "TTl":""
         * "expire_removal_time":
         * "last_accessed_time:
         * "client": "",
         * },
         *
         * --> Current_time - last_accessed_time >= expire_removal_time + 1hr
         * --> Cron -> at everypoint -->  At every time t1
         * -->
         *
         * -->
         * --> Cron --> queue --> Kafka --> Record
         *
         * -->
         *
         * CRON which can query --> Expiry in a particular time
         *
         * {
         * }
         *
         * ]
         */

        if (idempotentKey == null) {
            UUID.randomUUID();
        }

        if (requestHashMap.containsKey(idempotentKey)) {
            URLShortenResponse response = urlShortenService.convertToShortUrl(requestHashMap.get(idempotentKey));
            if (response.getStatusCode() == 200) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (response.getStatusCode() == 201) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else if (response.getStatusCode() == 404) {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        URLShortenResponse response = urlShortenService.convertToShortUrl(request);
        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.getStatusCode() == 201) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (response.getStatusCode() == 404) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        requestHashMap.put(idempotentKey, request);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<URLShortenResponse> getLongUrlFromShortUrl(UUID idempotentKey, String shortUrl) {
        URLShortenResponse response = urlShortenService.getShortenedURL(shortUrl);
        if (response.getStatusCode() == 200) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.getStatusCode() == 201) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else if (response.getStatusCode() == 404) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
