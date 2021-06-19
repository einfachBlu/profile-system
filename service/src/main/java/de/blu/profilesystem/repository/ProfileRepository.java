package de.blu.profilesystem.repository;

import com.google.inject.Singleton;
import de.blu.profilesystem.data.Profile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public final class ProfileRepository extends Repository<Profile> {

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
}
