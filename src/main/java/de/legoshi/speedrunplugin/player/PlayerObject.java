package de.legoshi.speedrunplugin.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerObject {

    private Player player;
    private int courseID;

    private long courseTime;
    private boolean inCourse;

    private Location onDelete;

    public PlayerObject(Player player, int courseID, long courseTime, boolean inCourse) {
        this.player = player;
        this.courseID = courseID;
        this.courseTime = courseTime;
        this.inCourse = inCourse;
        this.onDelete = null;
    }

    public Location getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(Location onDelete) {
        this.onDelete = onDelete;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public long getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(long courseTime) {
        this.courseTime = courseTime;
    }

    public boolean isInCourse() {
        return inCourse;
    }

    public void setInCourse(boolean inCourse) {
        this.inCourse = inCourse;
    }
}
