package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {
    private PaginationUtil() {}

    public static long calculateTotalPages(Long totalRecord, Integer pageSize) {
        return (totalRecord % pageSize == 0 ? (totalRecord / pageSize) : (totalRecord / pageSize + 1));
    }

    public Pageable pageable(PageableRequest paginationRequest) {
        Pageable pageable = null;
        if (paginationRequest.getOrderBy().equals("asc")) {
            pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), Sort.by(paginationRequest.getSortBy()).ascending());
        }else {
            pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), Sort.by(paginationRequest.getSortBy()).descending());
        }
        return pageable;

    }


}
