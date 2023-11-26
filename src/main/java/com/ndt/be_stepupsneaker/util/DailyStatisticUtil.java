package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyGrowthResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DailyStatisticUtil {

    public static AdminDailyStatisticResponse getDailyStatisticResponse(List<Statistic> statistics) {

        if (statistics.isEmpty()) {
            return new AdminDailyStatisticResponse();
        }

        Float initialRevenue = getStatisticValue(statistics, 0);
        Float finalRevenue = getStatisticValue(statistics, statistics.size() - 1);

        if (initialRevenue == null || finalRevenue == null) {
            return new AdminDailyStatisticResponse();
        }

        float growth = calculateGrowth(initialRevenue, finalRevenue);

        Float total = calculateTotal(statistics);

        AdminDailyStatisticResponse dailyRevenueResponse = new AdminDailyStatisticResponse();
        dailyRevenueResponse.setData(statistics);
        dailyRevenueResponse.setTotal(total);
        dailyRevenueResponse.setTrend(Math.round(growth));
        return dailyRevenueResponse;

    }

    public static List<AdminDailyGrowthResponse> getDailyGrowth(List<Statistic> statistics) {
        List<AdminDailyGrowthResponse> dailyGrowthList = new ArrayList<>();

        for (int i = 0; i < statistics.size(); i++) {
            Float todayRevenue = getStatisticValue(statistics, i);
            Float yesterdayRevenue = getStatisticValue(statistics, i - 1);

            if (todayRevenue != null && yesterdayRevenue != null && yesterdayRevenue != 0) {
                float dailyGrowth = calculateGrowth(yesterdayRevenue, todayRevenue);

                AdminDailyGrowthResponse dailyGrowthResponse = new AdminDailyGrowthResponse();
                dailyGrowthResponse.setDate(statistics.get(i).getDate());
                dailyGrowthResponse.setDailyGrowth(Math.round(dailyGrowth));

                dailyGrowthList.add(dailyGrowthResponse);
            } else {
                dailyGrowthList.add(null);
            }
        }

        return dailyGrowthList;
    }

    private static Float getStatisticValue(List<Statistic> statistics, int index) {
        return (index >= 0 && index < statistics.size()) ? statistics.get(index).getValue() : null;
    }

    private static float calculateGrowth(Float initial, Float finalValue) {
        return (finalValue - initial) / initial * 100;
    }

    private static Float calculateTotal(List<Statistic> statistics) {
        return statistics.stream()
                .map(Statistic::getValue)
                .filter(Objects::nonNull)
                .reduce(0f, Float::sum);
    }

}
