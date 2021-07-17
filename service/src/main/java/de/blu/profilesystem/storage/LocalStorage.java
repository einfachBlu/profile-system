package de.blu.profilesystem.storage;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.repository.ProfileRepository;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public final class LocalStorage implements Storage {

  @Inject @Getter private ProfileRepository profileRepository;
  @Inject @Getter private Gson gson;

  private File configFile;

  @Override
  public void init() {
    this.configFile = new File(this.getRootDirectory(), "local_storage.json");

    if (!this.configFile.exists()) {
      try {
        this.configFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void load() {
    // Load from Config Storage File
    List<Profile> profiles = this.all();
    System.out.println("Loaded " + profiles.size() + " Profiles");

    // Cache them in the Repository
    this.getProfileRepository().clear();
    this.getProfileRepository().addAll(profiles);
  }

  @Override
  public List<Profile> all() {
    try (FileReader fileReader = new FileReader(this.configFile)) {
      Profile[] profiles = this.gson.fromJson(fileReader, Profile[].class);
      if (profiles == null) {
        return new ArrayList<>();
      }

      return Arrays.asList(profiles);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }

  @Override
  public boolean contains(Profile targetProfile) {
    return this.all().stream().anyMatch(profile -> profile.getId() == targetProfile.getId());
  }

  @Override
  public void insert(Profile profile) {
    this.getProfileRepository().add(profile);

    this.save();
  }

  @Override
  public void delete(Profile profile) {
    Profile targetprofile = this.getProfileRepository().getById(profile.getId());
    if (targetprofile == null) {
      return;
    }

    this.getProfileRepository().remove(targetprofile);
    this.save();
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

    if (!onlyCache) {
      this.save();
    }
  }

  private void save() {
    try {
      try (FileWriter fileWriter = new FileWriter(this.configFile)) {
        List<Profile> profiles = new ArrayList<>(this.getProfileRepository().all());

        this.gson.toJson(profiles, fileWriter);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File getRootDirectory() {
    File directory = null;

    try {
      directory =
          new File(
                  LocalStorage.class
                      .getProtectionDomain()
                      .getCodeSource()
                      .getLocation()
                      .toURI()
                      .getPath())
              .getParentFile();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    if (directory != null && !directory.isDirectory()) {
      if (!directory.mkdir()) {
        throw new NullPointerException("Couldn't create root directory!");
      }
    }

    return directory;
  }
}
