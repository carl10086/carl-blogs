package com.cb.examples.flink.ride.job;

import com.cb.examples.flink.ride.common.datatypes.TaxiRide;
import com.cb.examples.flink.ride.common.sources.TaxiRideGenerator;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class RideCountJob {

  public static void main(String[] args) throws Exception {
    // 执行环境, local yarn?
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // 1. 数据源
    DataStream<TaxiRide> rides = env.addSource(new TaxiRideGenerator());

    // 2. countByDriverId
    // map each ride to a tuple of (driverId, 1)
    DataStream<Tuple2<Long, Long>> tuples =
        rides.map(
            new MapFunction<TaxiRide, Tuple2<Long, Long>>() {
              @Override
              public Tuple2<Long, Long> map(TaxiRide ride) {
                return Tuple2.of(ride.driverId, 1L);
              }
            });

    // partition the stream by the driverId
    KeyedStream<Tuple2<Long, Long>, Long> keyedByDriverId = tuples.keyBy(t -> t.f0);

    // count the rides for each driver
    DataStream<Tuple2<Long, Long>> rideCounts = keyedByDriverId.sum(1);

    // 3. 使用 print 作为简单的 sink
    rideCounts.print();

    // 4. 任务执行
    env.execute("Ride count");
  }
}
