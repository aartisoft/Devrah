package com.app.devrah.pojo;

/**
 * Created by Hamza Android on 10/23/2017.
 */
public class CardAssociatedCalendarMembersPojo {
    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    String[] members;

    public String getMemberSubscribed() {
        return memberSubscribed;
    }

    public void setMemberSubscribed(String memberSubscribed) {
        this.memberSubscribed = memberSubscribed;
    }

    String memberSubscribed;

    public String[] getInitials() {
        return initials;
    }

    public void setInitials(String[] initials) {
        this.initials = initials;
    }

    String[] initials;

    public String[] getGp_pictures() {
        return gp_pictures;
    }

    public void setGp_pictures(String[] gp_pictures) {
        this.gp_pictures = gp_pictures;
    }

    String[] gp_pictures;
}
