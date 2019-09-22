package com.gobucket.httpfeeder.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gobucket.dynamodb.repositories.CouponRepository;
import com.gobucket.dynamodb.repositories.models.coupon.Coupon;
import com.gobucket.httpfeeder.models.Coupons;
import com.gobucket.httpfeeder.models.Coupons.Item;

@Component
public class MerchantFeederServices {
	@Autowired CouponRepository couponRepository;
	public void saveToDB(Coupons coupons) {
		String zid = coupons.getZID();
		List<Item> items = coupons.getItem();
		Integer nid = coupons.getNID();
		Integer pid = coupons.getPID();
		for(Item item: items) {
			Coupon coupon = new Coupon();
			coupon.setZID(zid);
			coupon.setNID(nid);
			coupon.setPID(pid);
			coupon.setActivedate(item.getActivedate());
			coupon.setBrand(item.getBrand());
			coupon.setCouponid(item.getCouponid());
			coupon.setDescription(item.getDescription());
			coupon.setExpiration(item.getExpiration());
			coupon.setGeotarget(item.getGeotarget());
			coupon.setImage(item.getImage());
			coupon.setLink(item.getLink());
			coupon.setMajorCategory(item.getMajorCategory());
			coupon.setMinorCategory(item.getMinorCategory());
			coupon.setShutoff(item.getShutoff());
			coupon.setValue(item.getValue());
			couponRepository.save(coupon);
		}
	}
	public Optional<Coupon> getCoupon(Long couponId) {
		return couponRepository.findById(couponId);
	}
	public List<Coupon> getCoupon(String brand) {
		return couponRepository.findByBrandContains(brand);
	}
	public List<Coupon> getCoupons() {
		Iterable<Coupon> coups = couponRepository.findAll();
		List<Coupon> coupons = new ArrayList<>();
		coups.forEach(coupons::add);
		return coupons;
	}
	public long getCouponsCount() {
		return couponRepository.count();
	}
	public String deleteCoupons() {
		couponRepository.deleteAll();
		return "success";
	}
}
