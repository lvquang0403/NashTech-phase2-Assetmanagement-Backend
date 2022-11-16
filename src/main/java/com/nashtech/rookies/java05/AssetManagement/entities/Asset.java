package com.nashtech.rookies.java05.AssetManagement.entities;


import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Recycle;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "asset")
public class Asset {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "specification")
    private String specification;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AssetState state;

    @Column(name = "created_when")
    private Timestamp createdWhen;

    @Column(name = "updated_when")
    private Timestamp updatedWhen;

    @Column(name = "recycling")
    @Enumerated(EnumType.STRING)
    private Recycle recycle;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "asset")
    private List<Assignment> listAssignments;

    @ManyToOne
    @JoinColumn(name = "location_id",referencedColumnName = "id")
    private Location location;
}
