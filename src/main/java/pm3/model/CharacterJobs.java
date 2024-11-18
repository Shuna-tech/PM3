package pm3.model;

public class CharacterJobs {
    protected CharacterInfo character;
    protected Jobs job;          
    protected int level;
    protected int experiencePoints;
    protected boolean isCurrentJob;

    public CharacterJobs(CharacterInfo character, Jobs job, int level, 
            int experiencePoints, boolean isCurrentJob) {
        this.character = character;
        this.job = job;
        this.level = level;
        this.experiencePoints = experiencePoints;
        this.isCurrentJob = isCurrentJob;
    }

    // Getters
    public CharacterInfo getCharacter() {
        return character;
    }

    public Jobs getJob() {      
        return job;
    }

    public int getLevel() {
        return level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public boolean isCurrentJob() {
        return isCurrentJob;
    }

    // Setters
    public void setCharacter(CharacterInfo character) {
        this.character = character;
    }

    public void setJob(Jobs job) {    
        this.job = job;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public void setCurrentJob(boolean isCurrentJob) {
        this.isCurrentJob = isCurrentJob;
    }

    @Override
    public String toString() {
        return "CharacterJobs{" +
                "characterID=" + character.getCharacterID() +
                ", jobID=" + job.getJobID() +     
                ", level=" + level +
                ", experiencePoints=" + experiencePoints +
                ", isCurrentJob=" + isCurrentJob +
                '}';
    }
}