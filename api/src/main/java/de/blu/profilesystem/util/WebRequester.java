package de.blu.profilesystem.util;

import de.blu.profilesystem.exception.ServiceUnreachableException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class WebRequester {

  public String getRequest(String urlString) throws ServiceUnreachableException {
    HttpURLConnection connection = this.getConnection(urlString);
    return this.readContent(connection);
  }

  public String putRequest(String urlString, String body) throws ServiceUnreachableException {
    HttpURLConnection connection = this.getConnection(urlString);

    try {
      connection.setRequestMethod("PUT");
    } catch (ProtocolException e) {
      e.printStackTrace();
    }

    this.writeContent(connection, body);
    return this.readContent(connection);
  }

  public String patchRequest(String urlString, String body) throws ServiceUnreachableException {
    HttpURLConnection connection = this.getConnection(urlString);

    try {
      connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
      connection.setRequestMethod("POST");
    } catch (ProtocolException e) {
      e.printStackTrace();
    }

    this.writeContent(connection, body);
    return this.readContent(connection);
  }

  public String deleteRequest(String urlString) throws ServiceUnreachableException {
    return this.deleteRequest(urlString, "");
  }

  public String deleteRequest(String urlString, String body) throws ServiceUnreachableException {
    HttpURLConnection connection = this.getConnection(urlString);

    try {
      connection.setRequestMethod("DELETE");
    } catch (ProtocolException e) {
      e.printStackTrace();
    }

    this.writeContent(connection, body);
    return this.readContent(connection);
  }

  private HttpURLConnection getConnection(String urlString) throws ServiceUnreachableException {
    return this.getConnection(urlString, true, false);
  }

  private HttpURLConnection getConnection(
      String urlString, boolean doOutput, boolean allowUserInteraction)
      throws ServiceUnreachableException {
    try {
      int timeout = (int) TimeUnit.SECONDS.toMillis(2);

      URL url = new URL(urlString);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(timeout);
      connection.setReadTimeout(timeout);
      connection.setDoOutput(doOutput);
      connection.setAllowUserInteraction(allowUserInteraction);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");

      return connection;
    } catch (Exception e) {
      throw new ServiceUnreachableException();
    }
  }

  private void writeContent(URLConnection connection, String content)
      throws ServiceUnreachableException {
    try (OutputStream outputStream = connection.getOutputStream()) {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
      outputStreamWriter.write(content);
      outputStreamWriter.flush();
    } catch (Exception e) {
      throw new ServiceUnreachableException();
    }
  }

  private String readContent(URLConnection connection) throws ServiceUnreachableException {
    StringBuilder content = new StringBuilder();

    try (InputStream inputStream = connection.getInputStream()) {
      Scanner scanner = new Scanner(inputStream);
      while (scanner.hasNext()) {
        if (content.length() > 0) {
          content.append("\n");
        }

        content.append(scanner.nextLine());
      }
    } catch (FileNotFoundException e) {
    } catch (Exception e) {
      throw new ServiceUnreachableException();
    }

    return content.toString();
  }
}
