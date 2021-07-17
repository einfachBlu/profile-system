package de.blu.profilesystem.listener;

import de.blu.profilesystem.data.Profile;
import de.blu.profilesystem.util.DiscordWebhook;

import javax.inject.Singleton;
import java.awt.*;
import java.util.UUID;

@Singleton
public final class ProfileLogoutListener {

  public void onLogout(Profile profile, UUID playerId) {
    String description =
            ""
                    + "**Profile**:"
                    + "```"
                    + profile.getName()
                    + "```"
                    + "\n"
                    + "**Profile Id**:"
                    + "```"
                    + profile.getId()
                    + "```"
                    + "\n"
                    + "**Profile Owner**:"
                    + "```"
                    + profile.getPlayerId()
                    + "```"
                    + "\n"
                    + "**Player**:"
                    + "```"
                    + playerId
                    + "```"
                    + "";
    DiscordWebhook.PROFILE_LOGOUT_WEBHOOK.sendEmbed(null, description, Color.RED);
  }
}
