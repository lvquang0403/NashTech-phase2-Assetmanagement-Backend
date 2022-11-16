package com.nashtech.rookies.java05.AssetManagement.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "present_id")
public class PresentId {
    @Id
    private String id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "asset_id")
    private Integer assetId;


}
