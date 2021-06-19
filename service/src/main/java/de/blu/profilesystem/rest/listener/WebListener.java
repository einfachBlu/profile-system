package de.blu.profilesystem.rest.listener;

import spark.Request;
import spark.Response;
import spark.Route;

public interface WebListener extends Route {

  default String badRequest(Request request, Response response) {
    return this.badRequest(request, response, "");
  }

  default String badRequest(Request request, Response response, String message) {
    response.status(400);
    return message;
  }

  default String notFoundRequest(Request request, Response response) {
    return this.notFoundRequest(request, response, "");
  }

  default String notFoundRequest(Request request, Response response, String message) {
    response.status(404);
    return message;
  }
}
