package jpabook.jpashop.domain.item;

import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
    
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /**
     * do not recommend ManyToMany relation Entity in production product 
     * - can't add column to relation table
     * - cause many operation issue
     * recommend to make relation Entity
     * - AS-IS: target Entity > (ManyToMany) < target Entity 
     * - TO-BE: target Entity > (ManyToOne) > relation Entity < (OneToMany) < target Entity
     */
    @ManyToMany 
    @JoinTable(name = "category_item", 
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
}
