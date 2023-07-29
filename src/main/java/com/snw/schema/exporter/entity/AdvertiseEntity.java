
package com.snw.schema.exporter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;


@Entity
@Table(name = "advertise")
public class AdvertiseEntity  {

    @Id
    Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "mobile_banner")
    private String mobileBanner;

    @Column(name = "desktop_banner")
    private String desktopBanner;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "enabled")
    private boolean enabled;
}
