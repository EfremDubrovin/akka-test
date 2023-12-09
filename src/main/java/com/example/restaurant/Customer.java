package com.example.restaurant;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.util.List;

public class Customer extends AbstractBehavior<Customer.Meal> {

    public record Meal(List<String> meals) {}

    public Customer(ActorContext<Meal> context) {
        super(context);
        getContext().getLog().info("Created customer");
    }

    @Override
    public Receive<Meal> createReceive() {
        return newReceiveBuilder()
                   .onMessage(Meal.class, this::consumeMeal)
                   .build();
    }

    private Behavior<Meal> consumeMeal(Meal meal) {
        meal.meals().forEach(mealItem -> getContext().getLog().info("NomNomNom: {}", mealItem));
        return this;
    }

    public static Behavior<Customer.Meal> create() {
        return Behaviors.setup(Customer::new);
    }
}
