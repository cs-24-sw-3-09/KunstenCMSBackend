package com.github.cs_24_sw_3_09.CMS.mappers;

public interface Mapper<A, B> {
    B mapTo(A a);

    A mapFrom(B b);
}
