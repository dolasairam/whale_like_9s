import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.*;

public class TextAnalyzer {

    // List of stop words to exclude
    private static final Set<String> EXCLUDED_WORDS = new HashSet<>(Arrays.asList(
            // Articles
            "the", "a", "an",
            // Conjunctions
            "and", "or", "but", "nor", "so", "yet", "for",
            // Prepositions
            "in", "on", "at", "by", "with", "about", "against", "between", "into", "through", "during",
            "before", "after", "above", "below", "to", "from", "up", "down", "off", "over", "under",
            "of", "that", "as",
            // Pronouns
            "he", "she", "it", "they", "we", "you", "i", "me", "him", "her", "them", "us", "my", "your",
            "his", "their", "its", "ours", "mine", "yours", "hers", "all",
            // Modal/Helping verbs
            "is", "was", "am", "are", "were", "be", "been", "being", "this", "there",
            // Other common helpers
            "do", "does", "did", "have", "has", "had"
    ));

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String url = "https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt";
        Map<String, Integer> wordCount = new HashMap<>();
        int totalWordCount = 0;

        try {
            URL fileUrl = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileUrl.openStream()));

            String line;
            Pattern wordPattern = Pattern.compile("\\b[a-zA-Z]+(?:'s)?\\b"); // matches words with optional 's

            while ((line = reader.readLine()) != null) {
                Matcher matcher = wordPattern.matcher(line);

                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();

                    if (word.endsWith("'s")) {
                        word = word.substring(0, word.length() - 2);
                    }

                    if (!EXCLUDED_WORDS.contains(word)) {
                        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                        totalWordCount++;
                    }
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Sort by frequency
        List<Map.Entry<String, Integer>> sortedByFreq = new ArrayList<>(wordCount.entrySet());
        sortedByFreq.sort((e1, e2) -> e2.getValue() - e1.getValue());

        // Extract top 5
        List<Map.Entry<String, Integer>> top5 = sortedByFreq.subList(0, Math.min(5, sortedByFreq.size()));

        // Extract unique words in alphabetical order
        List<String> uniqueWords = new ArrayList<>(wordCount.keySet());
        Collections.sort(uniqueWords);

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;

        // Output
        System.out.println("Total Words (excluding excluded words): " + totalWordCount);
        System.out.println("\nTop 5 Most Frequent Words:");
        for (Map.Entry<String, Integer> entry : top5) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\nTop 50 Unique Words (Alphabetical):");
        uniqueWords.stream().limit(50).forEach(System.out::println);

        System.out.println("\nTime taken: " + duration + " seconds");
    }
}
