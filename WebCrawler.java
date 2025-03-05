import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler {
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor;
    
    public WebCrawler(int numThreads, int maxDepth) {
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    // Modified startCrawling method to accept a list of URLs
    public void startCrawling(List<String> startUrls) {
        for (String startUrl : startUrls) {
            urlQueue.add(startUrl);
        }
        for (int i = 0; i < 10; i++) {
            executor.execute(new CrawlerTask());
        }
        executor.shutdown();
    }

    private class CrawlerTask implements Runnable {
        @Override
        public void run() {
            while (!urlQueue.isEmpty()) {
                String url = urlQueue.poll();
                if (url != null && visitedUrls.add(url)) {
                    processPage(url);
                }
            }
        }

        private void processPage(String url) {
            try {
                System.out.println("Crawling: " + url);
                URL website = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(website.openStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                saveToFile(url, content.toString());
            } catch (IOException e) {
                System.err.println("Failed to fetch URL: " + url);
            }
        }
    }

    private void saveToFile(String url, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("crawled_data.txt", true))) {
            writer.write("URL: " + url + "\n");
            writer.write(content);
            writer.write("\n\n========================\n\n");
        } catch (IOException e) {
            System.err.println("Error saving content: " + url);
        }
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(5, 3);
        List<String> startUrls = Arrays.asList(
            "https://www.facebook.com",
            "https://www.Instagram.com",
            "https://www.youtube.com"
        );
        
        crawler.startCrawling(startUrls);
    }
}
