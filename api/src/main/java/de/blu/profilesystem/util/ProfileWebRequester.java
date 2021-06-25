package de.blu.profilesystem.util;

import com.google.gson.Gson;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.exception.ServiceUnreachableException;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ProfileWebRequester extends WebRequester {

  private static Map<UUID, TimerTask> profileLoginUpdateTasks = new HashMap<>();

  public List<Profile> getProfiles(String url) throws ServiceUnreachableException {
    String content = this.getRequest(url + "/profiles");

    if (content.isEmpty()) {
      return new ArrayList<>();
    }

    Profile[] profiles = new Gson().fromJson(content, Profile[].class);
    return Arrays.asList(profiles);
  }

  public Profile getProfileById(String url, UUID profileId) throws ServiceUnreachableException {
    String content = this.getRequest(url + "/profiles?id=" + profileId.toString());

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public Profile getCurrentProfile(String url, UUID playerId) throws ServiceUnreachableException {
    String content = this.getRequest(url + "/profiles?loggedInPlayer=" + playerId.toString());

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public List<Profile> getProfilesByPlayer(String url, UUID playerId)
      throws ServiceUnreachableException {
    String content = this.getRequest(url + "/profiles?player=" + playerId.toString());

    if (content.isEmpty()) {
      return new ArrayList<>();
    }

    Profile[] profiles = new Gson().fromJson(content, Profile[].class);
    return Arrays.asList(profiles);
  }

  public Profile getProfileByName(String url, String profileName)
      throws ServiceUnreachableException {
    String content = this.getRequest(url + "/profiles?name=" + profileName);

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public Profile createProfile(String url, UUID playerId, String name)
      throws ServiceUnreachableException {
    Profile profile = new Profile();
    profile.setPlayerId(playerId);
    profile.setName(name);

    String content = this.putRequest(url + "/profile", profile.toString());

    if (content.isEmpty() || content.equalsIgnoreCase("name already in use")) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public Profile deleteProfile(String url, UUID profileId) throws ServiceUnreachableException {
    String content = this.deleteRequest(url + "/profile?id=" + profileId);

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public void startLoginUpdateTask(String url, UUID playerId) throws ServiceUnreachableException {
    final Timer timer = new Timer();
    final TimerTask timerTask =
        new TimerTask() {
          @Override
          public void run() {
            try {
              Profile profile = getCurrentProfile(url, playerId);
              updateProfileLogin(url, profile);
            } catch (ServiceUnreachableException e) {
              System.out.println(
                  "Couldn't update login time of Profile: Service unreachable!");
            }
          }
        };
    profileLoginUpdateTasks.put(playerId, timerTask);
    timer.schedule(timerTask, 0, TimeUnit.SECONDS.toMillis(3));
  }

  public void stopLoginUpdateTask(String url, UUID playerId) throws ServiceUnreachableException {
    if (!profileLoginUpdateTasks.containsKey(playerId)) {
      return;
    }

    profileLoginUpdateTasks.remove(playerId).cancel();
  }

  public void login(String url, UUID playerId, Profile profile) throws ServiceUnreachableException {
    profile.setLoggedInPlayerId(playerId);
    profile.setLoggedInLastUpdate(System.currentTimeMillis());
    this.updateProfile(url, profile, true);
  }

  public void logout(String url, UUID playerId, Profile profile)
      throws ServiceUnreachableException {
    profile.setLoggedInPlayerId(null);
    profile.setLoggedInLastUpdate(0);
    this.updateProfile(url, profile, true);
  }

  public Profile updateProfile(String url, Profile updatedProfile, boolean onlyCache)
      throws ServiceUnreachableException {
    String content =
        this.patchRequest(
            url + "/profile?onlycache=" + onlyCache, new Gson().toJson(updatedProfile));

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public Profile updateProfile(String url, Profile updatedProfile)
      throws ServiceUnreachableException {
    String content = this.patchRequest(url + "/profile", new Gson().toJson(updatedProfile));

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }

  public Profile updateProfileLogin(String url, Profile profile)
      throws ServiceUnreachableException {
    String content = this.patchRequest(url + "/profile/login/refresh", new Gson().toJson(profile));

    if (content.isEmpty()) {
      return null;
    }

    return new Gson().fromJson(content, Profile.class);
  }
}
