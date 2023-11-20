package com.ndt.be_stepupsneaker.core.common.base;


import java.io.IOException;

public interface BaseService<T,K,R> {
    PageableObject<T> findAllEntity(R request);

    T create(R request);

    T update(R request);

    T findById(K id);

    Boolean delete(K id);
}
