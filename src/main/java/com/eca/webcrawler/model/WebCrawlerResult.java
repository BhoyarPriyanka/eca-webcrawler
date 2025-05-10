package com.eca.webcrawler.model;


import java.util.List;

public class WebCrawlerResult {
    String domain;
    List<String> pages;

    public WebCrawlerResult(String domain, List<String> pages) {
        this.domain = domain;
        this.pages = pages;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }
}
