package com.ndt.be_stepupsneaker.core.admin.dto.response.statistic;

import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDailyGrowthResponse {

    private String name;
    private Long date;
    private int dailyGrowth;

}
