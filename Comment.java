package com.firstapp.bbb;

public class Comment {
    private String pledgeId; // ID of the pledge this comment is linked to
    private String userId; // ID of the user who posted the comment
    private String message; // The comment message
    private long timestamp; // Time the comment was posted (for sorting)

    // No-argument constructor required for Firestore
    public Comment() {}

    public Comment(String pledgeId, String userId, String message, long timestamp) {
        this.pledgeId = pledgeId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;

    }

    // Getters
    public String getPledgeId() { return pledgeId; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setPledgeId(String pledgeId){ this.pledgeId = pledgeId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }



}
