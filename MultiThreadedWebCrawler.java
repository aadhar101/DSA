import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadedWebCrawler {
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor;

    public MultiThreadedWebCrawler(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    // Start crawling method, accepts list of initial URLs
    public void startCrawling(List<String> startUrls) {
        for (String url : startUrls) {
            urlQueue.add(url);
        }
        // Submit a task for each thread
        for (int i = 0; i < 10; i++) {
            executor.submit(new CrawlerTask());
        }
    }

    // Crawler task that fetches and processes URLs
    private class CrawlerTask implements Runnable {
        @Override
        public void run() {
            while (!urlQueue.isEmpty()) {
                String url = urlQueue.poll(); // Get a URL to crawl
                if (url != null && !visitedUrls.contains(url)) {
                    visitedUrls.add(url); // Mark the URL as visited
                    processPage(url); // Process the URL
                }
            }
        }

        private void processPage(String url) {
            try {
                System.out.println("Crawling: " + url);
                // Fetch the page content
                URL website = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                saveToFile(url, content.toString()); // Save content to file
                extractLinks(content.toString(), url); // Extract new links from the content
            } catch (IOException e) {
                System.err.println("Failed to fetch URL: " + url);
            }
        }

        private void saveToFile(String url, String content) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("crawled_data.txt", true))) {
                writer.write("URL: " + url + "\n");
                writer.write(content);
                writer.write("\n\n========================\n\n");
            } catch (IOException e) {
                System.err.println("Error saving content from: " + url);
            }
        }

        private void extractLinks(String pageContent, String baseUrl) {
            // Basic link extraction (a real crawler would be more robust)
            List<String> links = new ArrayList<>();
            int startIndex = 0;
            while ((startIndex = pageContent.indexOf("href=\"", startIndex)) != -1) {
                startIndex += 6;
                int endIndex = pageContent.indexOf("\"", startIndex);
                if (endIndex != -1) {
                    String link = pageContent.substring(startIndex, endIndex);
                    // If link is relative, convert it to an absolute URL
                    if (!link.startsWith("http")) {
                        link = baseUrl + link;
                    }
                    if (!visitedUrls.contains(link)) {
                        links.add(link);
                    }
                }
                startIndex = endIndex;
            }

            // Add new links to the queue
            for (String link : links) {
                urlQueue.add(link);
            }
        }
    }

    public static void main(String[] args) {
        MultiThreadedWebCrawler crawler = new MultiThreadedWebCrawler(5); // 5 threads
        List<String> startUrls = Arrays.asList(
            "https://www.facebook.com",
            "https://www.instagram.com",
            "https://www.youtube.com"
        );
        
        crawler.startCrawling(startUrls); // Start crawling
    }
}
