import java.util.*;
import java.util.regex.*;

class TrendingHashtags {
    public static void main(String[] args) {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(135, 13, "Enjoying a great start to the day. #HappyDay #MorningVibes", "2024-02-01"),
            new Tweet(136, 14, "Another #HappyDay with good vibes! #FeelGood", "2024-02-03"),
            new Tweet(137, 15, "Productivity hacks! #WorkLife #ProductiveDay", "2024-02-04"),
            new Tweet(138, 16, "Exploring new tech trends. #TechLife #Innovation", "2024-02-07"),
            new Tweet(139, 17, "Fun time with today’s moments. #HappyDay #Thankful", "2024-02-12"),
            new Tweet(140, 18, "Innovation drives us. #TechLife #FutureTech", "2024-02-18"),
            new Tweet(141, 19, "Connecting with nature’s serenity. #Nature #Peaceful", "2024-02-27")
        );

        // Process hashtags
        List<String> trendingHashtags = getTopTrendingHashtags(tweets, 3, "2024-02-01", "2024-02-29");

        // Display results
        System.out.println("Top Trending Hashtags:");
        for (String hashtag : trendingHashtags) {
            System.out.println(hashtag);
        }
    }

    public static List<String> getTopTrendingHashtags(List<Tweet> tweets, int topN, String startDate, String endDate) {
        Map<String, Integer> hashtagCount = new HashMap<>();
        Pattern hashtagPattern = Pattern.compile("#\\w+");

        for (Tweet tweet : tweets) {
            if (isWithinDateRange(tweet.tweetDate, startDate, endDate)) {
                Matcher matcher = hashtagPattern.matcher(tweet.content);
                while (matcher.find()) {
                    String hashtag = matcher.group();
                    hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        // Sort hashtags by frequency (descending) and alphabetically (if counts are equal)
        return hashtagCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().equals(a.getValue()) ? a.getKey().compareTo(b.getKey()) : b.getValue() - a.getValue())
            .limit(topN)
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .toList();
    }

    private static boolean isWithinDateRange(String tweetDate, String startDate, String endDate) {
        return tweetDate.compareTo(startDate) >= 0 && tweetDate.compareTo(endDate) <= 0;
    }
}

// Tweet class to store tweet details
class Tweet {
    int userId;
    int tweetId;
    String content;
    String tweetDate;

    public Tweet(int userId, int tweetId, String content, String tweetDate) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.content = content;
        this.tweetDate = tweetDate;
    }
}
