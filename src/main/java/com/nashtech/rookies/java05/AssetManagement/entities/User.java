package com.nashtech.rookies.java05.AssetManagement.entities;


import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.UserState;
import lombok.*;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "birth")
    private Date birth;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "joined_date")
    private Date joinedDate;

    @Column(name = "created_when")
    private Timestamp createdWhen;

    @Column(name = "updated_when")
    private Timestamp updatedWhen;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(name="disabled")
    private boolean isDisabled=false;

    @ManyToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "assignedTo")
    private List<Assignment> listAssignmentsTo;

    @OneToMany(mappedBy = "assignedBy")
    private List<Assignment> listAssignmentsBy;

    @OneToMany(mappedBy = "assignedTo")
    private List<Returning> listReturningAssignTo;

    @OneToMany(mappedBy = "assignedBy")
    private List<Returning> listReturningAssignBy;

    @ManyToOne
    @JoinColumn(name = "location_id",referencedColumnName = "id")
    private Location location;

}
