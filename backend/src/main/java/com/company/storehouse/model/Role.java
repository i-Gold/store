package com.company.storehouse.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.company.storehouse.controller.response.View;
import com.company.storehouse.model.enumeration.RoleName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(of = "id")
public class Role implements Serializable {
    private static final long serialVersionUID = -2293167774395576944L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView({View.Full.class})
    private RoleName name;

    @OneToOne(mappedBy = "role")
    private User user;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Authority> authorities = new HashSet<>();

    public Role() {
    }

    public Role(RoleName roleName) {
        this.name = roleName;
    }
}
