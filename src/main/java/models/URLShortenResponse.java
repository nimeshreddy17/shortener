package models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class URLShortenResponse {

    private Integer statusCode;

    private Integer shortUrl;

    private String longUrl;

    private List<URLError> error;

}
