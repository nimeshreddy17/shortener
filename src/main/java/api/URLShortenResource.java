package api;

import models.ShortenRequest;
import models.URLShortenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public interface URLShortenResource {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/shorten-url", produces = "application/json")
    ResponseEntity<URLShortenResponse> shortenURL(@RequestHeader("idempotent-key") UUID idempotentKey, ShortenRequest request);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/get-longurl", produces = "application/json")
    ResponseEntity<URLShortenResponse> getLongUrlFromShortUrl(@RequestHeader("idempotent-key") UUID idempotentKey,@RequestParam("short_url") String shortUrl);

}
