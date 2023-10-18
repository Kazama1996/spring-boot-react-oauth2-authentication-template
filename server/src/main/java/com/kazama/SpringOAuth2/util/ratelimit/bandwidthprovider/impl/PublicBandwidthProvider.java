package com.kazama.SpringOAuth2.util.ratelimit.bandwidthprovider.impl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kazama.SpringOAuth2.util.ratelimit.BucketFactory;
import com.kazama.SpringOAuth2.util.ratelimit.bandwidthprovider.BandwidthProvider;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class PublicBandwidthProvider implements BandwidthProvider {
    @Autowired
    private BucketFactory bucketFactory;

    @PostConstruct
    private void init() {
        bucketFactory.register("/public", this);
    }

    @Override
    public Bandwidth getBandwidth() {
        Refill refill = Refill.intervally(5, Duration.ofSeconds(10));
        return Bandwidth.classic(10, refill).withInitialTokens(10);

    }

}
