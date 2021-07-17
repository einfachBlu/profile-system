package de.blu.profilesystem.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DiscordWebhook {

  public static final DiscordWebhook PROFILE_LOGIN_WEBHOOK =
      new DiscordWebhook(
          "https://discord.com/api/webhooks/865902589174611988/TZZbCCY9zrjWkIYBpy7d1ZQI0K6zB87jWWMvns56xvKzd5YyL-Gxh35kDMCAg-IWNacD");
  public static final DiscordWebhook PROFILE_LOGOUT_WEBHOOK = new DiscordWebhook(
          "https://discord.com/api/webhooks/865904986163511307/tyAz_YgRcBGRSs5M4JECuIyOd_PS1kVyYG79dhkNDqvHDW4fSpb_Cxf5AcVAcmRuFafm");

  private String url;
  private String userName = null;
  private String avatarUrl = null;

  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  public DiscordWebhook(String url) {
    this.url = url;
  }

  public DiscordWebhook(String url, String userName, String avatarUrl) {
    this.url = url;
    this.userName = userName;
    this.avatarUrl = avatarUrl;
  }

  public void shutdown() {
    this.executorService.shutdown();
  }

  public void sendMessage(String content) {
    if (this.userName == null || this.avatarUrl == null) {
      return;
    }

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("content", content);
    jsonObject.addProperty("username", this.userName);
    jsonObject.addProperty("avatar_url", this.avatarUrl);
    this.request(this.url, jsonObject);
  }

  public void sendEmbed(String embedTitle, String embedDescription, Color color) {
    String hex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    int colorDecimal = Math.toIntExact(Long.decode(hex));

    JsonObject innerJsonObject = new JsonObject();
    innerJsonObject.addProperty("title", embedTitle);
    innerJsonObject.addProperty("type", "rich");
    innerJsonObject.addProperty("description", embedDescription);
    innerJsonObject.addProperty("color", colorDecimal);

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(innerJsonObject);

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("embeds", jsonArray);

    this.request(this.url, jsonObject);
  }

  private void request(String url, JsonObject jsonObject) {
    this.request(url, jsonObject.toString());
  }

  private void request(String url, String data) {
    this.executorService.submit(
        () -> {
          try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

            connection.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));

            int responseCode = connection.getResponseCode();
            if (responseCode == 204) {
              return;
            }

            System.out.println("Discord webhook returned response code: " + responseCode);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
