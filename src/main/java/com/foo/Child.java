package com.foo;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;

@Entity
public class Child {
    @Id
    private String id = java.util.UUID.randomUUID().toString();

    @Indexed
    private String name;

    public Child(final String name) {
        this.name = name;
    }
}
