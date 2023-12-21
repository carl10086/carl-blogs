package com.cb.examples.flink.ride.common.datatypes;

import com.cb.examples.flink.ride.common.utils.DataGenerator;
import java.time.Instant;

public class TaxiRide implements Comparable<TaxiRide> {

  public long rideId;
  public boolean isStart;
  public Instant eventTime;
  public float startLon;
  public float startLat;
  public float endLon;
  public float endLat;
  public short passengerCnt;
  public long taxiId;
  public long driverId;

  public TaxiRide() {
    this.eventTime = Instant.now();
  }

  /** Invents a TaxiRide. */
  public TaxiRide(long rideId, boolean isStart) {
    DataGenerator g = new DataGenerator(rideId);

    this.rideId = rideId;
    this.isStart = isStart;
    this.eventTime = isStart ? g.startTime() : g.endTime();
    this.startLon = g.startLon();
    this.startLat = g.startLat();
    this.endLon = g.endLon();
    this.endLat = g.endLat();
    this.passengerCnt = g.passengerCnt();
    this.taxiId = g.taxiId();
    this.driverId = g.driverId();
  }

  /** Creates a TaxiRide with the given parameters. */
  public TaxiRide(
      long rideId,
      boolean isStart,
      Instant eventTime,
      float startLon,
      float startLat,
      float endLon,
      float endLat,
      short passengerCnt,
      long taxiId,
      long driverId
  ) {
    this.rideId = rideId;
    this.isStart = isStart;
    this.eventTime = eventTime;
    this.startLon = startLon;
    this.startLat = startLat;
    this.endLon = endLon;
    this.endLat = endLat;
    this.passengerCnt = passengerCnt;
    this.taxiId = taxiId;
    this.driverId = driverId;
  }

  /** Gets the ride's time stamp as a long in millis since the epoch. */
  public long getEventTimeMillis() {
    return eventTime.toEpochMilli();
  }

  @Override
  public int compareTo(TaxiRide other) {
    if (other == null) {
      return 1;
    }
    int compareTimes = this.eventTime.compareTo(other.eventTime);
    if (compareTimes == 0) {
      if (this.isStart == other.isStart) {
        return 0;
      } else {
        if (this.isStart) {
          return -1;
        } else {
          return 1;
        }
      }
    } else {
      return compareTimes;
    }
  }
}
