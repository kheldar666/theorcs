package org.libermundi.theorcs.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.libermundi.theorcs.services.RSSService;

import static org.junit.Assert.*;

public class RSSServiceImplTest {
    private RSSService rssService;

    @Before
    public void setUp() throws Exception {
        rssService = new RSSServiceImpl();
    }

    @Test
    public void reedFeed() {
        rssService.reedFeed();
    }

    @Test
    public void reedFeed1() {
    }
}