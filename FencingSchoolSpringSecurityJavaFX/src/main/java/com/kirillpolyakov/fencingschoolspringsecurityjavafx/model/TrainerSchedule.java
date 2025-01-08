package com.kirillpolyakov.fencingschoolspringsecurityjavafx.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kirillpolyakov.fencingschoolspringsecurityjavafx.util.Constants;
import lombok.*;

import java.lang.reflect.Field;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TrainerSсhedule – расписание тренера, показывает в какие дни недели в какое время тренер ведет занятия, поля:
 * idTrainer
 * mondayStart
 * mondayEnd
 * tuesdayStart
 * tuesdayEnd
 * wednesdayStart
 * wednesdayEnd
 * thursdayStart
 * thursdayEnd
 * fridayStart
 * fridayStart
 * saturdayStart
 * saturdayEnd
 * sundayStart
 * sundayEnd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class TrainerSchedule {

    private long id;

    @NonNull
    private User trainer;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime mondayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime mondayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime tuesdayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime tuesdayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime wednesdayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime wednesdayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime thursdayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime thursdayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime fridayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime fridayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime saturdayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime saturdayEnd;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime sundayStart;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime sundayEnd;

    public void set(String day, LocalTime start, LocalTime end) {
        try {
            this.getClass().getDeclaredField(day + "Start").set(this, start);
            this.getClass().getDeclaredField(day + "End").set(this, end);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Incorrect name of day");
        } catch (IllegalAccessException ignored) {
        }
    }

    public void delete(String day) {
        this.set(day, null, null);
    }

    public LocalTime[] get(String day) {
        LocalTime startTime = null;
        LocalTime endTime = null;
        try {
            Field fieldStart = this.getClass().getDeclaredField(day + "Start");
            Field fieldEnd = this.getClass().getDeclaredField(day + "End");
            fieldStart.setAccessible(true);
            fieldEnd.setAccessible(true);
            startTime = (LocalTime) fieldStart.get(this);
            endTime = (LocalTime) fieldEnd.get(this);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Incorrect name of day");
        } catch (IllegalAccessException ignored) {
        }
        return new LocalTime[]{startTime, endTime};
    }

    public List<TrainerScheduleItem> get() {
        List<TrainerScheduleItem> res = new ArrayList<>();
        try {
            for (Map.Entry<String, String> dayDay : Constants.DAYS.entrySet()) {
                LocalTime start = (LocalTime) this.getClass().getDeclaredField(dayDay.getValue() + "Start").get(this);
                LocalTime end = (LocalTime) this.getClass().getDeclaredField(dayDay.getValue() + "End").get(this);
                if(start != null && end != null) {
                    TrainerScheduleItem trainerScheduleItem = new TrainerScheduleItem(dayDay.getKey(), dayDay.getValue(), start, end);
                    res.add(trainerScheduleItem);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
