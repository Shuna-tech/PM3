package pm3.model;

public class GearAndWeaponJobs {
    protected Items item;
    protected Jobs job;

    public GearAndWeaponJobs(Items item, Jobs job) {
        this.item = item;
        this.job = job;
    }

    public Items getItem() { return item; }
    public Jobs getJob() { return job; }
}