package com.shopkart.payment;

public class CardPayment extends PaymentMethod {
    private final String cardLast4;

    public CardPayment(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println(receipt(amount, "Card ending " + cardLast4));
        return true;
    }
}
