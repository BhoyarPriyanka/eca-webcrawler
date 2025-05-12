package com.eca.webcrawler.controller;

import com.eca.webcrawler.model.WebCrawlerResult;
import com.eca.webcrawler.service.WebCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@RestController
@RequestMapping("/pages")
@Slf4j
public class WebCrawlerController {
    @GetMapping
    public ResponseEntity<WebCrawlerResult> crawlUrl(@RequestParam String target, @RequestParam(defaultValue = "3") int maxDepth) {
        try {
            URI uri = new URI(target);
            if (uri.getScheme() == null || uri.getHost() == null || (!uri.getScheme().equals("http") && !uri.getScheme().equals("https"))) {
                return ResponseEntity.badRequest().build();
            }
            String domain = uri.getScheme() + "://" + uri.getHost();

            WebCrawlerService webCrawlerService = new WebCrawlerService(domain, maxDepth);
            Set<String> pages = webCrawlerService.crawl(target);

            WebCrawlerResult result = new WebCrawlerResult(domain, pages.stream().toList());
            return ResponseEntity.ok(result);

        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
