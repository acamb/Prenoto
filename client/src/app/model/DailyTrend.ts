export interface DailyTrend{
    day: number;
    data: HourlyTrend[];
}

export interface HourlyTrend{
    hour: number;
    value: number;
}