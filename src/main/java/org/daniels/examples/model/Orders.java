package org.daniels.examples.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple Orders-Model.
 * 
 * @author Siegfried Bolz
 */
public class Orders implements Serializable {

    private Long id;
    private Customer customer;
    private Article article;
    private int amount;
    private Date orderDate;
    private double sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
} // .EOF
