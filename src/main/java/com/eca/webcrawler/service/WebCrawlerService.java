package com.eca.webcrawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class WebCrawlerService {
    private final String domain;
    private final int maxDepth;
    private final Set<String> visited = new ConcurrentSkipListSet<>();
    private final Queue<UrlDepth> queue = new ConcurrentLinkedQueue<>();

    public WebCrawlerService(String domain, int maxDepth) {
        this.domain = domain;
        this.maxDepth = maxDepth;
    }

    public Set<String> crawl(String startUrl) {
        queue.add(new UrlDepth(startUrl, 1));
        visited.add(startUrl);
        while (!queue.isEmpty()) {
            UrlDepth current = queue.poll();
            if (current.depth <= maxDepth) {
                processUrl(current);
            }
        }
        return visited;
    }

    private void processUrl(UrlDepth urlDepth) {

        try {
            Connection.Response response = Jsoup.connect(urlDepth.url)
                    .timeout(5000)
                    .followRedirects(true)
                    .execute();

            String htmlBody = response.body();
            String baseUrl = response.url().toString();

            Document doc = Jsoup.parse(htmlBody, baseUrl);
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String absUrl = link.attr("abs:href");

                if (isInternalLink(absUrl) && visited.add(absUrl)) {
                    queue.add(new UrlDepth(absUrl, urlDepth.depth + 1));
                }
            }
        } catch (IOException ioException) {

            log.error(" Failed to crawl url::{} and error::{}", urlDepth.url, ioException.getMessage());

        }
    }

    private boolean isInternalLink(String url) {
        return url != null && url.startsWith(domain);
    }

    private static class UrlDepth {
        String url;
        int depth;

        UrlDepth(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }
    }

}