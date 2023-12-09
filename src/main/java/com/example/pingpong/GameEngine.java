package com.example.pingpong;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import java.time.Duration;

public class GameEngine extends AbstractActor {

    private final ActorRef ping;
    private final ActorRef pong;

    public GameEngine() {
        this.ping = getContext().actorOf(Player.props(10), "ping");
        this.pong = getContext().actorOf(Player.props(2), "pong");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                   .matchAny(o -> ping.tell(new Player.Pong(), pong))
                   .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
            2,
            Duration.ofMinutes(1),
            DeciderBuilder.match(ArithmeticException.class, e -> SupervisorStrategy.resume())
                          .match(RuntimeException.class, e -> SupervisorStrategy.restart())
                          .match(NullPointerException.class, e -> SupervisorStrategy.stop())
                          .matchAny(o -> SupervisorStrategy.escalate())
                          .build());
    }
}
