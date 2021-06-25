package de.blu.profilesystem.listener;

import de.blu.profilesystem.data.Profile;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class ProfileLoginListener {

  public void onLogin(Profile profile, UUID playerId) {
    System.out.println("Profile Login " + profile.getName() + " by Player " + playerId);
  }
}
