package de.blu.profilesystem.repository;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.listener.ProfileLogoutListener;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
public final class ProfileRepository extends Repository<Profile> {

  @Inject private ProfileLogoutListener profileLogoutListener;

  public ProfileRepository() {
    new Timer()
        .schedule(
            new TimerTask() {
              @Override
              public void run() {
                // Check logged in Profiles
                List<Profile> profiles = new ArrayList<>(all());
                for (Profile profile : profiles) {
                  if (profile.getLoggedInPlayerId() == null) {
                    continue;
                  }

                  // Timeout
                  UUID playerId = profile.getLoggedInPlayerId();
                  profile.setLoggedInPlayerId(null);
                  profileLogoutListener.onLogout(profile, playerId);
                }
              }
            },
            0,
            TimeUnit.SECONDS.toMillis(10));
  }

  public Profile getById(UUID id) {
    return this.all().stream()
        .filter(profile -> profile.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public Profile getByName(String name) {
    return this.all().stream()
        .filter(profile -> profile.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  public List<Profile> getByPlayer(UUID playerId) {
    return this.all().stream()
        .filter(profile -> profile.getPlayerId().equals(playerId))
        .collect(Collectors.toList());
  }

  public Profile getLoggedInProfileByPlayer(UUID playerId) {
    return this.all().stream()
        .filter(profile -> playerId.equals(profile.getLoggedInPlayerId()))
        .findFirst()
        .orElse(null);
  }
}
