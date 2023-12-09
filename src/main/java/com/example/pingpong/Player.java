package com.example.pingpong;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;

public class Player extends AbstractActor {

    private int lives;

    public record Ping() {}

    public record Pong() {}

    public Player(int lives) {
        this.lives = lives;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                   .match(Ping.class, ping -> {
                       Logging.getLogger(this).info("Received ping");
                       Thread.sleep(200); // NEVER DO
                       getSender().tell(new Pong(), self());
                       handleLives();
                   })
                   .match(Pong.class, pong -> {
                       Logging.getLogger(this).info("Received pong ");
                       Thread.sleep(200); // NEVER DO
                       getSender().tell(new Ping(), self());
                       handleLives();
                   })
                   .build();
    }

    private void handleLives() {
        lives--;
        if (lives == 0) {
            Logging.getLogger(this).info("Committing sepuko on 0 lives...");
            throw new RuntimeException("Ahhh... died");
        }
        Logging.getLogger(this).info("lives remaining: " + lives);
    }

    public static Props props(int lives) {
        return Props.create(Player.class, lives);
    }
}
