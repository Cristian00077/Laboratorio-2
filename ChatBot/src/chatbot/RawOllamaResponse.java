package chatbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawOllamaResponse {

    public final String model;
    public final String created_at;
    public final OllamaMessage message;
    public final boolean done;

    public RawOllamaResponse() {
        model = "";
        created_at = "";
        message = new OllamaMessage();
        done = false;
    }

    public RawOllamaResponse(String model, String created_at, OllamaMessage message , boolean done) {
        this.model = model;
        this.created_at = created_at;
        this.message = message;
        this.done = done;
    }
    public static RawOllamaResponse fromRawJson(String jsonStr) {
        String model;
        model = "";
        String content = "", createdAt = "", role = "";
        boolean isDone = false;
        Pattern modelPattern = Pattern.compile("\"model\":\\s*\"([^\"]+)\"");
        Matcher modelMatcher = modelPattern.matcher(jsonStr);
        if (modelMatcher.find()) {
            model = modelMatcher.group(1);
        }
        Pattern contentPattern = Pattern.compile("\"content\":\\s*\"([^\"]+)\"");
        Matcher contentMatcher = contentPattern.matcher(jsonStr);
        if (contentMatcher.find()) {
            content = contentMatcher.group(1);
        }
        Pattern createdAtPattern = Pattern.compile("\"created_at\":\\s*\"([^\"]+)\"");
        Matcher createdAtMatcher = createdAtPattern.matcher(jsonStr);
        if (createdAtMatcher.find()) {
            createdAt = createdAtMatcher.group(1);
        }
        Pattern rolePattern = Pattern.compile("\"role\":\\s*\"([^\"]+)\"");
        Matcher roleMatcher = rolePattern.matcher(jsonStr);
        if (roleMatcher.find()) {
            role = roleMatcher.group(1);
        }
        Pattern donePattern = Pattern.compile("\"done\":\\s*(true|false)");
        Matcher doneMatcher = donePattern.matcher(jsonStr);
        if (doneMatcher.find()) {
            isDone = Boolean.parseBoolean(doneMatcher.group(1));
        }
        return new RawOllamaResponse(model, createdAt, new OllamaMessage(role, content), isDone);
    }
}