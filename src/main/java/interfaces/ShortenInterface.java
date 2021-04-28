package interfaces;

import models.ShortenRequest;
import models.URLShortenResponse;

public interface ShortenInterface {

    URLShortenResponse convertToShortUrl(ShortenRequest request);

    URLShortenResponse getShortenedURL(String shortURL);

}
