package ru.practicum.shareit.item.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ITEMS", schema = "public")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "IS_AVAILABLE")
    private Boolean available;
    @Column(name = "OWNER_ID")
    private Long owner;
    @Column(name = "REQUEST_ID")
    private Long request;
}