package com.example.restaurant;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.util.List;

public class Gotvacha extends AbstractBehavior<Gotvacha.Order> {

    public record Order(List<String> orders, ActorRef<Customer.Meal> replyTo) {
    }

    public Gotvacha(ActorContext<Order> context) {
        super(context);
        getContext().getLog().info("Created gotvach");
    }

    @Override
    public Receive<Order> createReceive() {
        return newReceiveBuilder()
                   .onMessage(Order.class, this::handleOrder)
                   .build();
    }

    private Behavior<Order> handleOrder(Order order) {
        getContext().getLog().info("Preparing meals for order: {}", order.orders());
        List<String> preparedMeals = order.orders.stream()
                                                 .map(orderItem -> "Meal of: " + orderItem)
                                                 .toList();
        getContext().getLog().info("Prepared meals: {}", preparedMeals);

        Customer.Meal meal = new Customer.Meal(preparedMeals);
        throw new RuntimeException("boom");
//        order.replyTo().tell(meal);
//        return this;
    }

    public static Behavior<Gotvacha.Order> create() {
        return Behaviors.setup(Gotvacha::new);
    }
}
