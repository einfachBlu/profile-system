package de.blu.profilesystem.rest.listener;

import com.google.gson.Gson;
import com.google.inject.Inject;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.listener.ProfileLoginListener;
import de.blu.profilesystem.listener.ProfileLogoutListener;
import de.blu.profilesystem.repository.ProfileRepository;
import de.blu.profilesystem.storage.Storage;
import spark.Request;
import spark.Response;

import javax.inject.Singleton;

@Singleton
public final class UpdateProfile implements WebListener {

  @Inject private ProfileRepository profileRepository;
  @Inject private Storage storage;
  @Inject private Gson gson;

  @Inject private ProfileLoginListener profileLoginListener;
  @Inject private ProfileLogoutListener profileLogoutListener;

  @Override
  public Object handle(Request request, Response response) {
    if (request.body().isEmpty()) {
      return this.badRequest(request, response, "no body");
    }

    Profile profile = this.gson.fromJson(request.body(), Profile.class);

    boolean onlyCache = false;
    if (request.queryParams().contains("onlycache")) {
      try {
        onlyCache = Boolean.parseBoolean(request.queryParams("onlycache"));
      } catch (Exception e) {
      }
    }

    Profile oldProfile = this.profileRepository.getById(profile.getId());

    if (oldProfile.getLoggedInPlayerId() == null && profile.getLoggedInPlayerId() != null) {
      this.profileLoginListener.onLogin(profile, profile.getLoggedInPlayerId());
    }

    if (oldProfile.getLoggedInPlayerId() != null && profile.getLoggedInPlayerId() == null) {
      this.profileLogoutListener.onLogout(profile, oldProfile.getLoggedInPlayerId());
    }

    this.storage.update(profile, onlyCache);
    return this.gson.toJson(profile);
  }
}
