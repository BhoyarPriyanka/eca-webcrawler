package com.eca.webcrawler.controller;

import com.eca.webcrawler.model.WebCrawlerResult;
import com.eca.webcrawler.service.WebCrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pages")
public class WebCrawlerController {

    @GetMapping
    public ResponseEntity<WebCrawlerResult> crawl(@RequestParam String target) throws URISyntaxException {
          if(!target.startsWith("http"))
          {
              target="https://"+target;
          }
          URI uri=new URI(target);
          String domain=uri.getScheme()+"://"+uri.getHost();

        WebCrawlerService webCrawlerService=new WebCrawlerService(domain);
        Set<String> pages=webCrawlerService.crawl(target);

        WebCrawlerResult result=new WebCrawlerResult(domain,pages.stream().toList());
        return  ResponseEntity.ok(result);
    }
}
