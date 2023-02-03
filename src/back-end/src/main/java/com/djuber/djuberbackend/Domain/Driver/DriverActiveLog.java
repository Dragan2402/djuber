package com.djuber.djuberbackend.Domain.Driver;

import com.djuber.djuberbackend.Domain.Authentication.Identity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverActiveLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "driverId" , nullable = false)
    Driver driver;

    @Column(name = "logStart" , nullable = false)
    OffsetDateTime logStart;

    @Column(name = "logEnd" , nullable = false)
    OffsetDateTime logEnd;

    @Override
    public String toString() {
        return "DriverActiveLog{" +
                "id=" + id +
                ", driver=" + driver.getId() +
                ", logStart=" + logStart +
                ", logEnd=" + logEnd +
                '}';
    }
}
