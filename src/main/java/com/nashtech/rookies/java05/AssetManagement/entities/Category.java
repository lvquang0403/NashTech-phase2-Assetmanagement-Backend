package com.nashtech.rookies.java05.AssetManagement.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Asset> listAssets;

}
