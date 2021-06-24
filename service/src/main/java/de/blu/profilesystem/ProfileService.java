package de.blu.profilesystem;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.exception.ServiceUnreachableException;
import de.blu.profilesystem.rest.WebInitializer;
import de.blu.profilesystem.storage.LocalStorage;
import de.blu.profilesystem.storage.Storage;
import de.blu.profilesystem.util.ProfileWebRequester;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.UUID;

@Singleton
public class ProfileService {

  public static void main(String[] args) {
    Injector injector =
        Guice.createInjector(
            new AbstractModule() {
              @Override
              protected void configure() {
                bind(Gson.class).toInstance(new Gson().newBuilder().setPrettyPrinting().create());

                // Current Storage
                bind(Storage.class).to(LocalStorage.class);
              }
            });
    ProfileService profileService = injector.getInstance(ProfileService.class);
    injector.injectMembers(profileService);
    profileService.startup();
  }

  @Inject private Injector injector;

  public void startup() {
    for (int i = 0; i < 5; i++) {
      System.out.println(UUID.randomUUID());
    }

    System.out.println("");

    System.out.println("Profile Service started");
    System.out.println("Possible REST API Calls:");

    System.out.println("");

    System.out.println("GET:");
    // System.out.println("\t- http://localhost:8081/status");
    // System.out.println("\t- http://localhost:8081/help");
    System.out.println("\t- http://localhost:8081/profiles");
    System.out.println("\t- http://localhost:8081/profile?id=%profileId%");
    System.out.println("\t- http://localhost:8081/profiles?player=%uuid%");
    System.out.println("\t- http://localhost:8081/profiles?name=%name%");

    System.out.println("PUT:");
    System.out.println("\t- http://localhost:8081/profile");

    System.out.println("PATCH:");
    System.out.println("\t- http://localhost:8081/profile");
    System.out.println("\t- http://localhost:8081/profileloginupdate");

    System.out.println("DELETE:");
    System.out.println("\t- http://localhost:8081/profile?id=%profileId%");

    System.out.println("");

    // Storage
    Storage storage = this.injector.getInstance(Storage.class);
    storage.init();
    storage.load();

    this.injector.getInstance(WebInitializer.class).init();

    /*
    // Testing
    ProfileWebRequester profileWebRequester = new ProfileWebRequester();

    String url = "http://localhost:8081";
    System.out.println("Testing:");
    try {
      for (Profile profile : profileWebRequester.getProfiles(url)) {
        System.out.println("deleted Profile: " + profileWebRequester.deleteProfile(url, profile.getId()));
      }

      Profile profile = profileWebRequester.createProfile(url, UUID.randomUUID(), "Mango");
      if (profile == null) {
        return;
      }

      profile.setName("Banana");
      profile = profileWebRequester.updateProfile(url, profile);
      System.out.println("Updated Profile from Name Mongo to Banana: " + profile);

      System.out.println(
          "All Profiles: " + Arrays.toString(profileWebRequester.getProfiles(url).toArray()));
      System.out.println(
          "Profiles of first player: " + Arrays.toString(profileWebRequester.getProfilesByPlayer(url, UUID.fromString("7926cb9f-093c-4a81-895e-cf70626b18ee")).toArray()));
      System.out.println(
          "Profiles of second player: " + Arrays.toString(profileWebRequester.getProfilesByPlayer(url, UUID.fromString("e1ed140b-5847-4996-b9a5-cd52bc506602")).toArray()));
      System.out.println(
          "Profile by id: " + profileWebRequester.getProfileById(url, UUID.fromString("24298913-a1d8-4f9a-adf5-03398d20d8e4")));
      System.out.println(
          "Profile by Name: " + profileWebRequester.getProfileByName(url, "Banana"));
      System.out.println(
          "Inserted Profile with already used name: " + profileWebRequester.createProfile(url, UUID.randomUUID(), "Banana"));
      System.out.println(
          "Inserted Profile: " + profileWebRequester.createProfile(url, UUID.randomUUID(), "Mango"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    */
  }
}
