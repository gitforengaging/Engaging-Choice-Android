package com.aap.engagingchoice.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo of Offerlist api
 */
public class EcOfferListResponse implements Serializable {

    /**
     * server_datetime : 2018-09-25
     * detail : all offer list
     * data : [{"id":2,"tracking_number":"kdjnfgkndfkjgdf","offer_title":"kdhfgkdfgdfg","offer_description":"dfg.df,lg. dfgkdfkgdfgdf","offer_start_date":"2018-09-01","offer_end_date":"2018-12-20","offer_url":"https://www.w3.org/","offer_budget":"1","price_engagement":"1","coupon_code":"fgdfg","number_of_uses":1,"offer_uses_time":10,"status":1,"file_name":"c9ez79su9.mp4","file_type":2,"discount":"$10","discount_type":2}]
     * pagination : {"join_days":1,"file_url":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/","count":1,"num_pages":1,"is_popup":0,"per_page":3}
     */

    private String server_datetime;
    private String detail;
    private PaginationBean pagination;
    private List<DataBean> data;

    public String getServer_datetime() {
        return server_datetime;
    }

    public void setServer_datetime(String server_datetime) {
        this.server_datetime = server_datetime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PaginationBean implements Serializable {
        /**
         * join_days : 1
         * file_url : https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/
         * count : 1
         * num_pages : 1
         * is_popup : 0
         * per_page : 3
         */

        private int join_days;
        private String file_url;
        private int count;
        private int num_pages;
        private int is_popup;
        private int per_page;
        private int is_other_user;

        public int getIs_other_user() {
            return is_other_user;
        }

        public void setIs_other_user(int is_other_user) {
            this.is_other_user = is_other_user;
        }

        public int getJoin_days() {
            return join_days;
        }

        public void setJoin_days(int join_days) {
            this.join_days = join_days;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNum_pages() {
            return num_pages;
        }

        public void setNum_pages(int num_pages) {
            this.num_pages = num_pages;
        }

        public int getIs_popup() {
            return is_popup;
        }

        public void setIs_popup(int is_popup) {
            this.is_popup = is_popup;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }
    }

    public static class DataBean implements Serializable {
        /**
         * id : 2
         * tracking_number : kdjnfgkndfkjgdf
         * offer_title : kdhfgkdfgdfg
         * offer_description : dfg.df,lg. dfgkdfkgdfgdf
         * offer_start_date : 2018-09-01
         * offer_end_date : 2018-12-20
         * offer_url : https://www.w3.org/
         * offer_budget : 1
         * price_engagement : 1
         * coupon_code : fgdfg
         * number_of_uses : 1
         * offer_uses_time : 10
         * status : 1
         * file_name : c9ez79su9.mp4
         * file_type : 2
         * discount : $10
         * discount_type : 2
         */

        private int id;
        private String tracking_number;
        private String offer_title;
        private String offer_description;
        private String offer_start_date;
        private String offer_end_date;
        private String offer_url;
        private String offer_budget;
        private String price_engagement;
        private String coupon_code;
        private int number_of_uses;
        private int offer_uses_time;
        private int status;
        private String file_name;
        private int file_type;
        private String discount;
        private int discount_type;
        private String cover_image;

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTracking_number() {
            return tracking_number;
        }

        public void setTracking_number(String tracking_number) {
            this.tracking_number = tracking_number;
        }

        public String getOffer_title() {
            return offer_title;
        }

        public void setOffer_title(String offer_title) {
            this.offer_title = offer_title;
        }

        public String getOffer_description() {
            return offer_description;
        }

        public void setOffer_description(String offer_description) {
            this.offer_description = offer_description;
        }

        public String getOffer_start_date() {
            return offer_start_date;
        }

        public void setOffer_start_date(String offer_start_date) {
            this.offer_start_date = offer_start_date;
        }

        public String getOffer_end_date() {
            return offer_end_date;
        }

        public void setOffer_end_date(String offer_end_date) {
            this.offer_end_date = offer_end_date;
        }

        public String getOffer_url() {
            return offer_url;
        }

        public void setOffer_url(String offer_url) {
            this.offer_url = offer_url;
        }

        public String getOffer_budget() {
            return offer_budget;
        }

        public void setOffer_budget(String offer_budget) {
            this.offer_budget = offer_budget;
        }

        public String getPrice_engagement() {
            return price_engagement;
        }

        public void setPrice_engagement(String price_engagement) {
            this.price_engagement = price_engagement;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public int getNumber_of_uses() {
            return number_of_uses;
        }

        public void setNumber_of_uses(int number_of_uses) {
            this.number_of_uses = number_of_uses;
        }

        public int getOffer_uses_time() {
            return offer_uses_time;
        }

        public void setOffer_uses_time(int offer_uses_time) {
            this.offer_uses_time = offer_uses_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getDiscount_type() {
            return discount_type;
        }

        public void setDiscount_type(int discount_type) {
            this.discount_type = discount_type;
        }
    }
}
