package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerScheduleItem implements Comparable<TrainerScheduleItem>{

    public String rusDay;

    public String engDay;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    public LocalTime timeStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    public LocalTime timeEnd;

    @Override
    public int compareTo(TrainerScheduleItem o) {
        return Integer.compare(Constants.SORTED_DAYS.indexOf(this.rusDay), Constants.SORTED_DAYS.indexOf(o.rusDay));
    }
}
