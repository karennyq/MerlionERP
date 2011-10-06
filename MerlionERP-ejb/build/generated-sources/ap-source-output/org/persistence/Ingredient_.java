package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.RawMaterial;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(Ingredient.class)
public class Ingredient_ { 

    public static volatile SingularAttribute<Ingredient, Long> ingredient_id;
    public static volatile SingularAttribute<Ingredient, Integer> quantity;
    public static volatile SingularAttribute<Ingredient, RawMaterial> rawMaterial;

}