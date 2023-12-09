package com.example.restaurant;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.Cluster;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HalalDonersApp extends AbstractBehavior<HalalDonersApp.NahraniDinko> {

    private final ActorRef<Gotvacha.Order> baiIvan;
    private final ActorRef<Customer.Meal> dinko;

    public HalalDonersApp(ActorContext<NahraniDinko> context) {
        super(context);
        this.baiIvan = context.spawn(Gotvacha.create(), "baiIvan");
        this.dinko = context.spawn(Customer.create(), "dinko");
    }

    public record NahraniDinko() {}

    @Override
    public Receive<NahraniDinko> createReceive() {
        return newReceiveBuilder()
                   .onMessage(NahraniDinko.class, this::nahraniDinko)
                   .build();
    }

    private Behavior<NahraniDinko> nahraniDinko(NahraniDinko nahraniDinko) {
        List<String> hrana = Arrays.asList("banica", "boza");

        getContext().getLog().info("Da nahranim Dinko s: {}", hrana);

        baiIvan.tell(new Gotvacha.Order(hrana, dinko));
        baiIvan.tell(new Gotvacha.Order(Collections.singletonList("Pizzalab pizza"), dinko));
        baiIvan.tell(new Gotvacha.Order(Collections.singletonList("chicken wings"), dinko));
        return this;
    }


    public static void main(String[] args) {
        ActorSystem<NahraniDinko> dinko = ActorSystem.create(HalalDonersApp.create(), "Dunerdjiinicata");
        dinko.tell(new NahraniDinko());
    }

    public static Behavior<NahraniDinko> create() {
        return Behaviors.setup(HalalDonersApp::new);
    }
}
