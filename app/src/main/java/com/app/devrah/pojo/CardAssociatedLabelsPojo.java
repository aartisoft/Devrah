package com.app.devrah.pojo;

/**
 * Created by Rizwan Butt on 07-Aug-17.
 */

public class CardAssociatedLabelsPojo {
    String[] label;

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    String[] members;
    public String boardAssociatedLabelsId;

    public String getBoardAssociatedLabelsId() {
        return boardAssociatedLabelsId;
    }

    public void setBoardAssociatedLabelsId(String boardAssociatedLabelsId) {
        this.boardAssociatedLabelsId = boardAssociatedLabelsId;
    }

    public String[] getLabelText() {
        return labelText;
    }

    public void setLabelText(String[] labelText) {
        this.labelText = labelText;
    }

    String[] labelText;
    public String[] getLabels() {
        return label;
    }

    public void setLabels(String label[]) {
        this.label = label;
    }

    public String getLabelTextCards() {
        return labelTextCards;
    }

    public void setLabelTextCards(String labelTextCards) {
        this.labelTextCards = labelTextCards;
    }

    public String labelTextCards;

    public String getLabelColorCards() {
        return labelColorCards;
    }

    public void setLabelColorCards(String labelColorCards) {
        this.labelColorCards = labelColorCards;
    }

    public String labelColorCards;

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }

    public String labelid;


}
