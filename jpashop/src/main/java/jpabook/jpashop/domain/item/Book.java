package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.core.sym.Name;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("book")
@Getter @Setter
public class Book extends Item {
    
    private String author;
    
    private String isbn;
}
