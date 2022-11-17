package ac.sanbernardo.prenoto.model.dto

import io.micronaut.core.annotation.Introspected

@Introspected
public class HourTrend {
    int hour
    int value

    HourTrend(int hour, int value){
        this.hour = hour
        this.value = value
    }

    @Override
    public String toString() {
        return "HourTrend{" +
                "hour=" + hour +
                ", value=" + value +
                '}';
    }
}
