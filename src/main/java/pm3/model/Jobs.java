package pm3.model;

public class Jobs {
    protected int jobID;
    protected String jobName;

    public Jobs(String jobName) {
    	this.jobName = jobName;
    }
    
    public Jobs(int jobID, String jobName) {
        this.jobID = jobID;
        this.jobName = jobName;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "Jobs{" +
                "jobID=" + jobID +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}