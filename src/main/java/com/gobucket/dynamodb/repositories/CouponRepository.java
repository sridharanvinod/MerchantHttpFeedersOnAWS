package com.gobucket.dynamodb.repositories;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.gobucket.dynamodb.repositories.models.coupon.Coupon;

@EnableScan
public interface CouponRepository extends PagingAndSortingRepository<Coupon, Long>  {
	public List<Coupon> findByBrandContains(String brand);
}
