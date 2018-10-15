package com.aap.engagingchoice.pojo;

/**
 * Pojo of UpdateInfo api
 */
public class EcUpdateInfoReq {
    private String email;
    private String mobile_no;
    private String old_email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getOld_email() {
        return old_email;
    }

    public void setOld_email(String old_email) {
        this.old_email = old_email;
    }
}
