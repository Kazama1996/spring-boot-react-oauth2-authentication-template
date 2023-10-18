package com.kazama.SpringOAuth2.util.ratelimit.bandwidthprovider;

import io.github.bucket4j.Bandwidth;

public interface BandwidthProvider {
    Bandwidth getBandwidth();
}
