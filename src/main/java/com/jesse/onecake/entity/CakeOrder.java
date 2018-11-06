package com.jesse.onecake.entity;

import com.jesse.onecake.entity.base.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "cake_order")
@Data
public class CakeOrder extends IdEntity implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "status")
    private String status;

    @Column(name = "sure_id")
    private String sureId;

    @Column(name = "sure_user")
    private String sureUser;

    @Column(name = "sure_time")
    private Date sureTime;


    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_time")
    private Date createTime;

}