package de.blu.profilesystem.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.repository.ProfileRepository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Singleton
public final class InMemoryStorage implements Storage {

  @Inject @Getter private ProfileRepository profileRepository;

  @Override
  public void init() {}

  @Override
  public void load() {}

  @Override
  public List<Profile> all() {
    return new ArrayList<>();
  }

  @Override
  public boolean contains(Profile targetProfile) {
    return this.all().stream().anyMatch(profile -> profile.getId() == targetProfile.getId());
  }

  @Override
  public void insert(Profile profile) {
    this.getProfileRepository().add(profile);
  }

  @Override
  public void delete(Profile profile) {
    Profile targetProfile = this.getProfileRepository().getById(profile.getId());
    if (targetProfile == null) {
      return;
    }

    this.getProfileRepository().remove(targetProfile);
  }

  @Override
  public void update(Profile profile) {
    this.update(profile, false);
  }

  @Override
  public void update(Profile profile, boolean onlyCache) {
    Profile targetProfile = this.getProfileRepository().getById(profile.getId());
    if (targetProfile == null) {
      return;
    }

    targetProfile.copyFrom(profile);
  }
}
