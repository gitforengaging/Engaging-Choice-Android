package com.aap.engagingchoice.pojo;

import java.util.List;

/**
 * Pojo of Content Api Response
 */
public class EcContentResponse {

    /**
     * detail : all content list
     * data : [{"id":10,"content_title":"@#$$$%%%","content_description":"qweqweqwe\r\newewfrewte","content_start_date":"2018-08-21","content_end_date":"2018-10-19","file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/29dqtxh4q.mp4","status":1,"user_id":32,"range":0,"entire_us":1,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":25,"content_title":"New#Latest","content_description":"Latest moview","content_start_date":"2018-09-27","content_end_date":null,"file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/8w9nuaij7.mp4","status":1,"user_id":32,"range":0,"entire_us":0,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":30,"content_title":"content234","content_description":"edwefuewf\r\nfergjergjre\r\nregmkergmre\r\nregkregjr\r\nregrkgjm","content_start_date":"2018-09-19","content_end_date":"2018-10-01","file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/7sfz4dlz0.mp4","status":1,"user_id":81,"range":0,"entire_us":1,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":33,"content_title":"Testing 1234","content_description":"wdewrwt\r\nretrytry\r\nuytyutyu\r\nuiuyiuyi\r\nouiuo","content_start_date":"2018-09-03","content_end_date":"2018-10-03","file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/43nbyi0zp.mp4","status":1,"user_id":32,"range":0,"entire_us":1,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":34,"content_title":"content relevant","content_description":"tetsing testing testong testing testing testing","content_start_date":"2018-09-08","content_end_date":"2018-09-30","file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/fakiw8lpz.mp4","status":1,"user_id":32,"range":1,"entire_us":0,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":35,"content_title":"content_123","content_description":"content 12344","content_start_date":"2018-08-29","content_end_date":"2018-10-04","file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/65yqiw0fj.mp4","status":1,"user_id":32,"range":0,"entire_us":1,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":1,"content_title":"fgdfgdfgdfgdf","content_description":"dfgdfgdfgdfgdf","content_start_date":"2018-08-29","content_end_date":null,"file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/horkrqm9u.mp4","status":1,"user_id":31,"range":0,"entire_us":0,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"},{"id":1,"content_title":"fgdfgdfgdfgdf","content_description":"dfgdfgdfgdfgdf","content_start_date":"2018-08-29","content_end_date":null,"file_type":2,"file_name":"https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/horkrqm9u.mp4","status":1,"user_id":31,"range":0,"entire_us":0,"cover_image":"https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg"}]
     */

    private String detail;
    private List<DataBean> data;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 10
         * content_title : @#$$$%%%
         * content_description : qweqweqwe
         ewewfrewte
         * content_start_date : 2018-08-21
         * content_end_date : 2018-10-19
         * file_type : 2
         * file_name : https://fn-qa.s3.us-east-2.amazonaws.com/provider/content/29dqtxh4q.mp4
         * status : 1
         * user_id : 32
         * range : 0
         * entire_us : 1
         * cover_image : https://fn-dev.s3.us-east-2.amazonaws.com/marketer/offer/z9cozxcwd.jpeg
         */

        private int id;
        private String content_title;
        private String content_description;
        private String content_start_date;
        private String content_end_date;
        private int file_type;
        private String file_name;
        private int status;
        private int user_id;
        private int range;
        private int entire_us;
        private String cover_image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent_title() {
            return content_title;
        }

        public void setContent_title(String content_title) {
            this.content_title = content_title;
        }

        public String getContent_description() {
            return content_description;
        }

        public void setContent_description(String content_description) {
            this.content_description = content_description;
        }

        public String getContent_start_date() {
            return content_start_date;
        }

        public void setContent_start_date(String content_start_date) {
            this.content_start_date = content_start_date;
        }

        public String getContent_end_date() {
            return content_end_date;
        }

        public void setContent_end_date(String content_end_date) {
            this.content_end_date = content_end_date;
        }

        public int getFile_type() {
            return file_type;
        }

        public void setFile_type(int file_type) {
            this.file_type = file_type;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getRange() {
            return range;
        }

        public void setRange(int range) {
            this.range = range;
        }

        public int getEntire_us() {
            return entire_us;
        }

        public void setEntire_us(int entire_us) {
            this.entire_us = entire_us;
        }

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }
    }
}
