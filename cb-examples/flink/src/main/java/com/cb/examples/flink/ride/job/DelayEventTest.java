package com.cb.examples.flink.ride.job;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.eventtime.TimestampAssigner;
import org.apache.flink.api.common.eventtime.TimestampAssignerSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class DelayEventTest {

  public static String t2s(long time) {
    return FastDateFormat.getInstance("yyyyMMdd HH:mm:ss").format(time);
  }


  public static class DelayEvent {

    public long occurAt;
    public int id;

    public int userId;

    public DelayEvent() {

    }

    public DelayEvent(long occurAt, int id) {
      this.occurAt = occurAt;
      this.id = id;
      this.userId = 1;
    }

    public String toString() {
      return String.format("occurAt: %s, id: %d", t2s(occurAt), id);
    }
  }


  public static class DelayEventGenerator implements SourceFunction<DelayEvent> {

    @Override
    public void run(SourceContext<DelayEvent> ctx) throws Exception {
      System.out.println(Thread.currentThread().getName());
      for (int i = 0; i < 5; i++) {
        var event = new DelayEvent(System.currentTimeMillis(), i);
        ctx.collect(event);
        Thread.sleep(1000L);
      }
      Thread.sleep(10L * 1000);
      ctx.collect(new DelayEvent(System.currentTimeMillis(), 5));
    }

    @Override
    public void cancel() {

    }
  }

  public static void main(String[] args) throws Exception {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(4);
    DataStream<DelayEvent> sources = env.addSource(new DelayEventGenerator());

    WatermarkStrategy<DelayEvent> strategy = WatermarkStrategy.<DelayEvent>forBoundedOutOfOrderness(Duration.ofSeconds(
            2L))
        .withTimestampAssigner((event, currentTime) -> event.occurAt);

    DataStream<DelayEvent> sourcesWithTimestampsAndWatermarks = sources.assignTimestampsAndWatermarks(strategy);

    var stream = sourcesWithTimestampsAndWatermarks.keyBy((KeySelector<DelayEvent, Integer>) delayEvent -> delayEvent.userId)
        .window(TumblingEventTimeWindows.of(Time.of(1L, TimeUnit.SECONDS)));

    // 在原有代码的后面
    stream.process(new ProcessWindowFunction<DelayEvent, String, Integer, TimeWindow>() {
      @Override
      public void process(Integer key, Context context, Iterable<DelayEvent> events, Collector<String> out)
          throws Exception {
        ArrayList<DelayEvent> arrayList = new ArrayList<>();
        for (DelayEvent event : events) {
          arrayList.add(event);
        }

        out.collect(
            "Now:" + t2s(System.currentTimeMillis()) + ", Window: " + t2s(context.window().getStart()) + "->" + t2s(
                context.window().getEnd()) + ", Events: "
                + arrayList);
      }
    }).print();

    env.execute();

  }

}