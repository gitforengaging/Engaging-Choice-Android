package com.aap.engagingchoice.network;

/**
 * This class is used to set Secret key ,EmailId and content Id
 */
public class EngagingChoiceKey {
    private static EngagingChoiceKey mPublishSecretKey;

    private String publishSecretKey;
    private String emailId;
    private int contentId;

    private EngagingChoiceKey() {
    }

    public static EngagingChoiceKey getInstance() {
        if (mPublishSecretKey == null) {
            mPublishSecretKey = new EngagingChoiceKey();
        }
        return mPublishSecretKey;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPublishSecretKey() {
        return publishSecretKey;
    }

    public void setPublishSecretKey(String publishSecretKey) {
        this.publishSecretKey = publishSecretKey;
    }


}
