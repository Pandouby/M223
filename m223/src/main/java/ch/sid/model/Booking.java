package ch.sid.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity(name = "BOOKING")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @Column(name = "day_duration", nullable = false)
    private float dayDuration;

    @Column(name = "date", nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(name = "status", nullable = false)
    private String status;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public float getDayDuration() {
        return dayDuration;
    }

    public void setDayDuration(float dayDuration) {
        this.dayDuration = dayDuration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
