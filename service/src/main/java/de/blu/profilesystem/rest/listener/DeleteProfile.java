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
public final class DeleteProfile implements WebListener {

  @Inject private ProfileRepository profileRepository;
  @Inject private Storage storage;
  @Inject private Gson gson;

  @Override
  public Object handle(Request request, Response response) {
    if (request.queryParams().contains("id")) {
      try {
        UUID id = UUID.fromString(request.queryParams("id"));

        Profile profile = this.profileRepository.getById(id);
        if (profile != null) {
          this.storage.delete(profile);
          return this.gson.toJson(profile);
        }
      } catch (IllegalArgumentException e) {
        // no uuid
      }
    }

    return this.badRequest(request, response, "invalid id");
  }
}
