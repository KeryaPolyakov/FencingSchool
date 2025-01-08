package com.kirillpolyakov.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerScheduleItem {

    public String rusDay;

    public String engDay;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    public LocalTime timeStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    public LocalTime timeEnd;

}
