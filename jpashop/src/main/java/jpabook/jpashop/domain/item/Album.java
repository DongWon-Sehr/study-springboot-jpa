package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("album")
@Getter @Setter
public class Album extends Item {
    
    private String artist;
    
    private String etc;
}
