package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Time;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "displaydevice_onhours")
public class DisplayDeviceOnHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "displaydevice_onhours_id_seq")
    @SequenceGenerator(name = "displaydevice_onhours_id_seq", sequenceName = "displaydevice_onhours_id_seq", allocationSize = 1)
    protected Integer id;
    private Time monday_start;
    private Time monday_end;
    private Time tuesday_start;
    private Time tuesday_end;
    private Time wednesday_start;
    private Time wednesday_end;
    private Time thursday_start;
    private Time thursday_end;
    private Time friday_start;
    private Time friday_end;
    private Time saturday_start;
    private Time saturday_end;
    private Time sunday_start;
    private Time sunday_end;
}
