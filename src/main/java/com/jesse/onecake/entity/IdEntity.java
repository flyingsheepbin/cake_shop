package com.jesse.onecake.entity;

import lombok.Data;

import javax.persistence.Id;


@Data
public class IdEntity {
    @Id
    private Object id;

}
