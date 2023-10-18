package com.kazama.SpringOAuth2.util.ratelimit;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.util.ratelimit.bandwidthprovider.BandwidthProvider;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;

@Component
public class BucketFactory {
    @Autowired
    private ProxyManager<String> proxyManager;

    private Map<String, BandwidthProvider> bandwidthProviderList = new ConcurrentHashMap<>();

    private Supplier<BucketConfiguration> getConfigSupplier(String uri)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        return bandwidthProviderList.entrySet()
                .stream()
                .filter(entry -> uri.contains(entry.getKey()))
                .map(entry -> {
                    BandwidthProvider provider = entry.getValue();
                    Bandwidth limit = provider.getBandwidth();
                    return (Supplier<BucketConfiguration>) () -> BucketConfiguration.builder().addLimit(limit).build();
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("URI not found in the map. \n"));

    }

    public Bucket getBucket(String uri)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Supplier<BucketConfiguration> configSupplier = getConfigSupplier(uri);
        return proxyManager.builder().build(uri, configSupplier);
    }

    public void register(String uri, BandwidthProvider bucket) {
        bandwidthProviderList.put(uri, bucket);
    }
}
