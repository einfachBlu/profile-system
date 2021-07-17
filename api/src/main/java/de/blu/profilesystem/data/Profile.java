package de.blu.profilesystem.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Profile {

  /** Unique identifier of this profile */
  private UUID id;

  /** If this Profile is disabled/banned */
  private boolean disabled;

  /** Unique Name of this Profile. Used to filter for this name */
  private String name;

  /** The Player UniqueId which is linked to this Profile */
  private UUID playerId;

  /** The Player UniqueId which is currently logged in to this Profile */
  private UUID loggedInPlayerId;

  /** The Time in milliseconds when this Profile was created */
  private long creationTime;

  /** The last Time in milliseconds when someone logged in with this Profile */
  private List<PlayTime> playTimes = new ArrayList<>();

  /** Additional extra Data can be stored here for this Profile */
  private Map<String, JsonObject> data = new HashMap<>();

  @Override
  public String toString() {
    return this.toJson().toString();
  }

  public JsonObject toJson() {
    return new Gson().toJsonTree(this).getAsJsonObject();
  }

  /**
   * Copy all values of the instance to this
   *
   * @param profile the profile to use the values from
   */
  public void copyFrom(Profile profile) {
    this.id = profile.getId();
    this.name = profile.getName();
    this.disabled = profile.isDisabled();
    this.playerId = profile.getPlayerId();
    this.loggedInPlayerId = profile.getLoggedInPlayerId();
    this.creationTime = profile.getCreationTime();
    this.playTimes = profile.getPlayTimes();
    this.data = profile.getData();
  }
}
