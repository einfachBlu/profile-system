package de.blu.profilesystem.storage;

import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.repository.ProfileRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Storage {

  /**
   * Initializes the Storage. e.g. connecting to the SQL/NoSQL Database or creates the Configs in
   * case of a local storage
   */
  void init();

  /**
   * Load the Profiles from the Storage and cache them in the local Repository
   *
   * @see ProfileRepository
   */
  void load();

  /**
   * Check wether a Profile exist in the Storage
   *
   * @param profile the Profile
   * @return true if exist, otherwise false
   */
  boolean contains(Profile profile);

  /**
   * Get a list of all Profiles in the Storage
   *
   * @return a list with all Profiles stored in the Storage
   */
  List<Profile> all();

  /**
   * Get all Profiles in the Storage which are linked to a specific player
   *
   * @param playerId the player unique id linked to the profiles
   * @return list with profiles if found, otherwise empty list
   */
  default List<Profile> getById(UUID playerId) {
    return this.all().stream()
        .filter(profile -> profile.getPlayerId().equals(playerId))
        .collect(Collectors.toList());
  }

  /**
   * Insert a Profile into the Storage
   *
   * @param profile the Profile
   */
  void insert(Profile profile);

  /**
   * Delete a Profile from the Storage
   *
   * @param profile the Profile
   */
  void delete(Profile profile);

  /**
   * Update a Profile into the Storage The Id will be used as a reference to the previous Profile
   *
   * @param profile the Profile with the new data
   */
  void update(Profile profile);

  /**
   * Update a Profile into the Storage The Id will be used as a reference to the previous Profile
   *
   * @param profile the Profile with the new data
   * @param onlyCache if true, only caches will be updated
   */
  void update(Profile profile, boolean onlyCache);

  /**
   * Get the Repository which contains all locally cached Profile Information
   *
   * @return the Repository
   */
  ProfileRepository getProfileRepository();
}
