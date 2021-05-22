package massbalancer.Unit;

/**
 * Attributes, both edu and program-specific, that are added to a Unit and can be
 * queried by a Balancer to calculate mass.
 */
public enum UnitAttribute {
    ARMOR,
    DEFENCE_SKILL,
    SHIELD,

    SIDE_SIDE_SPACING,
    FRONT_BACK_SPACING,

    SPEAR("spear"),
    SWORD("sword"),
    PIKE,
    MELEE_ATTACK,

    LIGHT_SPEAR("light_spear"),

    BOW,
    JAVELIN("javelin"),
    SLING,
    RANGED_ATTACK,

    COMMAND("command"),
    CANNOT_SKIRMISH("cannot skirmish"),

    HARDY("hardy"),
    VERY_HARDY("very_hardy"),

    MORALE,
    DISCIPLINE,
    TRAINING,

    NAME,





    IGNORE,
    ;

    private final String eduName;

    UnitAttribute(){
        eduName = null;
    }

    UnitAttribute(final String eduName){
        this.eduName = eduName;
    }

    public String getEduName(){
        return eduName;
    }
}
