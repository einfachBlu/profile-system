package de.blu.profilesystem.listener;

import de.blu.profilesystem.data.Profile;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class ProfileLogoutListener {

  public void onLogout(Profile profile, UUID playerId) {
    System.out.println("Profile Logout " + profile.getName() + " by Player " + playerId);
  }
}
