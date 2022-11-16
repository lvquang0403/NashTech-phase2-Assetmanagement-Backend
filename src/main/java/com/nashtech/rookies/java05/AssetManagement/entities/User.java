package com.nashtech.rookies.java05.AssetManagement.entities;


import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "birth")
    private Timestamp birth;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "created_when")
    private Timestamp createdWhen;

    @Column(name = "updated_when")
    private Timestamp updatedWhen;

    @ManyToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;

    @OneToMany(mappedBy = "assignedTo")
    private List<Assignment> listAssignmentsTo;

    @OneToMany(mappedBy = "assignedBy")
    private List<Assignment> listAssignmentsBy;

    @ManyToOne
    @JoinColumn(name = "location_id",referencedColumnName = "id")
    private Location location;

}
