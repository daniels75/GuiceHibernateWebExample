package org.daniels.examples.model;

import java.io.Serializable;

/**
 * Simple Article-Model.
 * 
 * @author Siegfried Bolz
 */
public class Article implements Serializable{
    private Long id;
    private String name;
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
} // .EOF
