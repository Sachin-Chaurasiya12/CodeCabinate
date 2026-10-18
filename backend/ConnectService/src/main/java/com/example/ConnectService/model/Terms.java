package com.example.ConnectService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity 
@Table(name = "terms",
    indexes = @Index(name = "idx_userid", columnList = "userId")
)
public class Terms {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private boolean IstermChecked;

    public Terms(Long userId,boolean IstermChecked){
        this.userId = userId;
        this.IstermChecked = IstermChecked;
    }
    public Terms(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setIstermChecked(boolean istermChecked) {
        IstermChecked = istermChecked;
    }
    public boolean getIstermChecked(){
        return IstermChecked;
    }
}
