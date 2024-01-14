package com.nfa.entity.secondary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword")
@Data
@NoArgsConstructor
public class SecondaryKeyword {

    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

}
