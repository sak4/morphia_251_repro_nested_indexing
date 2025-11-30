package com.foo;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.UUID;

@Entity
public class MyEntity {
    @Id
    private String id = UUID.randomUUID().toString();
    private String name;
    private Child child;

    public MyEntity(final String name, final Child child) {
        this.name = name;
        this.child = child;
    }
}
