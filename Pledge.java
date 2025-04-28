package com.firstapp.bbb;

// model class for a Pledge
public class Pledge {
    private String description;
    private String condition;
    private String acceptedBy;
    private boolean isAccepted;
    private String id;
    private String userId;
    private boolean isCompleted;

    private String dueDate;

    private boolean isMutual;
    private String mutualDescription;
    private boolean creatorCompleted;
    private boolean acceptorCompleted;


    public Pledge() { }


    public Pledge(String description, String condition, String acceptedBy, boolean isAccepted) {
        this.description = description;
        this.condition   = condition;
        this.acceptedBy = acceptedBy;
        this.isAccepted = isAccepted;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public String getCondition() {
        return condition;
    }
    public String getAcceptedBy() {
        return acceptedBy;
    }
    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public void setIsAccepted(boolean isAccepted){
        this.isAccepted = isAccepted;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getUserId() {
       return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public boolean getIsCompleted(){
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    // Mutual pledge getters and setters
    public boolean getIsMutual() {return isMutual;}
    public void setIsMutual(boolean isMutual) {this.isMutual = isMutual; }

    public String getMutualDescription() {return mutualDescription; }
    public void setMutualDescription(String mutualDescription) {this.mutualDescription = mutualDescription; }

    public boolean getCreatorCompleted() { return creatorCompleted; }
    public void setCreatorCompleted(boolean creatorCompleted) {this.creatorCompleted = creatorCompleted; }

    public boolean getAcceptedCompleted() { return acceptorCompleted; }
    public void setAcceptorCompleted(boolean acceptorCompleted) { this.acceptorCompleted = acceptorCompleted; }

}