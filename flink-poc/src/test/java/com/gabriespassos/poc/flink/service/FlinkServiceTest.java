package com.gabriespassos.poc.flink.service;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FlinkServiceTest {

    private FlinkService flinkService;

    @Before
    public void setup() {
        this.flinkService = new FlinkService();
    }

    @Test
    public void shouldJoinData() throws Exception {
        List<Tuple2<Tuple3<String, Integer, Double>, Tuple3<Integer, String, String>>> joined = flinkService.joinDataSources();

        assertEquals(3, joined.size());
        assertEquals("transaction_1", joined.get(0).f0.f0);
        assertEquals("London", joined.get(0).f1.f2);
        assertEquals(50.50, joined.get(0).f0.f2, 0);

        assertEquals("transaction_3", joined.get(1).f0.f0);
        assertEquals("London", joined.get(1).f1.f2);
        assertEquals(20.0, joined.get(1).f0.f2,0);

        assertEquals("transaction_2", joined.get(2).f0.f0);
        assertEquals("Liverpool", joined.get(2).f1.f2);
        assertEquals(125.8, joined.get(2).f0.f2, 0);
    }

    @Test
    public void shouldSortDataDescending() throws Exception {
        List<Tuple3<String, Integer, Double>> sorted = flinkService.sortTransactions();

        assertEquals(4, sorted.size());
        assertEquals("transaction_200", sorted.get(0).f0);
        assertEquals("transaction_5", sorted.get(1).f0);
        assertEquals("transaction_4", sorted.get(2).f0);
        assertEquals("transaction_2", sorted.get(3).f0);
    }

    @Test
    public void shouldMapData() throws Exception {
        List<Integer> mapped = flinkService.mapDataSource();

        assertEquals(22, mapped.get(0).intValue());
        assertEquals(20, mapped.get(1).intValue());
    }

    @Test
    public void shouldFilterData() throws Exception {
        List<Integer> filtered = flinkService.filterAmounts();

        assertEquals(1, filtered.size());
        assertEquals(160, filtered.get(0).intValue());
    }

    @Test
    public void shouldReturnJoinedCsvs() throws Exception {
        List<Tuple2<Tuple3<String, Integer, Double>, Tuple3<Integer, String, String>>> joined = flinkService.joinCsvFiles();

        assertEquals(3, joined.size());
        assertEquals("transaction_3", joined.get(0).f0.f0);
        assertEquals("London", joined.get(0).f1.f1);
        assertEquals(20.0, joined.get(0).f0.f2, 0);

        assertEquals("transaction_1", joined.get(1).f0.f0);
        assertEquals("London", joined.get(1).f1.f1);
        assertEquals(50.50, joined.get(1).f0.f2,0);

        assertEquals("transaction_2", joined.get(2).f0.f0);
        assertEquals("Liverpool", joined.get(2).f1.f1);
        assertEquals(125.8, joined.get(2).f0.f2, 0);
    }
}
