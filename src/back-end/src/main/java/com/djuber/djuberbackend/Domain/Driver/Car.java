package com.djuber.djuberbackend.Domain.Driver;

import com.djuber.djuberbackend.Domain._Common.Coordinates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE car SET deleted = true WHERE id = ?")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carType", nullable = false)
    private CarType carType;

//    @OneToOne
//    @JoinColumn(name = "coordinatesId")
//    private Coordinates coordinates;

    @OneToOne(mappedBy = "car")
    private Driver driver;

    @Column(name = "x", nullable = false)
    public Double x;

    @Column(name = "y", nullable = false)
    public Double y;

    @Column(name = "additionalServices", nullable = false)
    @ElementCollection(targetClass = String.class)
    private Set<String> additionalServices;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}

