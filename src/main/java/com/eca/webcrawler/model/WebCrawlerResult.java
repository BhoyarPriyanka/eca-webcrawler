package com.eca.webcrawler.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class WebCrawlerResult {
    String domain;
    List<String> pages;
}
