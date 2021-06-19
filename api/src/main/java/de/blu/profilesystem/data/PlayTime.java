package de.blu.profilesystem.data;

import lombok.Getter;

@Getter
public class PlayTime {

  /** The time in Millis when the PlayTime started */
  private long from;

  /** The time in Millis when the PlayTime ended. Is -1 if not ended yet */
  private long to = -1;

  /**
   * Get the Duration in milliseconds
   *
   * @return milliseconds between the from and to of this PlayTime
   */
  public long duration(){
    if (this.to == -1) {
      return System.currentTimeMillis() - this.from;
    }

    return this.to - this.from;
  }
}
