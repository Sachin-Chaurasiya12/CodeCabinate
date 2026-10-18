package com.example.ConnectService.model.dto;

public class TermRequest {
    private boolean termreq;

    public TermRequest(boolean termreq){
        this.termreq = termreq;
    }
    public TermRequest(){}
    
    public void setTermreq(boolean termreq) {
        this.termreq = termreq;
    }
    public boolean getTermreq(){
        return termreq;
    }
}
