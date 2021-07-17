package de.blu.profilesystem.rest.listener;

import com.google.gson.Gson;
import com.google.inject.Inject;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.repository.ProfileRepository;
import spark.Request;
import spark.Response;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public final class ShowProfiles implements WebListener {

  @Inject private ProfileRepository profileRepository;
  @Inject private Gson gson;

  @Override
  public Object handle(Request request, Response response) {
    if (request.queryParams().contains("id")) {
      // Profile by the specified id
      return this.handleProfileIdFilter(request, response);
    }

    if (request.queryParams().contains("player")) {
      // Profiles by the specified player
      return this.handleProfilePlayerFilter(request, response);
    }

    if (request.queryParams().contains("loggedInPlayer")) {
      // Profile by the specified player logged in
      return this.handleProfileLoginPlayerFilter(request, response);
    }

    // All Profiles
    return this.gson.toJson(this.profileRepository.all());
  }

  public Object handleProfileIdFilter(Request request, Response response) {
    try {
      UUID id = UUID.fromString(request.queryParams("id"));

      Profile profile = this.profileRepository.getById(id);
      if (profile != null) {
        return this.gson.toJson(profile);
      }
    } catch (IllegalArgumentException e) {
      // no uuid
      return this.badRequest(request, response, "invalid id");
    }

    return this.notFoundRequest(request, response, "unknown id");
  }

  public Object handleProfileLoginPlayerFilter(Request request, Response response) {
    try {
      UUID playerId = UUID.fromString(request.queryParams("loggedInPlayer"));

      Profile profile = this.profileRepository.getLoggedInProfileByPlayer(playerId);
      if (profile != null) {
        return this.gson.toJson(profile);
      }
    } catch (IllegalArgumentException e) {
      // no uuid
      return this.badRequest(request, response, "invalid id");
    }

    return this.notFoundRequest(request, response, "unknown id");
  }

  public Object handleProfilePlayerFilter(Request request, Response response) {
    UUID playerId;
    try {
      playerId = UUID.fromString(request.queryParams("player"));
    } catch (IllegalArgumentException e) {
      // no uuid
      return this.badRequest(request, response, "invalid player");
    }

    List<Profile> profiles = this.profileRepository.getByPlayer(playerId);
    if (profiles != null) {
      return this.gson.toJson(profiles);
    }

    return this.notFoundRequest(request, response, "unknown player");
  }
}
