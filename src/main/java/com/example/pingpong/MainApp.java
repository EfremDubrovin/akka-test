package com.example.pingpong;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MainApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("game-system");
        ActorRef gameEngine = system.actorOf(Props.create(GameEngine.class));

        gameEngine.tell("start", ActorRef.noSender());
    }
}
