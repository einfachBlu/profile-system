package de.blu.profilesystem.rest;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.blu.profilesystem.rest.listener.*;
import spark.Spark;

@Singleton
public final class WebInitializer {

  @Inject private Injector injector;

  public void init() {
    Spark.port(8081);

    Spark.get("/profiles", this.injector.getInstance(ShowProfiles.class));
    Spark.put("/profile", this.injector.getInstance(InsertProfile.class));
    Spark.delete("/profile", this.injector.getInstance(DeleteProfile.class));
    Spark.patch("/profile", this.injector.getInstance(UpdateProfile.class));

    Spark.awaitInitialization();
  }
}
