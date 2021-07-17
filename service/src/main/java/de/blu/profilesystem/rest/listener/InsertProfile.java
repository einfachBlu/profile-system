package de.blu.profilesystem.rest.listener;

import com.google.gson.Gson;
import com.google.inject.Inject;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.repository.ProfileRepository;
import de.blu.profilesystem.storage.Storage;
import spark.Request;
import spark.Response;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class InsertProfile implements WebListener {

  @Inject private ProfileRepository profileRepository;
  @Inject private Storage storage;
  @Inject private Gson gson;

  @Override
  public Object handle(Request request, Response response) {
    if (request.body().isEmpty()) {
      return this.badRequest(request, response, "no body");
    }

    Profile profile;
    try {
      profile = this.gson.fromJson(request.body(), Profile.class);
    } catch (IllegalArgumentException e) {
      // invalid uuid as player
      return this.badRequest(request, response, "invalid player id");
    }

    profile.setId(UUID.randomUUID());
    profile.setCreationTime(System.currentTimeMillis());

    this.storage.insert(profile);

    return this.gson.toJson(profile);
  }
}
