package de.blu.profilesystem.util;

import com.google.gson.Gson;
import de.blu.profilesystem.data.PlayTime;
import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.exception.ServiceUnreachableException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class ProfileWebRequester extends WebRequester {

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

  public Profile createProfile(String url, UUID playerId, String name)
      throws ServiceUnreachableException {
    Profile profile = new Profile();
    profile.setPlayerId(playerId);
    profile.setName(name);

    String content = this.putRequest(url + "/profile", profile.toString());

    if (content.isEmpty()) {
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

  public void login(String url, UUID playerId, Profile profile) throws ServiceUnreachableException {
    profile.setLoggedInPlayerId(playerId);

    // PlayTime
    // Check if merge is possible
    if (profile.getPlayTimes().size() > 0) {
      PlayTime lastPlayTime = profile.getPlayTimes().get(profile.getPlayTimes().size() - 1);
      if (lastPlayTime.getTo() != -1) {
        if ((System.currentTimeMillis() - lastPlayTime.getTo()) <= TimeUnit.SECONDS.toMillis(5)) {
          lastPlayTime.setTo(-1);
          this.updateProfile(url, profile);
          return;
        }
      }
    }

    profile.getPlayTimes().add(new PlayTime());
    PlayTime playTime = profile.getPlayTimes().get(profile.getPlayTimes().size() - 1);
    playTime.setFrom(System.currentTimeMillis());

    this.updateProfile(url, profile);
  }

  public void disableProfile(String url, Profile profile) throws ServiceUnreachableException {
    profile.setDisabled(true);

    this.updateProfile(url, profile);
  }

  public void enableProfile(String url, Profile profile) throws ServiceUnreachableException {
    profile.setDisabled(false);

    this.updateProfile(url, profile);
  }

  public void logout(String url, Profile profile) throws ServiceUnreachableException {
    profile.setLoggedInPlayerId(null);

    // PlayTime
    PlayTime playTime = profile.getPlayTimes().get(profile.getPlayTimes().size() - 1);
    playTime.setTo(System.currentTimeMillis());

    this.updateProfile(url, profile);
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
}
