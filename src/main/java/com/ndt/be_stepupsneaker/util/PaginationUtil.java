package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaginationUtil {
    private PaginationUtil() {}

    public static long calculateTotalPages(Long totalRecord, Integer pageSize) {
        return (totalRecord % pageSize == 0 ? (totalRecord / pageSize) : (totalRecord / pageSize + 1));
    }

    public Pageable pageable(PageableRequest paginationRequest) {
//        Pageable pageable = null;
//        if (paginationRequest.getOrderBy().equals("asc")) {
//            pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), Sort.by(paginationRequest.getSortBy()).ascending());
//        }else {
//            pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), Sort.by(paginationRequest.getSortBy()).descending());
//        }
//        return pageable;
//
        Sort sort = null;
        Map<String, String> sortByMapping = new HashMap<>();
        sortByMapping.put("voucherCode", "voucher.code");
        sortByMapping.put("orderCode", "order.code");
        String sortByValue = sortByMapping.getOrDefault(paginationRequest.getSortBy(), paginationRequest.getSortBy());
        sort = paginationRequest.getOrderBy().equals("asc") ? Sort.by(sortByValue).ascending() : Sort.by(sortByValue).descending();
        return PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize(), sort);

    }
}
