package ac.sanbernardo.prenoto.model.dto

import io.micronaut.core.annotation.Introspected


class DailyTrend {

    int day
    List<HourTrend> data = []


    @Override
    public String toString() {
        return "DailyTrend{" +
                "day=" + day +
                ", data=" + data +
                '}';
    }
}

