package com.nashtech.rookies.java05.AssetManagement.entities;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "returnings")
public class Returning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "returned_date")
    private Timestamp returnedDate;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AssignmentReturnState state;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id",referencedColumnName = "id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assignedBy_id",referencedColumnName = "id")
    private User assignedBy;

    @ManyToOne
    @JoinColumn(name = "acceptedBy_id",referencedColumnName = "id")
    private User acceptedBy;

    @OneToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "asset_id", referencedColumnName = "id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "requested_by_user_id",referencedColumnName = "id")
    private User requestedBy;


}
