package com.eca.webcrawler.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.*;

@Service
@Slf4j
public class WebCrawlerServiceWorkerBased {


    public Set<String> crawl(String startUrl, String domain, int maxDepth) {
         final int NUM_WORKERS = 10;

        Set<String> visited = ConcurrentHashMap.newKeySet();
        BlockingQueue<UrlDepth> queue = new LinkedBlockingQueue<>();

        queue.add(new UrlDepth(startUrl, 1));
        visited.add(startUrl);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS);
        CountDownLatch latch = new CountDownLatch(NUM_WORKERS);

        Runnable worker = () -> {
            try {
                while (true) {
                    log.info("service crawl method running in thread::{}",Thread.currentThread().getName());

                    UrlDepth current = queue.poll(2, TimeUnit.MILLISECONDS);
                    if (current == null) break; // Timeout = end of queue

                    if (current.depth > maxDepth) continue;

                    try {
                        Connection.Response response = Jsoup.connect(current.url)
                                .timeout(5000)
                                .followRedirects(true)
                                .execute();

                        Document doc = Jsoup.parse(response.body(), response.url().toString());
                        Elements links = doc.select("a[href]");

                        for (Element link : links) {
                            String absUrl = link.attr("abs:href");
                            if (absUrl.startsWith(domain) && visited.add(absUrl)) {
                                queue.offer(new UrlDepth(absUrl, current.depth + 1));
                            }
                        }
                    } catch (IOException e) {
                        log.warn("Failed to crawl: {}", current.url);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        };

        // Submit all workers
        for (int i = 0; i < NUM_WORKERS; i++) {
            executor.submit(worker);
        }

        try {
            latch.await(); // Wait for all workers to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
        }

        return visited;
    }
}
