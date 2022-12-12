package com.spdev.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "Contents")
public interface ApplicationContent {

    Integer getId();

    String getLink();
}