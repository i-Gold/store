package com.company.storehouse.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User implements Serializable {
    private static final long serialVersionUID = -905968913257827720L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Full.class, View.Part.class})
    private Long id;

    @JsonView({View.Full.class, View.Part.class})
    private String email;

    @JsonView({View.Full.class, View.Part.class})
    private String username;

    private String password;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonView({View.Full.class})
    private Role role;

}
