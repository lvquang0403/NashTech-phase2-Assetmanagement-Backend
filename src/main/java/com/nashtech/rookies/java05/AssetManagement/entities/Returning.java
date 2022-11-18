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
    @JoinColumn(name = "requestedBy_id",referencedColumnName = "id")
    private User requestBy;

    @ManyToOne
    @JoinColumn(name = "acceptedBy_id",referencedColumnName = "id")
    private User acceptBy;

    @OneToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;
}
