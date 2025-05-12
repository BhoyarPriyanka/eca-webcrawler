## Webcrawler

### Assignment details
Build a web crawler that takes a URL as input and finds all unique pages for the given website (same domain).

### Approach
Expose GET REST API that take target URL and MaxDepth value to crawl to the Limit 

### Usage

1. Get this repository in local
   ```shell
   git clone git@github.com:BhoyarPriyanka/eca-webcrawler.git
   ```

### Prerequisite
1. Java 8 or higher version
2. Maven 3.6.3 or higher

### API Details
<details>
<summary>

#### 1. `GET  /pages?target=url&maxDepth=1` _Get unique pages for given website
</summary>

###### Request
```curl
curl http://localhost:8080/pages?target=https://example.com&maxDepth=1
```
###### Response
```curl
{
domain: "https://example.com",
pages: [
"https://example.com/",
"https://example.com/contact.html"
]
}
```
</details>


