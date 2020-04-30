package net.scaliby.marketaggregator.data.concurrency;

public interface ChannelRunnable<K> extends Runnable {

    K getChannel();

}
