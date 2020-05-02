package net.scaliby.marketaggregator.core.concurrency;

public interface ChannelRunnable<K> extends Runnable {

    K getChannel();

}
