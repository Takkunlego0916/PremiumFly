package io.github.takkunlego0916.premiumFly;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class UpdateChecker {

    private final PremiumFly plugin;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private volatile boolean resultReady = false;
    private volatile boolean upToDate = true;
    private volatile String latestVersion = "";
    private volatile String pageUrl = "";

    public UpdateChecker(PremiumFly plugin) {
        this.plugin = plugin;
    }

    public void check() {
        String modrinthId = plugin.getConfig().getString("update-checker.modrinth-id", "premiumfly");
        if (modrinthId == null || modrinthId.isBlank()) {
            return;
        }

        String currentVersion = plugin.getDescription().getVersion();
        String userAgent = "Takkunlego0916/PremiumFly/" + currentVersion + " (github.com/Takkunlego0916/PremiumFly)";
        URI uri = URI.create("https://api.modrinth.com/v2/project/" + modrinthId + "/version");

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("User-Agent", userAgent)
                .timeout(Duration.ofSeconds(8))
                .GET()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> handleResponse(response, modrinthId, currentVersion))
                .exceptionally(throwable -> {
                    plugin.getLogger().fine("PremiumFly update check failed: " + throwable.getMessage());
                    return null;
                });
    }

    private void handleResponse(HttpResponse<String> response, String modrinthId, String currentVersion) {
        if (response.statusCode() != 200) {
            plugin.getLogger().fine("PremiumFly update check returned HTTP " + response.statusCode());
            return;
        }

        JsonElement parsed;
        try {
            parsed = JsonParser.parseString(response.body());
        } catch (RuntimeException exception) {
            return;
        }

        if (!parsed.isJsonArray()) {
            return;
        }

        JsonArray versions = parsed.getAsJsonArray();
        String newestNumber = null;
        String newestDate = null;

        for (JsonElement element : versions) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject version = element.getAsJsonObject();
            if (!version.has("version_number") || !version.has("date_published")) {
                continue;
            }

            String number = version.get("version_number").getAsString();
            String date = version.get("date_published").getAsString();

            if (newestDate == null || date.compareTo(newestDate) > 0) {
                newestDate = date;
                newestNumber = number;
            }
        }

        if (newestNumber == null) {
            return;
        }

        latestVersion = newestNumber;
        pageUrl = "https://modrinth.com/plugin/" + modrinthId + "/version/" + newestNumber;
        upToDate = compareVersions(currentVersion, newestNumber) >= 0;
        resultReady = true;

        if (!upToDate) {
            String finalLatest = newestNumber;
            Bukkit.getScheduler().runTask(plugin, () ->
                    plugin.getLogger().info("A new PremiumFly version is available: " + finalLatest + " (current: " + currentVersion + ")"));
        }
    }

    private int compareVersions(String a, String b) {
        String[] partsA = a.split("[^0-9]+");
        String[] partsB = b.split("[^0-9]+");
        int length = Math.max(partsA.length, partsB.length);

        for (int i = 0; i < length; i++) {
            int valueA = parsePart(partsA, i);
            int valueB = parsePart(partsB, i);
            if (valueA != valueB) {
                return Integer.compare(valueA, valueB);
            }
        }
        return 0;
    }

    private int parsePart(String[] parts, int index) {
        if (index >= parts.length || parts[index].isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(parts[index]);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    public boolean hasResult() {
        return resultReady;
    }

    public boolean isUpToDate() {
        return upToDate;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}
