package com.islesmc.auctionhouse.dto;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BiddableItem {
    private final GooseItem gooseItem;
    private final AtomicInteger currentPrice;
    private final UUID seller;
    private UUID currentBidder;
    private AuctionEndResponse auctionEndResponse;

    public BiddableItem(final GooseItem gooseItem, final Integer initialPrice, final UUID seller) {
        this.gooseItem = gooseItem;
        this.currentPrice = new AtomicInteger(initialPrice);
        this.currentBidder = null;
        this.seller = seller;



        this.auctionEndResponse = AuctionEndResponse.OTHER;
    }

    public GooseItem getGooseItem() {
        return gooseItem;
    }

    public AtomicInteger getCurrentPrice() {
        return currentPrice;
    }

    public UUID getCurrentBidder() {
        return currentBidder;
    }

    public void setCurrentBidder(final UUID uuid) {
        this.currentBidder = uuid;
    }
    public AuctionEndResponse getAuctionEndResponse() {
        return auctionEndResponse;
    }

    public void setAuctionEndResponse(AuctionEndResponse auctionEndResponse) {
        this.auctionEndResponse = auctionEndResponse;
    }

    public UUID getSeller() {
        return seller;
    }
}
