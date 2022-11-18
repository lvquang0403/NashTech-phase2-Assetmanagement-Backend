package com.nashtech.rookies.java05.AssetManagement.entities;


import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import lombok.*;


import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_when")
    private Timestamp createdWhen;

    @Column(name = "updated_when")
    private Timestamp updatedWhen;

    @Column(name = "note")
    private String note;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AssignmentState state;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id",referencedColumnName = "id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assignedBy_id",referencedColumnName = "id")
    private User assignedBy;

    @ManyToOne
    @JoinColumn(name = "asset_id",referencedColumnName = "id")
    private Asset asset;

    @OneToOne(mappedBy = "assignment")
    private Returning returning;
}
