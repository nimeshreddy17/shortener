import models.ShortenEntityType;
import models.ShortenRequest;
import models.URL;
import models.URLShortenResponse;
import org.junit.Assert;
import org.junit.Test;
import services.URLShortenService;

public class URLShortenServiceTest {

    private URLShortenService shortenService = new URLShortenService();

    @Test
    public void testCreateService() {
        ShortenRequest request = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .longUrl(URL.builder().urlString("www.google.com").build())
                .build();

        URLShortenResponse response = shortenService.convertToShortUrl(request);

        Assert.assertEquals(response.getShortUrl(), Integer.valueOf(-2026034140));
        Assert.assertEquals(201, (int) response.getStatusCode());
    }

    @Test
    public void testCreateServiceAlreadyPresent() {
        ShortenRequest request = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .longUrl(URL.builder().urlString("www.google.com").build())
                .build();

        URLShortenResponse response = shortenService.convertToShortUrl(request);

        Assert.assertEquals(response.getShortUrl(), Integer.valueOf(-2026034140));
        Assert.assertEquals(201, (int) response.getStatusCode());

        request = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .longUrl(URL.builder().urlString("www.google.com").build())
                .build();
        response = shortenService.convertToShortUrl(request);
        Assert.assertEquals(response.getShortUrl(), Integer.valueOf(-2026034140));
        Assert.assertEquals(200, (int) response.getStatusCode());
    }


    @Test
    public void testGetLongURLFromShortURL() {

        ShortenRequest request = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .clientId("client-id3")
                .longUrl(URL.builder().urlString("www.google.com").build())
                .build();

        URLShortenResponse response = shortenService.convertToShortUrl(request);

        Assert.assertEquals(response.getShortUrl(), Integer.valueOf(-694632195));
        Assert.assertEquals(201, (int) response.getStatusCode());

        response = shortenService.getShortenedURL("-694632195");
        Assert.assertEquals(response.getLongUrl(), "www.google.com");
        Assert.assertEquals(200, (int) response.getStatusCode());
    }

    @Test
    public void testSameURLDifferentClients() {

        ShortenRequest request = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .longUrl(URL.builder().urlString("www.google.com").build())
                .clientId("clientid-1")
                .build();

        URLShortenResponse response = shortenService.convertToShortUrl(request);

        Assert.assertEquals(response.getShortUrl(), Integer.valueOf(-692506713));
        Assert.assertEquals(201, (int) response.getStatusCode());

        ShortenRequest request2 = ShortenRequest.builder()
                .entityType(ShortenEntityType.URL)
                .longUrl(URL.builder().urlString("www.google.com").build())
                .clientId("clientid-2")
                .build();

        URLShortenResponse response2 = shortenService.convertToShortUrl(request2);
        Assert.assertNotEquals(response2.getShortUrl(), Integer.valueOf(-692506713));
        Assert.assertEquals(201, (int) response2.getStatusCode());
        Assert.assertEquals(response2.getShortUrl(), Integer.valueOf(-692506712));
    }

}
