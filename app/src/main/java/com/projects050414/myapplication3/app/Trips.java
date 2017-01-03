package com.projects050414.myapplication3.app;

/**
 * Created by obelix on 12/05/2014.
 */
public class Trips {
        private String trip_id;
        private String from;
        private String to;
        private String date;
        private String time;
        private String owner;

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public Trips()
    {
        this.trip_id = trip_id;
        this.from = GlobalValues.orig_address;
        this.to = GlobalValues.dest_address;
        this.date = GlobalValues.car_share_trip_date;
        this.time = GlobalValues.car_share_trip_time;
        this.owner = owner;
    }
    public Trips(String trip_id, String from, String to, String date, String time, String owner) {
        super();

    }
    public String getTripId()
    {
        return trip_id;
    }
    public String getFrom()
    {
        return from;
    }
    public String[] getTripInfo()
    {
        String trips[] = null;

        return trips;
    }

    @Override
    public String toString() {
        return "Trip [id=" + trip_id+ "from: "+from + ", to=" + to + ", date=" + date
                +", time="+ time+ "]";
    }

    public String getOwner() {
        return owner;
    }

    public String getTo() {
        return to;
    }

    public void setTripId(String tripId) {
        this.trip_id = tripId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
