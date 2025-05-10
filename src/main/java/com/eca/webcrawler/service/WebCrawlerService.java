package com.eca.webcrawler.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class WebCrawlerService {

    private final String domain;
    private final Set<String> visited = new HashSet<>();
    private final Queue<String> queue = new LinkedList<>();

    public WebCrawlerService(String domain) {
        this.domain = domain;
    }

    public Set<String> crawl(String startUrl) {
        queue.add(startUrl);
        visited.add(startUrl);
        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();
            processUrl(currentUrl);
        }
        return visited;
    }

    private void processUrl(String url) {

        try {
            Connection.Response response = Jsoup.connect(url)
                    .timeout(5000)
                    .followRedirects(true)
                    .execute();

            String htmlBody = response.body();
            String baseUrl = response.url().toString();

            Document doc = Jsoup.parse(htmlBody, baseUrl);
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String absUrl = link.attr("abs:href");

                if (isInternalLink(absUrl) && !visited.contains(absUrl)) {
                    queue.add(absUrl);
                    visited.add(absUrl);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to crawl: " + url + " because of " + e.getMessage());
        }
    }

    private boolean isInternalLink(String url) {
        return url != null && url.startsWith(domain);
    }


}
