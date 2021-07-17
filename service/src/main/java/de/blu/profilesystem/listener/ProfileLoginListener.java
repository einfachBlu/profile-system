package de.blu.profilesystem.listener;

import de.blu.profilesystem.data.Profile;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public final class ProfileLoginListener {

  public void onLogin(Profile profile, UUID playerId) {
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
    // DiscordWebhook.PROFILE_LOGIN_WEBHOOK.sendEmbed(null, description, Color.GREEN);
  }
}
